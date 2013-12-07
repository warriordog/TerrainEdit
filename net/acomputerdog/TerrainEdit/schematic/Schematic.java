package net.acomputerdog.TerrainEdit.schematic;

import net.acomputerdog.BlazeLoader.api.block.ApiBlock;
import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;

@Deprecated
/**
 * Represents a schematic.  Very old and buggy code.  Must be tied to a File that contains the schematic.
 */
public class Schematic{
	NBTTagCompound schematic;
	short height;
	short length;
	short width;
	byte[] blocks;
	byte[] data;
	String materials;

	NBTTagList entities;
	NBTTagList tileEntities;

    public Schematic(File path){
        try{
            this.schematic = CompressedStreamTools.readCompressed(new FileInputStream(path));
            height = this.schematic.getShort("Height");
            length = this.schematic.getShort("Length");
            width = this.schematic.getShort("Width");
            blocks = this.schematic.getByteArray("Blocks");
            data = this.schematic.getByteArray("Data");
            materials = this.schematic.getString("Materials");
            entities = this.schematic.func_150295_c("Entities", 0);
            tileEntities = this.schematic.func_150295_c("TileEntities", 0);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error loading schematic!", e);
        }
    }

    public void place(World world, int x, int y, int z){
        try{
            int currBlock = 0;
            //For loops MUST stay in order!!
            for (int currY = y; currY < y + height; currY++){
                for (int currZ = z; currZ < z + length; currZ++){
                    for (int currX = x; currX < x + width; currX++){
                        ApiBlock.setBlock(world, currX, currY, currZ, Block.func_149729_e(blocks[currBlock]), data[currBlock], ENotificationType.NOTIFY_CLIENTS.getType());
                        currBlock++;
                    }
                }
            }

            Collection<TileEntity> tileEntityCollection = new ArrayList<TileEntity>();

            for (int count = 0; count < tileEntities.tagCount(); count++){
                TileEntity currTileEntity = TileEntity.func_145827_c(tileEntities.func_150305_b(count));
                if (currTileEntity != null){
                    currTileEntity.field_145851_c = currTileEntity.field_145851_c + length;
                    currTileEntity.field_145848_d = currTileEntity.field_145848_d + height;
                    currTileEntity.field_145849_e = currTileEntity.field_145849_e + width;
                    tileEntityCollection.add(currTileEntity);
                }
            }
            world.func_147448_a(tileEntityCollection);

            for (int count = 0; count < entities.tagCount(); count++){
                NBTTagCompound currTag = entities.func_150305_b(count);
                NBTTagList currRot = currTag.func_150295_c("Rotation", 0);
                Entity currEntity = EntityList.createEntityByName((currTag.getString("id")), world);
                if (currEntity != null){
                    currEntity.setLocationAndAngles(x, y, z, currRot.func_150308_e(0), currRot.func_150308_e(1));
                    currEntity.setAir(currTag.getShort("Air"));
                    currEntity.setFire(currTag.getShort("Fire"));
                    currEntity.fallDistance = (currTag.getFloat("FallDistance"));
                    currEntity.onGround = (currTag.getBoolean("OnGround"));
                    world.spawnEntityInWorld(currEntity);
                }
            }
        }
        catch (Exception e){
            throw new RuntimeException("Exception placing schematic!", e);
        }
    }
}
