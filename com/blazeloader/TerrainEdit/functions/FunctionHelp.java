package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.main.BlazeModTerrainEdit;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.api.api.chat.ChatColor;
import net.minecraft.command.ICommandSender;

import java.util.*;

/**
 * A function to return a list of all TE functions and their descriptions.
 */
public class FunctionHelp extends Function {

    private List<String> sortedNames = new ArrayList<String>();

    public FunctionHelp(BlazeModTerrainEdit baseMod, CommandTE baseCommand) {
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
        return new String[]{"help", "hlp", "?"};
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
        sortFuncs();

        int numPages = (int) Math.max(Math.ceil((float) sortedNames.size() / 8f), 1);
        int currPage = 1;
        try {
            if (args.length >= 2) {
                currPage = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException e) {
            sendChatLine(user, ChatColor.COLOR_RED + "Invalid page number!  Must be an integer!");
            return;
        }
        if (currPage > numPages) {
            currPage = numPages;
        }
        if (currPage < 1) {
            currPage = 1;
        }
        sendChatLine(user, ChatColor.COLOR_DARK_GREEN + "" + ChatColor.FORMAT_UNDERLINE + "Available TE functions (Page " + currPage + " of " + numPages + "):");
        sendChatLine(user, "");

        Map<String, Function> functions = baseCommand.getUniqueFunctions();
        for (int index = 8 * (currPage - 1); index < 8 * currPage && index < sortedNames.size() && index >= 0; index++) {
            String name = sortedNames.get(index);
            sendChatLine(user, ChatColor.COLOR_YELLOW + name + ChatColor.COLOR_ORANGE + " - " + functions.get(name).getFunctionDescription());
        }
    }

    private void sortFuncs() {
        if (baseCommand.functionListChanged) {
            Map<String, Function> functions = baseCommand.getUniqueFunctions();
            sortedNames.clear();
            for (String name : functions.keySet()) {
                sortedNames.add(name);
            }
            Collections.sort(sortedNames, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            baseCommand.functionListChanged = false;
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
        return getFunctionNames()[0] + " [page]";
    }

}
