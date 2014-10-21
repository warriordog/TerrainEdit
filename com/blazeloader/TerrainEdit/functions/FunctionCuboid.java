package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import com.blazeloader.TerrainEdit.cuboid.CuboidTable;
import com.blazeloader.TerrainEdit.main.BlazeModTerrainEdit;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.api.api.chat.ChatColor;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class FunctionCuboid extends Function {
    public static final int CORNER_1 = 1;
    public static final int CORNER_2 = 2;

    private final int corner;

    public FunctionCuboid(BlazeModTerrainEdit baseMod, CommandTE baseCommand, int corner) {
        super(baseMod, baseCommand);
        this.corner = corner;
        register();
    }

    /**
     * Executes the command.
     *
     * @param user The user executing the command.
     * @param args The arguments passed to the module.
     */
    @Override
    public void execute(ICommandSender user, String[] args) {
        BlockPos loc = user.getPosition();
        Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getName());
        int locX = loc.getX();
        int locY = loc.getY();
        int locZ = loc.getZ();
        if (this.corner == CORNER_1) {
            cuboid.setXPos1(locX);
            cuboid.setYPos1(locY);
            cuboid.setZPos1(locZ);
            if (!cuboid.isSet()) {
                cuboid.setXPos2(locX);
                cuboid.setYPos2(locY);
                cuboid.setZPos2(locZ);
                cuboid.set();
            }
        } else {
            cuboid.setXPos2(locX);
            cuboid.setYPos2(locY);
            cuboid.setZPos2(locZ);
            if (!cuboid.isSet()) {
                cuboid.setXPos1(locX);
                cuboid.setYPos1(locY);
                cuboid.setZPos1(locZ);
                cuboid.set();
            }
        }
        sendChatLine(user, ChatColor.COLOR_YELLOW + "Set cuboid position " + corner + " to: " + locX + ", " + locY + ", " + locZ + ".");
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
    public String[] getFunctionNames() {
        return new String[]{"p" + this.corner, "pos" + this.corner};
    }
}
