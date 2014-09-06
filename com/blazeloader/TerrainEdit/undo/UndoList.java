package com.blazeloader.TerrainEdit.undo;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of tasks to perform when undoing actions.
 */
public class UndoList {
    private static List<WorldSection> tasks = new ArrayList<WorldSection>();
    private static int maxEntries = 5;

    public static void addTask(WorldSection task) {
        tasks.add(task);
        if (tasks.size() > maxEntries) {
            tasks.remove(0);
        }
    }

    public static boolean hasTask() {
        return tasks.size() > 0;
    }

    public static void undoLastTask(World world) {
        if (hasTask()) {
            tasks.remove(tasks.size() - 1).writeInto(world);
        }
    }

    public static int getMaxEntries() {
        return maxEntries;
    }

    public static void setMaxEntries(int maxEntries) {
        UndoList.maxEntries = maxEntries;
    }

    public static void createUndoTask(World world, Cuboid cuboid) {
        addTask(new WorldSection(cuboid, world));
    }

    public static void createUndoTask(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        addTask(new WorldSection(x1, y1, z1, x2, y2, z2, world));
    }
}
