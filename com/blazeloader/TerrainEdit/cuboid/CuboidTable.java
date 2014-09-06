package com.blazeloader.TerrainEdit.cuboid;

import java.util.HashMap;
import java.util.Map;

/**
 * A table mapping Cuboids to their players.
 */
public class CuboidTable {
    private static Map<String, Cuboid> table = new HashMap<String, Cuboid>();

    /**
     * Gets the cuboid for a player, creating it if needed.
     *
     * @param player The player to create the cuboid for.
     * @return Return the cuboid for the player.
     */
    public static Cuboid getCuboidForPlayer(String player) {
        Cuboid cuboid = table.get(player);
        if (cuboid == null) {
            cuboid = new Cuboid(0, 0, 0, 0, 0, 0, false);
            table.put(player, cuboid);
        }
        return cuboid;
    }

    public static boolean doesPlayerHaveCuboid(String player) {
        return table.containsKey(player);
    }

    public static void setCuboidForPlayer(String player, Cuboid cuboid) {
        table.put(player, cuboid);
    }

    public static Map<String, Cuboid> getTable() {
        return table;
    }
}
