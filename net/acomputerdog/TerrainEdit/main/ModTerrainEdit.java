package net.acomputerdog.TerrainEdit.main;

import net.acomputerdog.BlazeLoader.api.command.ApiCommand;
import net.acomputerdog.BlazeLoader.main.Version;
import net.acomputerdog.BlazeLoader.mod.Mod;
import net.acomputerdog.core.logger.CLogger;

/**
 * Base mod class for TerrainEdit.  Registers CommandTE.
 */
public class ModTerrainEdit extends Mod {
    public CommandTE command;
    public CLogger logger = new CLogger(this.getModName(), true, true);
    public static ModTerrainEdit instance;

    /**
     * Returns ID used to identify this mod internally, even among different versions of the same mod.  Mods should override.
     * --This should never be changed after the mod has been released!--
     *
     * @return Returns the id of the mod.
    */
    @Override
    public String getModId() {
        return "acomputerdog.terrainedit";
    }

    /**
     * Returns the user-friendly name of the mod.  Mods should override.
     * --Can be changed among versions, so this should not be used to ID mods!--
     *
     * @return Returns user-friendly name of the mod.
    */
    @Override
    public String getModName() {
        return "Terrain Edit";
    }

    /**
     * Gets the version of the mod as an integer for choosing the newer version.
     *
     * @return Return the version of the mod as an integer.
    */
    @Override
    public int getIntModVersion() {
        return 0;
    }

    /**
     * Gets the version of the mod as a String for display.
     *
     * @return Returns the version of the mod as an integer.
    */
    @Override
    public String getStringModVersion() {
        return "0.0";
    }

    /**
     * Returns true if this mod is compatible with the installed version of BlazeLoader.  This should be checked using Version.class.
     * -Called before mod is loaded!  Do not depend on Mod.load()!-
     *
     * @return Returns true if the mod is compatible with the installed version of BlazeLoader.
    */
    @Override
    public boolean isCompatibleWithBLVersion() {
        if(!Version.getMinecraftVersion().equals("1.7.2")){
            System.out.println("TerrainEdit - Incorrect Minecraft version, aborting launch!");
            return false;
        } else if (Version.getGlobalVersion() == 3 && Version.getApiVersion() > 1) {
            System.out.println("TerrainEdit - Unknown BL version, bad things may happen!");
            return true;
        } else if (Version.getGlobalVersion() != 3 || Version.getApiVersion() <= -1) {
            System.out.println("TerrainEdit - Incompatible BL version, aborting launch!");
            return false;
        }else{
            System.out.println("TerrainEdit - Valid BL and MC versions.");
            return true;
        }
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

    /**
     * Called when mod is started.  Game is fully loaded and can be interacted with.
     */
    @Override
    public void start() {
        ApiCommand.registerCommand(command = new CommandTE(this));
        logger.logInfo("Loaded.");
    }

    /**
     * Called when mod is loaded.  Called before game is loaded.
     */
    @Override
    public void load() {
        instance = this;
    }
}
