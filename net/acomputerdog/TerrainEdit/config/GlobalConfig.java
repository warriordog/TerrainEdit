package net.acomputerdog.TerrainEdit.config;

import net.acomputerdog.BlazeLoader.mod.Mod;
import net.acomputerdog.BlazeLoader.util.config.ModConfig;

public class GlobalConfig extends ModConfig {

    public GlobalConfig(Mod owner) {
        super(owner, "/TE/config.cfg");
        super.loadConfig();
    }
}
