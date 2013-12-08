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

/**
 * Function that deletes the blocks in the cuboid.
 */
public class FunctionDelete extends Function {
    public FunctionDelete(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "delete";
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
        Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
        if(cuboid.getIsSet()){
            try{
                UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                for(int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++){
                    for(int y = Math.min(cuboid.getYPos1(), cuboid.getYPos2()); y <= Math.max(cuboid.getYPos1(), cuboid.getYPos2()); y++){
                        for(int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++){
                            ApiBlock.setBlockAt(user.getEntityWorld(), x, y, z, Blocks.field_150350_a, 0, ENotificationType.NOTIFY_CLIENTS.getType());
                        }
                    }
                }
                if(Config.getConfigForPlayer(user.getCommandSenderName()).commandConfirmation){
                    sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
                }
            }catch(Exception e){
                sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An error occurred while deleting blocks!");
                e.printStackTrace();
            }
        }else{
            sendChatLine(user, EChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Deletes all blocks in the selection";
    }
}
