package com.blazeloader.TerrainEdit.main;

import com.blazeloader.api.core.base.mod.BLMod;
import net.acomputerdog.core.logger.CLogger;

import java.io.File;

/**
 * Base mod class for TerrainEdit.  Registers CommandTE.
 */
public class ModTerrainEdit implements BLMod {
    private final int version = 0;

    public CommandTE command;
    public final CLogger logger = new CLogger(this.getName(), true, true);
    public static ModTerrainEdit instance;

    /**
     * Returns ID used to identify this mod internally, even among different versions of the same mod.  Mods should override.
     * --This should never be changed after the mod has been released!--
     *
     * @return Returns the id of the mod.
     */
    @Override
    public String getModId() {
        return "terrainedit";
    }

    @Override
    public String getName() {
        return "TerrainEdit";
    }

    /**
     * Compares two mods of the same type to determine which is newer.  Mods should override this and implement their own comparison logic.
     *
     * @param otherMod The mod to compare
     * @return Return the newer mod
     */
    @Override
    public BLMod getNewerVersion(BLMod otherMod) {
        if (otherMod instanceof ModTerrainEdit) {
            ModTerrainEdit other = (ModTerrainEdit) otherMod;
            return (this.version > other.version) ? this : other;
        }
        return this;
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
     * Returns true if this mod is compatible with the installed version of BlazeLoader.  This should be checked using Version.class.
     * -Called before mod is loaded!  Do not depend on Mod.load()!-
     *
     * @return Returns true if the mod is compatible with the installed version of BlazeLoader.
     */
    @Override
    public boolean isCompatibleWithEnvironment() {
        return true; //todo check MC version
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
}
