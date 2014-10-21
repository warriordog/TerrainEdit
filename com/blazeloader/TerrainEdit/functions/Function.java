package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.main.BlazeModTerrainEdit;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.api.api.chat.ApiChat;
import com.blazeloader.api.api.chat.ChatColor;
import net.minecraft.command.ICommandSender;

/**
 * Base class for /te functions
 * TODO add function protected boolean hasCuboid(ICommandSender) that sends warning to user
 * TODO put function names in final fields
 */
public abstract class Function {
    public static final int PERMISSION_NONE = 0;
    public static final int PERMISSION_PLAYER = 1;
    public static final int PERMISSION_COMMAND_BLOCK = 3;
    public static final int PERMISSION_OP = 4;
    public static final int PERMISSION_CONSOLE = 4;

    protected BlazeModTerrainEdit baseMod;
    protected CommandTE baseCommand;

    public Function(BlazeModTerrainEdit baseMod, CommandTE baseCommand) {
        this.baseMod = baseMod;
        this.baseCommand = baseCommand;
    }

    protected void register() {
        String[] names = this.getFunctionNames();
        if (names == null || names.length == 0) {
            throw new IllegalArgumentException("Invalid names!");
        }
        int nameNum = 0;
        for (String name : this.getFunctionNames()) {
            if (nameNum == 0) {
                baseCommand.registerUniqueFunction(name, this);
            } else {
                baseCommand.registerFunction(name, this);
            }
            nameNum++;
        }
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    public abstract String[] getFunctionNames();

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

    public String getFunctionUsage() {
        return getFunctionNames()[0];
    }

    /**
     * Sends chat to a command user.
     *
     * @param target  The user to send the chat to.
     * @param message The message to send.
     */
    protected void sendChat(ICommandSender target, String message) {
        ApiChat.sendChat(target, message);
    }

    /**
     * Sends chat to a command user, followed by a format_reset marker.
     *
     * @param target  The user to send the chat to.
     * @param message The message to send.
     */
    protected void sendChatLine(ICommandSender target, String message) {
        sendChat(target, message + ChatColor.FORMAT_RESET);
    }

    /**
     * Returns the name of the function.
     *
     * @return Return FunctionBase.getFunctionName();
     */
    @Override
    public String toString() {
        return this.getFunctionNames()[0];
    }
}
