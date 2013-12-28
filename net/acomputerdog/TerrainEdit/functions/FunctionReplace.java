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
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

/**
 * Function that replaces all blocks in the cuboid of a specific type with another type.
 */
public class FunctionReplace extends Function {
    public FunctionReplace(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "replace";
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
        if(args.length < 3){
            sendChatLine(user, EChatColor.COLOR_RED + "Not enough args!  Use /te replace <block1> <block2> [block1_meta] [block2_meta]");
        }else{
            try{
                Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
                if(cuboid.getIsSet()){
                    Block block1 = ApiBlock.getBlockByNameOrId(args[1]);
                    Block block2 = ApiBlock.getBlockByNameOrId(args[2]);
                    boolean useMeta1 = false;
                    int meta1 = 0;
                    if(args.length >= 4){
                        meta1 = Integer.parseInt(args[3]);
                        useMeta1 = true;
                    }
                    int meta2 = 0;
                    if(args.length >= 5){
                        meta2 = Integer.parseInt(args[4]);
                    }
                    World world = user.getEntityWorld();
                    UndoList.createUndoTask(world, cuboid);
                    for(int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++){
                        for(int y = Math.min(cuboid.getYPos1(), cuboid.getYPos2()); y <= Math.max(cuboid.getYPos1(), cuboid.getYPos2()); y++){
                            for(int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++){
                                if(ApiBlock.getBlockAt(world, x, y, z) == block1){
                                    if(!useMeta1 || world.getBlockMetadata(x, y, z) == meta1){
                                        ApiBlock.setBlockAt(world, x, y, z, block2, meta2, ENotificationType.NOTIFY_CLIENTS.getType());
                                    }
                                }
                            }
                        }
                    }
                    if(Config.getConfigForPlayer(user.getCommandSenderName()).commandConfirmation){
                        sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
                    }
                }else{
                    sendChatLine(user, EChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
                }
            }catch(NumberFormatException e){
                sendChatLine(user, EChatColor.COLOR_RED + "Invalid arguments!  Use /te replace <block1> <block2> [block1_meta] [block2_meta]");
            }catch(Exception e){
                sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An error occurred while replacing blocks!");
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
        return "Replaces one type of block with another.";
    }
}
