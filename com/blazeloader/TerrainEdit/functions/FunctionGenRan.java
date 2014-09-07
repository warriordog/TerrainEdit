package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import com.blazeloader.TerrainEdit.cuboid.CuboidTable;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.main.ModTerrainEdit;
import com.blazeloader.TerrainEdit.undo.UndoList;
import com.blazeloader.api.direct.base.api.chat.EChatColor;
import com.blazeloader.api.direct.server.api.block.ApiBlockServer;
import com.blazeloader.api.direct.server.api.block.ENotificationType;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;

import java.util.Random;

/**
 * Function that generates terrain based on random numbers and a percent chance.  Uses the seed "TerrainEdit".
 */
public class FunctionGenRan extends Function {
    private Random random = new Random("TerrainEdit".hashCode());

    public FunctionGenRan(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "genran";
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
            Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
            if (cuboid.isSet()) {
                int chance = Integer.parseInt(args[1]);
                Block block = ApiBlockServer.getBlockByNameOrId(args[2]);
                int meta = 0;
                if (args.length >= 4) {
                    meta = Integer.parseInt(args[3]);
                }
                UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                for (int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++) {
                    for (int y = Math.min(cuboid.getYPos1(), cuboid.getYPos2()); y <= Math.max(cuboid.getYPos1(), cuboid.getYPos2()); y++) {
                        for (int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++) {
                            if (random.nextInt(100) < chance) {
                                ApiBlockServer.setBlockAt(user.getEntityWorld(), x, y, z, block, meta, ENotificationType.NOTIFY_CLIENTS.getType());
                            }
                        }
                    }
                }
                sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
            } else {
                sendChatLine(user, EChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
            }
        } catch (NumberFormatException e) {
            sendChatLine(user, EChatColor.COLOR_RED + "Invalid arguments!  Use /te genran <chance> <block> [metadata]");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Generates terrain based on random numbers";
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
        return getFunctionName() + " <chance> <block> [metadata]";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
