package com.blazeloader.TerrainEdit.schematic;

import com.blazeloader.TerrainEdit.main.BlockAccess;
import com.blazeloader.api.api.block.ApiBlock;
import com.blazeloader.api.api.block.NotificationType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;

@Deprecated
/**
 * Represents a schematic.  Very old and buggy code.  Must be tied to a File that contains the schematic.
 */
public class Schematic {
    NBTTagCompound schematic;
    short height;
    short length;
    short width;
    byte[] blocks;
    byte[] data;
    String materials;

    NBTTagList entities;
    NBTTagList tileEntities;

    public Schematic(File path) {
        try {
            this.schematic = CompressedStreamTools.readCompressed(new FileInputStream(path));
            height = this.schematic.getShort("Height");
            length = this.schematic.getShort("Length");
            width = this.schematic.getShort("Width");
            blocks = this.schematic.getByteArray("Blocks");
            data = this.schematic.getByteArray("Data");
            materials = this.schematic.getString("Materials");
            entities = this.schematic.getTagList("Entities", 0);
            tileEntities = this.schematic.getTagList("TileEntities", 0);
        } catch (Exception e) {
            throw new RuntimeException("Error loading schematic!", e);
        }
    }

    public void place(World world, int x, int y, int z) {
        try {
            int currBlock = 0;
            //For loops MUST stay in order!!
            for (int currY = y; currY < y + height; currY++) {
                for (int currZ = z; currZ < z + length; currZ++) {
                    for (int currX = x; currX < x + width; currX++) {
                        BlockAccess.setBlockAt(world, currX, currY, currZ, ApiBlock.getBlockById(blocks[currBlock]), data[currBlock], NotificationType.NOTIFY_CLIENTS);
                        currBlock++;
                    }
                }
            }

            Collection<TileEntity> tileEntityCollection = new ArrayList<TileEntity>();

            for (int count = 0; count < tileEntities.tagCount(); count++) {
                TileEntity currTileEntity = TileEntity.createAndLoadEntity(tileEntities.getCompoundTagAt(count));
                if (currTileEntity != null) {
                    BlockPos pos = currTileEntity.getPos();
                    currTileEntity.setPos(new BlockPos(pos.getX() + length, pos.getY() + height, pos.getZ() + width));
                    tileEntityCollection.add(currTileEntity);
                }
            }
            world.addTileEntities(tileEntityCollection);

            for (int count = 0; count < entities.tagCount(); count++) {
                NBTTagCompound currTag = entities.getCompoundTagAt(count);
                NBTTagList currRot = currTag.getTagList("Rotation", 0);
                Entity currEntity = EntityList.createEntityByName((currTag.getString("id")), world);
                if (currEntity != null) {
                    currEntity.setLocationAndAngles(x, y, z, currRot.getFloat(0), currRot.getFloat(1));
                    currEntity.setAir(currTag.getShort("Air"));
                    currEntity.setFire(currTag.getShort("Fire"));
                    currEntity.fallDistance = (currTag.getFloat("FallDistance"));
                    currEntity.onGround = (currTag.getBoolean("OnGround"));
                    world.spawnEntityInWorld(currEntity);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception placing schematic!", e);
        }
    }
}
