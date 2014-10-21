package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import com.blazeloader.TerrainEdit.cuboid.CuboidTable;
import com.blazeloader.TerrainEdit.main.BlazeModTerrainEdit;
import com.blazeloader.TerrainEdit.main.BlockAccess;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.undo.UndoList;
import com.blazeloader.api.api.block.ApiBlock;
import com.blazeloader.api.api.block.NotificationType;
import com.blazeloader.api.api.chat.ChatColor;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

/**
 * Function that replaces all blocks in the cuboid of a specific type with another type.
 */
public class FunctionReplace extends Function {
    public FunctionReplace(BlazeModTerrainEdit baseMod, CommandTE baseCommand) {
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
        return new String[]{"replace", "switch", "swap"};
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
            Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getName());
            if (cuboid.isSet()) {
                Block block1 = ApiBlock.getBlockByNameOrId(args[1]);
                Block block2 = ApiBlock.getBlockByNameOrId(args[2]);
                boolean useMeta1 = false;
                int meta1 = 0;
                if (args.length >= 4) {
                    meta1 = Integer.parseInt(args[3]);
                    useMeta1 = true;
                }
                int meta2 = 0;
                if (args.length >= 5) {
                    meta2 = Integer.parseInt(args[4]);
                }
                World world = user.getEntityWorld();
                UndoList.createUndoTask(world, cuboid);
                for (int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++) {
                    for (int y = Math.min(cuboid.getYPos1(), cuboid.getYPos2()); y <= Math.max(cuboid.getYPos1(), cuboid.getYPos2()); y++) {
                        for (int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++) {
                            if (BlockAccess.getBlockTypeAt(world, x, y, z) == block1) {
                                if (!useMeta1 || BlockAccess.getBlockDataAt(world, x, y, z) == meta1) {
                                    BlockAccess.setBlockAt(world, x, y, z, block2, meta2, NotificationType.NOTIFY_CLIENTS);
                                }
                            }
                        }
                    }
                }
                sendChatLine(user, ChatColor.COLOR_YELLOW + "Done.");
            } else {
                sendChatLine(user, ChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
            }
        } catch (NumberFormatException e) {
            sendChatLine(user, ChatColor.COLOR_RED + "Invalid arguments!  Use \"/te " + getFunctionUsage() + "\".");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Replaces one type of block with another.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return PERMISSION_OP;
    }

    @Override
    public int getNumRequiredArgs() {
        return 2;
    }

    @Override
    public String getFunctionUsage() {
        return getFunctionNames()[0] + " <block1> <block2> [block1_meta] [block2_meta]";
    }
}
