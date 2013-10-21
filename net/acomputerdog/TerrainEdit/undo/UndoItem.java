package net.acomputerdog.TerrainEdit.undo;

import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.minecraft.src.World;

/**
 * An undo item.  Stores a list of block information to perform undo operations.
 */
public class UndoItem {
    private int x1, y1, z1, x2, y2, z2;
    private int[] blockIDs;
    private int[] blockDATAs;

    public UndoItem(int x1, int y1, int z1, int x2, int y2, int z2, World world) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        blockIDs = new int[Math.abs(x2 - x1) * Math.abs(y2 - y1) * Math.abs(z2 - z1)];
        blockDATAs = new int[Math.abs(x2 - x1) * Math.abs(y2 - y1) * Math.abs(z2 - z1)];
        int currIndex = 0;
        for(int currX = x1; currX <= x2; currX++){
            for(int currY = y1; currY <= y2; currY++){
                for(int currZ = z1; currZ <= z2; currZ++){
                    blockIDs[currIndex] = world.getBlockId(currX, currY, currZ);
                    blockDATAs[currIndex] = world.getBlockMetadata(currX, currY, currZ);
                    currIndex++;
                }
            }
        }
    }

    public UndoItem(Cuboid cuboid, World world) {
        this.x1 = cuboid.getXPos1();
        this.y1 = cuboid.getYPos1();
        this.z1 = cuboid.getZPos1();
        this.x2 = cuboid.getXPos2();
        this.y2 = cuboid.getYPos2();
        this.z2 = cuboid.getZPos2();
        blockIDs = new int[Math.abs(x2 - x1) * Math.abs(y2 - y1) * Math.abs(z2 - z1)];
        blockDATAs = new int[Math.abs(x2 - x1) * Math.abs(y2 - y1) * Math.abs(z2 - z1)];
        int currIndex = 0;
        for(int currX = x1; currX <= x2; currX++){
            for(int currY = y1; currY <= y2; currY++){
                for(int currZ = z1; currZ <= z2; currZ++){
                    blockIDs[currIndex] = world.getBlockId(currX, currY, currZ);
                    blockDATAs[currIndex] = world.getBlockMetadata(currX, currY, currZ);
                    currIndex++;
                }
            }
        }
    }

    public void undo(World world){
        int currIndex = 0;
        for(int currX = x1; currX <= x2; currX++){
            for(int currY = y1; currY <= y2; currY++){
                for(int currZ = z1; currZ <= z2; currZ++){
                    world.setBlock(currX, currY, currZ, blockIDs[currIndex], blockDATAs[currIndex], ENotificationType.NOTIFY_CLIENTS.getType());
                    currIndex++;
                }
            }
        }
    }
}
