package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.main.BlazeModTerrainEdit;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.undo.UndoList;
import com.blazeloader.api.api.chat.ChatColor;
import net.minecraft.command.ICommandSender;

/**
 * Allows the user to undo TE changes.  Not currently user-specific!
 */
public class FunctionUndo extends Function {
    public FunctionUndo(BlazeModTerrainEdit baseMod, CommandTE baseCommand) {
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
        return new String[]{"undo"};
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
        if (UndoList.hasTask()) {
            UndoList.undoLastTask(user.getEntityWorld());
            sendChatLine(user, ChatColor.COLOR_YELLOW + "Done.");
        } else {
            sendChatLine(user, ChatColor.COLOR_RED + "There is nothing to undo!");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Undoes a previous TE action.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return PERMISSION_OP;
    }

    @Override
    public int getNumRequiredArgs() {
        return 0;
    }

    @Override
    public String getFunctionUsage() {
        return getFunctionNames()[0];
    }
}
