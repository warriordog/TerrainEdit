package net.acomputerdog.TerrainEdit.config;

import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains configuration settings for TerrainEdit.
 */
public class Config{

    private static Map<String, PlayerConfig> playerConfigs = new HashMap<String, PlayerConfig>();
    public static final GlobalConfig globalConfig = new GlobalConfig(ModTerrainEdit.instance);

    public static PlayerConfig getConfigForPlayer(String player){
        PlayerConfig config = playerConfigs.get(player);
        if(config == null){
            playerConfigs.put(player, config = new PlayerConfig(ModTerrainEdit.instance, player));
        }
        return config;
    }
}
