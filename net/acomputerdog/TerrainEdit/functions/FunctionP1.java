package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.acomputerdog.TerrainEdit.cuboid.CuboidTable;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ICommandSender;

/**
 * Sets the first cuboid position.
 */
public class FunctionP1 extends BaseFunction {

    public FunctionP1(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "p1";
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
        ChunkCoordinates loc = user.getPlayerCoordinates();
        Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
        cuboid.setXPos1(loc.posX);
        cuboid.setYPos1(loc.posY);
        cuboid.setZPos1(loc.posZ);
        sendChatLine(user, EChatColor.COLOR_YELLOW + "Set cuboid position 1 to: " + loc.posX + ", " + loc.posY + ", " + loc.posZ + ".");
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Sets the first cuboid location.";
    }
}
