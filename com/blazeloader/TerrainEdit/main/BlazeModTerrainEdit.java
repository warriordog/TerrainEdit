package com.blazeloader.TerrainEdit.main;

import com.blazeloader.api.api.command.ApiCommand;
import com.blazeloader.api.event.ModEventHandler;
import com.blazeloader.api.mod.BLMod;
import com.blazeloader.api.version.BuildType;
import com.blazeloader.api.version.type.ModVersion;
import net.acomputerdog.core.logger.CLogger;

import java.io.File;

/**
 * Base mod class for TerrainEdit.  Registers CommandTE.
 */
public class BlazeModTerrainEdit implements BLMod, ModEventHandler {
    private final ModVersion version = new ModVersion(BuildType.DEVELOPMENT, this, 0, 0);

    public CommandTE command;
    public final CLogger logger = new CLogger(this.getName(), false, true);
    public static BlazeModTerrainEdit instance;

    /**
     * Returns ID used to identify this mod internally, even among different versions of the same mod.  Mods should override.
     * --This should never be changed after the mod has been released!--
     *
     * @return Returns the id of the mod.
     */
    @Override
    public String getModId() {
        return "TerrainEdit";
    }

    @Override
    public String getName() {
        return "TerrainEdit";
    }

    @Override
    public String getVersion() {
        return "0.0.0";
    }

    /**
     * Do startup stuff here, minecraft is not fully initialised when this function is called so mods *must not*
     * interact with minecraft in any way here
     *
     * @param configPath Configuration path to use
     */
    @Override
    public void init(File configPath) {
        instance = this;
        logger.logInfo("Initialized.");
    }

    /**
     * Called when the loader detects that a version change has happened since this mod was last loaded
     *
     * @param version       new version
     * @param configPath    Path for the new version-specific config
     * @param oldConfigPath Path for the old version-specific config
     */
    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {

    }

    /**
     * Gets a user-friendly description of the mod.
     *
     * @return Return a String representing a user-friendly version of the mod.
     */
    @Override
    public String getModDescription() {
        return "A command-line terrain editor.";
    }

    @Override
    public ModVersion getModVersion() {
        return version;
    }

    @Override
    public void start() {
        command = new CommandTE(this);
        ApiCommand.registerCommand(command);
        logger.logInfo("Started.");
    }

    @Override
    public void stop() {
        logger.logInfo("Stopped.");
    }
}
