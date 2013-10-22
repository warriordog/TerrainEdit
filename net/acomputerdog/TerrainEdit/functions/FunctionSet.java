package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.config.Config;
import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.acomputerdog.TerrainEdit.cuboid.CuboidTable;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.acomputerdog.TerrainEdit.undo.UndoList;
import net.minecraft.src.ICommandSender;

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
        if(args.length < 2){
            sendChatLine(user, EChatColor.COLOR_RED + "Not enough arguments!  Use /te set <block_id> [metadata]");
        }else{
            Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
            if(cuboid.getIsSet()){
                try{
                    int id = Integer.parseInt(args[1]);
                    int meta = 0;
                    if(args.length >= 3){
                        meta = Integer.parseInt(args[2]);
                    }
                    UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                    for(int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++){
                        for(int y = Math.min(cuboid.getYPos1(), cuboid.getYPos2()); y <= Math.max(cuboid.getYPos1(), cuboid.getYPos2()); y++){
                            for(int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++){
                                user.getEntityWorld().setBlock(x, y, z, id, meta, ENotificationType.NOTIFY_CLIENTS.getType());
                            }
                        }
                    }
                    if(Config.getConfigForPlayer(user.getCommandSenderName()).commandConfirmation){
                        sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
                    }
                }catch(NumberFormatException e){
                    sendChatLine(user, EChatColor.COLOR_RED + "Invalid arguments!  Use /te set <block_id> [metadata]");
                }catch(Exception e){
                    sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An error occurred while setting blocks!");
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
        return "Sets the blocks in the cuboid.";
    }
}
