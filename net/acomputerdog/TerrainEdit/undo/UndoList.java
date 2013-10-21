package net.acomputerdog.TerrainEdit.undo;

import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.minecraft.src.World;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of tasks to perform when undoing actions.
 */
public class UndoList {
    private static List<UndoTask> tasks = new ArrayList<UndoTask>();
    private static int maxEntries = 5;

    public static void addTask(UndoTask task){
        tasks.add(task);
        if(tasks.size() > maxEntries){
            tasks.remove(0);
        }
    }

    public static boolean hasTask(){
        return tasks.size() > 0;
    }

    public static void undoLastTask(World world){
        if(hasTask()){
            tasks.remove(tasks.size() - 1).undo(world);
        }
    }

    public static int getMaxEntries(){
        return maxEntries;
    }

    public static void setMaxEntries(int maxEntries){
        UndoList.maxEntries = maxEntries;
    }

    public static void createUndoTask(World world, Cuboid cuboid){
        addTask(new UndoTask(cuboid, world));
    }
}
