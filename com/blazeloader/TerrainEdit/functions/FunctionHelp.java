package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.main.ModTerrainEdit;
import com.blazeloader.api.direct.base.api.chat.EChatColor;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * A function to return a list of all TE functions and their descriptions.
 */
public class FunctionHelp extends Function {

    public FunctionHelp(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "help";
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
        int numPages = Math.max((baseCommand.functionList.size() / 6) + 1, 1);
        int currPage = 1;
        try {
            if (args.length >= 2) {
                currPage = Integer.parseInt(args[1]);
            }
            if (currPage > numPages) {
                currPage = numPages;
            }
            sendChatLine(user, EChatColor.COLOR_DARK_GREEN + "" + EChatColor.FORMAT_UNDERLINE + "Available TE functions (Page " + +currPage + " of " + numPages + "):");
            sendChatLine(user, "");
            Object[] functions = baseCommand.functionList.values().toArray();
            //todo: rewrite, completely.  everything is wrong with this :)
            Collections.sort(Arrays.asList(functions), new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ((Function) o1).getFunctionName().compareTo(((Function) o2).getFunctionName());
                }
            });
            for (int index = 6 * (currPage - 1); index < 6 * currPage && index < baseCommand.functionList.size(); index++) {
                Function function = (Function) functions[index];
                sendChatLine(user, EChatColor.COLOR_YELLOW + function.getFunctionName() + EChatColor.COLOR_ORANGE + " - " + function.getFunctionDescription());
            }
        } catch (NumberFormatException e) {
            sendChatLine(user, EChatColor.COLOR_RED + "Invalid page number!  Must be an integer!");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Gets a list of TE functions";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return PERMISSION_NONE;
    }

    @Override
    public int getNumRequiredArgs() {
        return 0;
    }

    @Override
    public String getFunctionUsage() {
        return getFunctionName() + " [page]";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
