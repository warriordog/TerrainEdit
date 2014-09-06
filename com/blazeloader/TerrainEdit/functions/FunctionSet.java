package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import com.blazeloader.TerrainEdit.cuboid.CuboidTable;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.main.ModTerrainEdit;
import com.blazeloader.TerrainEdit.undo.UndoList;
import com.blazeloader.api.api.block.ApiBlock;
import com.blazeloader.api.api.block.ENotificationType;
import com.blazeloader.api.api.chat.EChatColor;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;

/**
 * Sets all the blocks in the cuboid to a particular type.
 */
public class FunctionSet extends Function {
    public FunctionSet(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "set";
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
        if (args.length < 2) {
            sendChatLine(user, EChatColor.COLOR_RED + "Not enough arguments!  Use /te set <block> [metadata]");
        } else {
            Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
            if (cuboid.getIsSet()) {
                try {
                    Block block = ApiBlock.getBlockByNameOrId(args[1]);
                    int meta = 0;
                    if (args.length >= 3) {
                        meta = Integer.parseInt(args[2]);
                    }
                    UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                    for (int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++) {
                        for (int y = Math.min(cuboid.getYPos1(), cuboid.getYPos2()); y <= Math.max(cuboid.getYPos1(), cuboid.getYPos2()); y++) {
                            for (int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++) {
                                ApiBlock.setBlockAt(user.getEntityWorld(), x, y, z, block, meta, ENotificationType.NOTIFY_CLIENTS.getType());
                            }
                        }
                    }
                    sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
                } catch (NumberFormatException e) {
                    sendChatLine(user, EChatColor.COLOR_RED + "Invalid arguments!  Use /te set <block> [metadata]");
                } catch (Exception e) {
                    sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An error occurred while setting blocks!");
                    e.printStackTrace();
                }
            } else {
                sendChatLine(user, EChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
            }
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Sets the blocks in the cuboid.";
    }
}
