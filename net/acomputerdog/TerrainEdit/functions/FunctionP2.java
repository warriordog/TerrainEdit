package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.acomputerdog.TerrainEdit.cuboid.CuboidTable;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ICommandSender;

/**
 * Sets the second cuboid position.
 */
public class FunctionP2 extends BaseFunction {

    public FunctionP2(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "p2";
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
        cuboid.setXPos2(loc.posX);
        cuboid.setYPos2(loc.posY);
        cuboid.setZPos2(loc.posZ);
        if(!cuboid.getIsSet()){
            cuboid.setXPos1(loc.posX);
            cuboid.setYPos1(loc.posY);
            cuboid.setZPos1(loc.posZ);
            cuboid.setIsSet(true);
        }
        sendChatLine(user, EChatColor.COLOR_YELLOW + "Set cuboid position 2 to: " + loc.posX + ", " + loc.posY + ", " + loc.posZ + ".");
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Sets the second cuboid location.";
    }
}
