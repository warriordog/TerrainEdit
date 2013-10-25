package net.acomputerdog.TerrainEdit.undo;

import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.minecraft.src.World;

/**
 * A section of a world containing blocks, entities, and tile entities.
 */
public class WorldSection {
    private int x1, y1, z1, x2, y2, z2;
    private int[] blockIDs;
    private int[] blockDATAs;

    public WorldSection(int x1, int y1, int z1, int x2, int y2, int z2, World world) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        blockIDs = new int[(Math.abs(x2 - x1) + 1) * (Math.abs(y2 - y1) + 1) * (Math.abs(z2 - z1) + 1)];
        blockDATAs = new int[(Math.abs(x2 - x1) + 1) * (Math.abs(y2 - y1) + 1) * (Math.abs(z2 - z1) + 1)];
        int currIndex = 0;
        for(int currX = Math.min(x1, x2); currX <= Math.max(x2, x1); currX++){
            for(int currY = Math.min(y1, y2); currY <= Math.max(y2, y1); currY++){
                for(int currZ = Math.min(z1, z2); currZ <= Math.max(z2, z1); currZ++){
                    blockIDs[currIndex] = world.getBlockId(currX, currY, currZ);
                    blockDATAs[currIndex] = world.getBlockMetadata(currX, currY, currZ);
                    currIndex++;
                }
            }
        }
    }

    public WorldSection(Cuboid cuboid, World world) {
        this(cuboid.getXPos1(), cuboid.getYPos1(), cuboid.getZPos1(), cuboid.getXPos2(), cuboid.getYPos2(), cuboid.getZPos2(), world);
    }

    public void writeInto(World world){
        int currIndex = 0;
        for(int currX = Math.min(x1, x2); currX <= Math.max(x2, x1); currX++){
            for(int currY = Math.min(y1, y2); currY <= Math.max(y2, y1); currY++){
                for(int currZ = Math.min(z1, z2); currZ <= Math.max(z2, z1); currZ++){
                    world.setBlock(currX, currY, currZ, blockIDs[currIndex], blockDATAs[currIndex], ENotificationType.NOTIFY_CLIENTS.getType());
                    currIndex++;
                }
            }
        }
    }
}
