package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.main.BlazeModTerrainEdit;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.schematic.Schematic;
import com.blazeloader.api.api.chat.ChatColor;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.io.File;

/**
 * Loads a schematic and imports it into the world.  Currently uses very old and buggy schematic loading code that will be replaced soon.
 * Cannot currently be undone!
 */
public class FunctionSchemLoad extends Function {
    public FunctionSchemLoad(BlazeModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
        register();
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String[] getFunctionNames() {
        return new String[]{"schemload"};
    }

    /**
     * Executes the command.
     *
     * @param user The user executing the command.
     * @param args The arguments passed to the module.
     *             -WARNING: args[0] is always the name of the sub-command!  Skip it!-
     */
    @Override
    public void execute(ICommandSender user, String[] args) {
        try {
            File schematic = new File(args[1]);
            if (schematic.exists() && schematic.isFile()) {
                if (args.length == 2) {
                    BlockPos loc = user.getPosition();
                    try {
                        new Schematic(schematic).place(user.getEntityWorld(), loc.getX(), loc.getY(), loc.getZ());
                        sendChatLine(user, ChatColor.COLOR_YELLOW + "Placed schematic.");
                    } catch (Exception e) {
                        sendChatLine(user, ChatColor.COLOR_RED + "Unable to place schematic!");
                        e.printStackTrace();
                    }
                } else if (args.length >= 5) {
                    try {
                        new Schematic(schematic).place(user.getEntityWorld(), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[2]));
                        sendChatLine(user, ChatColor.COLOR_YELLOW + "Placed schematic.");
                    } catch (Exception e) {
                        sendChatLine(user, ChatColor.COLOR_RED + "Unable to place schematic!");
                        e.printStackTrace();
                    }
                } else {
                    sendChatLine(user, ChatColor.COLOR_RED + "Illegal args!  Use \"/te " + getFunctionUsage() + "\".");
                }
            } else {
                sendChatLine(user, ChatColor.COLOR_RED + "The specified schematic was not found!");
            }
        } catch (NumberFormatException e) {
            sendChatLine(user, ChatColor.COLOR_RED + "Illegal args!  x, y, and z must be integers!");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Imports a schematic into the world.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return PERMISSION_OP;
    }

    @Override
    public int getNumRequiredArgs() {
        return 1;
    }

    @Override
    public String getFunctionUsage() {
        return getFunctionNames()[0] + " <path_to_schematic> [x] [y] [z]";
    }
}
