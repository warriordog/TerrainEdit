package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.block.ApiBlock;
import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.config.Config;
import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.acomputerdog.TerrainEdit.cuboid.CuboidTable;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.acomputerdog.TerrainEdit.undo.UndoList;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * A function that shifts blocks up or down by an amount.
 */
public class FunctionShift extends Function {
    public FunctionShift(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "shift";
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
        if(args.length < 2){
            sendChatLine(user, EChatColor.COLOR_RED + "Not enough arguments!  Use /te shift <distance> [switches]");
        }else{
            Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
            if(cuboid.getIsSet()){
                try{
                    int distance = Integer.parseInt(args[1]);
                    boolean allowShiftOutOfCuboid = false;
                    if(args.length >= 3){
                        List<String> switches = Arrays.asList(args[2].split("-"));
                        if(switches.contains("allowOutOfCuboid")){
                            allowShiftOutOfCuboid = true;
                        }
                    }
                    UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                    World world = user.getEntityWorld();
                    boolean dir = distance >= 0;
                    int y1 = dir ? cuboid.getMaxY() : cuboid.getMinY();
                    int y2 = dir ? cuboid.getMinY() : cuboid.getMaxY();
                    for(int x = cuboid.getMinX(); x <= cuboid.getMaxX(); x++){
                        for(int z = cuboid.getMinZ(); z <= cuboid.getMaxZ(); z++){
                            for(int y = y2; dir ? y >= y1 : y <= y1; y = dir ? y-1 : y+1){
                                if(allowShiftOutOfCuboid || (y + distance >= y2 && y + distance <= y1)){
                                    if(dir){
                                        ApiBlock.setBlock(world, x, y + distance, z, ApiBlock.getBlock(world, x, y, z), world.getBlockMetadata(x, y, z), ENotificationType.NOTIFY_CLIENTS.getType());
                                        ApiBlock.setBlock(world, x, y, z, Blocks.field_150350_a, 0, ENotificationType.NOTIFY_CLIENTS.getType());
                                    }else{
                                        ApiBlock.setBlock(world, x, y - distance, z, ApiBlock.getBlock(world, x, y, z), world.getBlockMetadata(x, y, z), ENotificationType.NOTIFY_CLIENTS.getType());
                                        ApiBlock.setBlock(world, x, y, z, Blocks.field_150350_a, 0, ENotificationType.NOTIFY_CLIENTS.getType());
                                    }
                                }
                            }
                        }
                    }
                    if(Config.getConfigForPlayer(user.getCommandSenderName()).commandConfirmation){
                        sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
                    }
                }catch(NumberFormatException e){
                    sendChatLine(user, EChatColor.COLOR_RED + "Invalid arguments!  Use /te shift <distance> [switches]");
                }catch(Exception e){
                    sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An error occurred while shifting blocks!");
                    e.printStackTrace();
                }
            }else{
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
        return "Shifts blocks up or down by an amount.";
    }
}
