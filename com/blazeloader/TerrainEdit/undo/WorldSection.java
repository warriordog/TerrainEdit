package com.blazeloader.TerrainEdit.undo;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import com.blazeloader.api.direct.server.api.block.ApiBlockServer;
import com.blazeloader.api.direct.server.api.block.ENotificationType;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * A section of a world containing blocks, entities, and tile entities.
 */
public class WorldSection {
    private int x1, y1, z1, x2, y2, z2;
    private Block[] blocks;
    private int[] blockDATAs;
    private NBTTagCompound tileEntities = new NBTTagCompound();

    public WorldSection(int x1, int y1, int z1, int x2, int y2, int z2, World world) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        blocks = new Block[(Math.abs(x2 - x1) + 1) * (Math.abs(y2 - y1) + 1) * (Math.abs(z2 - z1) + 1)];
        blockDATAs = new int[(Math.abs(x2 - x1) + 1) * (Math.abs(y2 - y1) + 1) * (Math.abs(z2 - z1) + 1)];
        int currIndex = 0;
        for (int currX = Math.min(x1, x2); currX <= Math.max(x2, x1); currX++) {
            for (int currY = Math.min(y1, y2); currY <= Math.max(y2, y1); currY++) {
                for (int currZ = Math.min(z1, z2); currZ <= Math.max(z2, z1); currZ++) {
                    blocks[currIndex] = ApiBlockServer.getBlockAt(world, currX, currY, currZ);
                    blockDATAs[currIndex] = world.getBlockMetadata(currX, currY, currZ);
                    TileEntity te = world.getTileEntity(currX, currY, currZ);
                    if (te != null) {
                        NBTTagCompound thisTile = new NBTTagCompound();
                        te.writeToNBT(thisTile);
                        tileEntities.setTag(currX + "," + currY + "," + currZ, thisTile);
                    }
                    currIndex++;
                }
            }
        }
    }

    public WorldSection(Cuboid cuboid, World world) {
        this(cuboid.getXPos1(), cuboid.getYPos1(), cuboid.getZPos1(), cuboid.getXPos2(), cuboid.getYPos2(), cuboid.getZPos2(), world);
    }

    public void writeInto(World world) {
        int currIndex = 0;
        for (int currX = Math.min(x1, x2); currX <= Math.max(x2, x1); currX++) {
            for (int currY = Math.min(y1, y2); currY <= Math.max(y2, y1); currY++) {
                for (int currZ = Math.min(z1, z2); currZ <= Math.max(z2, z1); currZ++) {
                    ApiBlockServer.setBlockAt(world, currX, currY, currZ, blocks[currIndex], blockDATAs[currIndex], ENotificationType.NOTIFY_CLIENTS.getType());
                    TileEntity te = world.getTileEntity(currX, currY, currZ);
                    if (te != null) {
                        world.setTileEntity(currX, currY, currZ, TileEntity.createAndLoadEntity(tileEntities.getCompoundTag(currX + "," + currY + "," + currZ)));
                    }
                    currIndex++;
                }
            }
        }
    }
}
