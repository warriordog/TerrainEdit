package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.acomputerdog.TerrainEdit.undo.UndoList;
import net.minecraft.src.ICommandSender;

/**
 * Sets the block at a specific location to a given type.
 */
public class FunctionSetAt extends BaseFunction {
    public FunctionSetAt(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "setat";
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
        if(args.length < 5){
            sendChatLine(user, EChatColor.COLOR_RED + "Not enough args!  Use /te setat <x> <y> <z> <block_id> [metadata]");
        }else{
            try{
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);
                int id = Integer.parseInt(args[4]);
                int meta = 0;
                if(args.length >= 6){
                    meta = Integer.parseInt(args[5]);
                }
                UndoList.createUndoTask(user.getEntityWorld(), x, y, z, x, y, z);
                user.getEntityWorld().setBlock(x, y, z, id, meta, ENotificationType.NOTIFY_CLIENTS.getType());
                sendChatLine(user, EChatColor.COLOR_YELLOW + "Set block successfully!");
            }catch(NumberFormatException e){
                sendChatLine(user, EChatColor.COLOR_RED + "Invalid args!  Use /te setat <x> <y> <z> <block_id> [metadata]");
            }catch(Exception e){
                sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An error occurred while setting the block!");
                e.printStackTrace();
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
        return "Sets the block at a given location.";
    }
}
