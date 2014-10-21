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
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * A function that adds a layer of blocks to the cuboid.
 */
public class FunctionLayer extends Function {
    public FunctionLayer(BlazeModTerrainEdit baseMod, CommandTE baseCommand) {
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
        return new String[]{"layer"};
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
        Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getName());
        if (cuboid.isSet()) {
            try {
                Block block = ApiBlock.getBlockByNameOrId(args[1]);
                int meta = 0;
                boolean onlyOnExistingBlock = false;
                if (args.length >= 3) {
                    meta = Integer.parseInt(args[2]);
                    if (args.length >= 4) {
                        List<String> switches = Arrays.asList(args[3].split("-"));
                        if (switches.contains("b")) {
                            onlyOnExistingBlock = true;
                        }
                    }
                }
                UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                World world = user.getEntityWorld();
                int maxY = Math.max(cuboid.getYPos1(), cuboid.getYPos2());
                int minY = Math.min(cuboid.getYPos1(), cuboid.getYPos2());
                for (int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++) {
                    for (int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++) {
                        int y = getHighestBlock(world, x, z, maxY, minY - 1) + 1;
                        if (y <= maxY && y >= minY) {
                            if (!onlyOnExistingBlock) {
                                BlockAccess.setBlockAt(world, x, y, z, block, meta, NotificationType.NOTIFY_CLIENTS);
                            } else if (BlockAccess.getBlockTypeAt(world, x, y - 1, z) != Blocks.air) {
                                BlockAccess.setBlockAt(world, x, y, z, block, meta, NotificationType.NOTIFY_CLIENTS);
                            }
                        }
                    }
                }
                sendChatLine(user, ChatColor.COLOR_YELLOW + "Done.");
            } catch (NumberFormatException e) {
                sendChatLine(user, ChatColor.COLOR_RED + "Invalid arguments!  Use Use /te layer <block> [metadata] [switches]");
            }
        } else {
            sendChatLine(user, ChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Adds a layer of blocks to the cuboid.";
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
        return getFunctionNames()[0] + " <block> [metadata] [switches]";
    }

    public int getHighestBlock(World world, int x, int z, int maxY, int minY) {
        if (maxY < minY) {
            throw new IllegalArgumentException("maxY must be less than minY!");
        } else {
            for (int y = maxY; y >= minY; y--) {
                if (BlockAccess.getBlockTypeAt(world, x, y, z) != Blocks.air) {
                    return y;
                }
            }
            return minY;
        }
    }
}
