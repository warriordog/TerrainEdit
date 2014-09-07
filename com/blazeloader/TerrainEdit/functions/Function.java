package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.main.ModTerrainEdit;
import com.blazeloader.api.direct.base.api.chat.EChatColor;
import com.blazeloader.api.direct.server.api.chat.ApiChatServer;
import net.minecraft.command.ICommandSender;

/**
 * Base class for /te functions
 */
public abstract class Function {
    public static final int PERMISSION_NONE = 0;
    public static final int PERMISSION_PLAYER = 1;
    public static final int PERMISSION_COMMAND_BLOCK = 3;
    public static final int PERMISSION_OP = 4;
    public static final int PERMISSION_CONSOLE = 4;

    protected ModTerrainEdit baseMod;
    protected CommandTE baseCommand;

    public Function(ModTerrainEdit baseMod, CommandTE baseCommand) {
        this.baseMod = baseMod;
        this.baseCommand = baseCommand;
        baseCommand.functionList.put(this.getFunctionName(), this);
        for (String str : getAliases()) {
            baseCommand.functionList.put(str, this);
        }
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    public abstract String getFunctionName();

    /**
     * Executes the command.
     *
     * @param user The user executing the command.
     * @param args The arguments passed to the module.
     *             -WARNING: args[0] is always the name of the sub-command!  Skip it!-
     */
    public abstract void execute(ICommandSender user, String[] args);

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    public abstract String getFunctionDescription();

    public abstract int getRequiredPermissionLevel();

    public abstract int getNumRequiredArgs();

    public abstract String getFunctionUsage();

    public abstract String[] getAliases();

    /**
     * Sends chat to a command user.
     *
     * @param target  The user to send the chat to.
     * @param message The message to send.
     */
    protected void sendChat(ICommandSender target, String message) {
        ApiChatServer.sendChat(target, message);
    }

    /**
     * Sends chat to a command user, followed by a format_reset marker.
     *
     * @param target  The user to send the chat to.
     * @param message The message to send.
     */
    protected void sendChatLine(ICommandSender target, String message) {
        sendChat(target, message + EChatColor.FORMAT_RESET);
    }

    /**
     * Returns the name of the function.
     *
     * @return Return FunctionBase.getFunctionName();
     */
    @Override
    public String toString() {
        return this.getFunctionName();
    }
}
