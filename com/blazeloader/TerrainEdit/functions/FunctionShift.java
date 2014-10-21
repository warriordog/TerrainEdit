package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import com.blazeloader.TerrainEdit.cuboid.CuboidTable;
import com.blazeloader.TerrainEdit.main.BlazeModTerrainEdit;
import com.blazeloader.TerrainEdit.main.BlockAccess;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.undo.UndoList;
import com.blazeloader.api.api.block.NotificationType;
import com.blazeloader.api.api.chat.ChatColor;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * A function that shifts blocks up or down by an amount.
 */
public class FunctionShift extends Function {
    public FunctionShift(BlazeModTerrainEdit baseMod, CommandTE baseCommand) {
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
        return new String[]{"shift", "move", "mv"};
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
                int distance = Integer.parseInt(args[1]);
                boolean allowShiftOutOfCuboid = false;
                if (args.length >= 3) {
                    List<String> switches = Arrays.asList(args[2].split("-"));
                    if (switches.contains("allowOutOfCuboid")) {
                        allowShiftOutOfCuboid = true;
                    }
                }
                UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                World world = user.getEntityWorld();
                boolean dir = distance >= 0;
                int y1 = dir ? cuboid.getMaxY() : cuboid.getMinY();
                int y2 = dir ? cuboid.getMinY() : cuboid.getMaxY();
                for (int x = cuboid.getMinX(); x <= cuboid.getMaxX(); x++) {
                    for (int z = cuboid.getMinZ(); z <= cuboid.getMaxZ(); z++) {
                        for (int y = y2; dir ? y >= y1 : y <= y1; y = dir ? y - 1 : y + 1) {
                            if (allowShiftOutOfCuboid || (y + distance >= y2 && y + distance <= y1)) {
                                if (dir) {
                                    BlockAccess.setBlockAt(world, x, y + distance, z, BlockAccess.getBlockTypeAt(world, x, y, z), BlockAccess.getBlockDataAt(world, x, y, z), NotificationType.NOTIFY_CLIENTS);
                                    BlockAccess.setBlockAt(world, x, y, z, Blocks.air, 0, NotificationType.NOTIFY_CLIENTS);
                                } else {
                                    BlockAccess.setBlockAt(world, x, y - distance, z, BlockAccess.getBlockTypeAt(world, x, y, z), BlockAccess.getBlockDataAt(world, x, y, z), NotificationType.NOTIFY_CLIENTS);
                                    BlockAccess.setBlockAt(world, x, y, z, Blocks.air, 0, NotificationType.NOTIFY_CLIENTS);
                                }
                            }
                        }
                    }
                }
                sendChatLine(user, ChatColor.COLOR_YELLOW + "Done.");
            } catch (NumberFormatException e) {
                sendChatLine(user, ChatColor.COLOR_RED + "Invalid arguments!  Use \"/te " + getFunctionUsage() + "\".");
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
        return "Shifts blocks up or down by an amount.";
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
        return getFunctionNames()[0] + " <distance> [switches]";
    }

}
