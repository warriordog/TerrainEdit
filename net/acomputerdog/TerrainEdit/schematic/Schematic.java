package net.acomputerdog.TerrainEdit.schematic;

import net.acomputerdog.BlazeLoader.api.block.ApiBlock;
import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagFloat;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

	String version = "0.2";	

    public Schematic(File path){
        try{
            this.schematic = CompressedStreamTools.readCompressed(new FileInputStream(path));
            height = this.schematic.getShort("Height");
            length = this.schematic.getShort("Length");
            width = this.schematic.getShort("Width");
            blocks = this.schematic.getByteArray("Blocks");
            data = this.schematic.getByteArray("Data");
            materials = this.schematic.getString("Materials");
            entities = this.schematic.getTagList("Entities");
            tileEntities = this.schematic.getTagList("TileEntities");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error loading schematic!", e);
        }
    }

    public void place(int x, int y, int z, int d){
        try{
            int currBlock = 0;
            //For loops MUST stay in order!!
            for (int currY = y; currY < y + height; currY++){
                for (int currZ = z; currZ < z + length; currZ++){
                    for (int currX = x; currX < x + width; currX++){
                        ApiBlock.setBlock(d, currX, currY, currZ, blocks[currBlock], data[currBlock], ENotificationType.NOTIFY_CLIENTS.getType());
                        currBlock++;
                    }
                }
            }

            /*
            Collection<TileEntity> tileEntityCollection = new ArrayList<TileEntity>();

            for (int count = 0; count < tileEntities.tagCount(); count++){
                TileEntity currTileEntity = TileEntity.createAndLoadEntity((NBTTagCompound)tileEntities.tagAt(count));

                if (currTileEntity != null){
                    currTileEntity.xCoord = currTileEntity.xCoord + length;
                    currTileEntity.yCoord = currTileEntity.yCoord + height;
                    currTileEntity.zCoord = currTileEntity.zCoord + width;
                    tileEntityCollection.add(currTileEntity)
                }
            }

            worldServers[d].addTileEntity(tileEntityCollection);//add the tile entity collection to world
            //end load tile entities--------------------------------------------------
            //load entities-----------------------------------------------------------
            List entityCollection = new ArrayList<EntityList>();// list to hold entities

            for (int count = 0; count < entities.tagCount(); count++)//count tile entities in schematic
            {
                NBTTagCompound currTag = (NBTTagCompound)entities.tagAt(count);//gets entity's tags
                NBTTagList currRot = currTag.getTagList("Rotation");//get entity rotation
                Entity currEntity = EntityList.createEntityByName((currTag.getString("id")), worldServers[d]);//create a new entity

                if (currEntity != null)
                {
                    //set the rotation and position of entity
                    currEntity.setLocationAndAngles(x, y, z, ((NBTTagFloat)currRot.tagAt(0)).data, ((NBTTagFloat)currRot.tagAt(1)).data);
                    currEntity.setAir(currTag.getShort("Air"));//sets the entity's air
                    currEntity.setFire(currTag.getShort("Fire"));//sets the entity' fire
                    currEntity.fallDistance = (currTag.getFloat("FallDistance")); //sets the entity's fall distance
                    currEntity.onGround = (currTag.getBoolean("OnGround")); //sets weather the entity is on the ground
                    worldServers[d].spawnEntityInWorld(currEntity);//add entity to world
                }
            }

            //end load entities----------------------------------------------------------
            return true;
            */
        }
        catch (Exception e){
            throw new RuntimeException("Exception placing schematic!", e);
        }
    }
}
