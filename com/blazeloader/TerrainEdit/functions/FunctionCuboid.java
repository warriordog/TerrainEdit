package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import com.blazeloader.TerrainEdit.cuboid.CuboidTable;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.main.ModTerrainEdit;
import com.blazeloader.api.direct.base.api.chat.EChatColor;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChunkCoordinates;

public class FunctionCuboid extends Function {
    public static final int CORNER_1 = 0;
    public static final int CORNER_2 = 1;

    private final int corner;
    private final String commandName;

    public FunctionCuboid(ModTerrainEdit baseMod, CommandTE baseCommand, int corner) {
        super(baseMod, baseCommand);
        this.corner = corner;
        this.commandName = "p" + corner;
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return commandName;
    }

    /**
     * Executes the command.
     *
     * @param user The user executing the command.
     * @param args The arguments passed to the module.
     */
    @Override
    public void execute(ICommandSender user, String[] args) {
        ChunkCoordinates loc = user.getPlayerCoordinates();
        Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
        if (this.corner == CORNER_1) {
            cuboid.setXPos1(loc.posX);
            cuboid.setYPos1(loc.posY);
            cuboid.setZPos1(loc.posZ);
            if (!cuboid.isSet()) {
                cuboid.setXPos2(loc.posX);
                cuboid.setYPos2(loc.posY);
                cuboid.setZPos2(loc.posZ);
                cuboid.set();
            }
        } else {
            cuboid.setXPos2(loc.posX);
            cuboid.setYPos2(loc.posY);
            cuboid.setZPos2(loc.posZ);
            if (!cuboid.isSet()) {
                cuboid.setXPos1(loc.posX);
                cuboid.setYPos1(loc.posY);
                cuboid.setZPos1(loc.posZ);
                cuboid.set();
            }
        }
        sendChatLine(user, EChatColor.COLOR_YELLOW + "Set cuboid position " + corner + " to: " + loc.posX + ", " + loc.posY + ", " + loc.posZ + ".");
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Sets cuboid position " + corner + ".";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return PERMISSION_PLAYER;
    }

    @Override
    public int getNumRequiredArgs() {
        return 0;
    }

    @Override
    public String getFunctionUsage() {
        return getFunctionName();
    }

    @Override
    public String[] getAliases() {
        return new String[]{"pos" + corner};
    }
}
