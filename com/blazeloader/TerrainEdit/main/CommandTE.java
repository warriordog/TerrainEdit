package com.blazeloader.TerrainEdit.main;

import com.blazeloader.TerrainEdit.functions.*;
import com.blazeloader.api.direct.base.api.chat.EChatColor;
import com.blazeloader.api.direct.server.api.command.BLCommandBase;
import net.minecraft.command.ICommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * The base command for TerrainEdit.  Loads functions as sub-commands.
 */
public class CommandTE extends BLCommandBase {
    protected ModTerrainEdit baseMod;
    public Map<String, Function> functionList = new HashMap<String, Function>();

    public CommandTE(ModTerrainEdit baseMod) {
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
                sendChatLine(user, EChatColor.COLOR_RED + "Please specify a function using /te [function [args]].  Use /te help for more info.");
            } else {
                Function function = functionList.get(args[0]);
                if (function != null) {
                    if (function.getNumRequiredArgs() > args.length) {//must be greater because first index is function name!
                        if (user.canCommandSenderUseCommand(function.getRequiredPermissionLevel(), "te " + function.getFunctionName())) {
                            try {
                                function.execute(user, args);
                            } catch (Exception e) {
                                sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "An error occurred while executing the command!  Please tell a server administrator!");
                                ModTerrainEdit.instance.logger.logError("Exception executing function " + function.getFunctionName() + "!", e);
                            }
                        } else {
                            sendChat(user, EChatColor.COLOR_RED + "You do not have permission to execute this command!");
                        }
                    } else {
                        sendChat(user, EChatColor.COLOR_RED + "Not enough args!  Use \"/te " + function.getFunctionUsage() + "\"!");
                    }
                } else {
                    sendChat(user, EChatColor.COLOR_RED + "Unknown function!  Use \"/te help\" for a list.");
                }
            }

        } catch (Exception e) {
            sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An unknown error occurred!  Please tell a server administrator!");
            ModTerrainEdit.instance.logger.logError("Unknown exception occurred!", e);
        }
    }
}
