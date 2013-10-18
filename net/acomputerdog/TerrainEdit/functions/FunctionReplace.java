package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.acomputerdog.TerrainEdit.cuboid.CuboidTable;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.World;

public class FunctionReplace extends BaseFunction{
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
            sendChatLine(user, EChatColor.COLOR_RED + "Not enough args!  Use /te replace <block1_id> <block2_id> [block1_meta] [block2_meta]");
        }else{
            try{
                Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
                if(cuboid.getIsSet()){
                    int id1 = Integer.parseInt(args[1]);
                    int id2 = Integer.parseInt(args[2]);
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
                    sendChatLine(user, EChatColor.COLOR_YELLOW + "Settings blocks...");
                    for(int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++){
                        for(int y = Math.min(cuboid.getYPos1(), cuboid.getYPos2()); y <= Math.max(cuboid.getYPos1(), cuboid.getYPos2()); y++){
                            for(int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++){
                                if(world.getBlockId(x, y, z) == id1){
                                    if(!useMeta1 || world.getBlockMetadata(x, y, z) == meta1){
                                        world.setBlock(x, y, z, id2, meta2, ENotificationType.NOTIFY_CLIENTS.getType());
                                    }
                                }
                            }
                        }
                    }
                    sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
                }else{
                    sendChatLine(user, EChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
                }
            }catch(NumberFormatException e){
                sendChatLine(user, EChatColor.COLOR_RED + "Invalid arguments!  Use /te replace <block1_id> <block2_id> [block1_meta] [block2_meta]");
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
