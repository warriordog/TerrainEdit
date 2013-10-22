package net.acomputerdog.TerrainEdit.config;

import net.acomputerdog.BlazeLoader.mod.Mod;
import net.acomputerdog.BlazeLoader.util.config.ModConfig;

/**
 * Contains player-specific configuration settings.
 */
public class PlayerConfig extends ModConfig{
    public boolean commandConfirmation;

    /**
     * Creates a new config file using owner.getModId() as the filename.
     *
     * @param owner The mod that created this config.
     */
    public PlayerConfig(Mod owner, String playerName) {
        super(owner, "/TE/players/" + playerName + ".cfg");
        commandConfirmation = true;
        loadConfig();
    }
}
