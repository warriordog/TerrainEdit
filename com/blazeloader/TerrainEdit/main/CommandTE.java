package com.blazeloader.TerrainEdit.main;

import com.blazeloader.TerrainEdit.functions.*;
import com.blazeloader.api.api.chat.ChatColor;
import com.blazeloader.api.api.command.BLCommandBase;
import net.minecraft.command.ICommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * The base command for TerrainEdit.  Loads functions as sub-commands.
 */
public class CommandTE extends BLCommandBase {
    protected final BlazeModTerrainEdit baseMod;
    private final Map<String, Function> uniqueFunctions = new HashMap<String, Function>();
    private final Map<String, Function> functionList = new HashMap<String, Function>();
    public boolean functionListChanged = false;

    public CommandTE(BlazeModTerrainEdit baseMod) {
        super();
        this.baseMod = baseMod;
        new FunctionHelp(baseMod, this);
        new FunctionCuboid(baseMod, this, FunctionCuboid.CORNER_1);
        new FunctionCuboid(baseMod, this, FunctionCuboid.CORNER_2);
        new FunctionSet(baseMod, this);
        new FunctionDelete(baseMod, this);
        new FunctionGenRan(baseMod, this);
        new FunctionReplace(baseMod, this);
        new FunctionSchemLoad(baseMod, this);
        new FunctionUndo(baseMod, this);
        new FunctionLayer(baseMod, this);
        //new FunctionShift(baseMod, this);  //Temporarily disabled
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public String getCommandName() {
        return "te";
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/te [function [args]]";
    }

    @Override
    public void processCommand(ICommandSender user, String[] args) {
        try {
            if (args.length == 0) {
                sendChatLine(user, ChatColor.COLOR_RED + "Please specify a function using /te [function [args]].  Use /te help for more info.");
            } else {
                Function function = functionList.get(args[0]);
                if (function != null) {
                    if (function.getNumRequiredArgs() <= args.length - 1) {//subtract 1 because first index is "te"
                        if (user.canCommandSenderUseCommand(function.getRequiredPermissionLevel(), "te " + function.getFunctionNames()[0])) {
                            try {
                                function.execute(user, args);
                            } catch (Exception e) {
                                sendChatLine(user, ChatColor.COLOR_RED + "" + ChatColor.FORMAT_UNDERLINE + "An error occurred while executing the command!  Please tell a server administrator!");
                                BlazeModTerrainEdit.instance.logger.logError("Exception executing function " + function.getFunctionNames()[0] + "!", e);
                            }
                        } else {
                            sendChat(user, ChatColor.COLOR_RED + "You do not have permission to execute this command!");
                        }
                    } else {
                        sendChat(user, ChatColor.COLOR_RED + "Not enough args!  Use \"/te " + function.getFunctionUsage() + "\"!");
                    }
                } else {
                    sendChat(user, ChatColor.COLOR_RED + "Unknown function!  Use \"/te help\" for a list.");
                }
            }

        } catch (Exception e) {
            sendChatLine(user, ChatColor.COLOR_RED.combine(ChatColor.FORMAT_UNDERLINE) + ChatColor.FORMAT_BOLD + "An unknown error occurred!  Please tell a server administrator!");
            BlazeModTerrainEdit.instance.logger.logError("Unknown exception occurred!", e);
        }
    }

    public void registerFunction(String name, Function function) {
        if (name == null || function == null) {
            throw new IllegalArgumentException("Invalid function!");
        }
        if (functionList.put(name, function) != null) {
            baseMod.logger.logWarning("Registering duplicate function: " + name);
        }
    }

    public void registerUniqueFunction(String name, Function function) {
        if (name == null || function == null) {
            throw new IllegalArgumentException("Invalid function!");
        }
        functionListChanged = true;
        registerFunction(name, function);
        if (uniqueFunctions.put(name, function) != null) {
            baseMod.logger.logWarning("Registering duplicate function: " + name);
        }
    }

    public Map<String, Function> getFunctionList() {
        return functionList;
    }

    public Map<String, Function> getUniqueFunctions() {
        return uniqueFunctions;
    }
}
