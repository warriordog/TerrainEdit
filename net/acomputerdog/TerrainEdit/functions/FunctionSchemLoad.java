package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.config.Config;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.acomputerdog.TerrainEdit.schematic.Schematic;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChunkCoordinates;

import java.io.File;

/**
 * Loads a schematic and imports it into the world.  Currently uses very old and buggy schematic loading code that will be replaced soon.
 * Cannot currently be undone!
 */
public class FunctionSchemLoad extends Function {
    public FunctionSchemLoad(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "schemload";
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
        try{
            if(args.length < 2){
                sendChatLine(user, EChatColor.COLOR_RED + "Not enough args!  Use /schemload <path_to_schematic> [x] [y] [z]");
            }else{
                File schematic = new File(args[1]);
                if(schematic.exists() && schematic.isFile()){
                    if(args.length == 2){
                        ChunkCoordinates loc = user.getPlayerCoordinates();
                        try{
                            new Schematic(schematic).place(user.getEntityWorld(), loc.posX, loc.posY, loc.posZ);
                            sendChatLine(user, EChatColor.COLOR_YELLOW + "Placed schematic.");
                        }catch(Exception e){
                            sendChatLine(user, EChatColor.COLOR_RED.toString() + EChatColor.FORMAT_BOLD.toString() + EChatColor.FORMAT_UNDERLINE.toString() + "Unable to place schematic!");
                            e.printStackTrace();
                        }
                    }else if(args.length >= 5){
                        try{
                            new Schematic(schematic).place(user.getEntityWorld(), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[2]));
                            if(Config.getConfigForPlayer(user.getCommandSenderName()).commandConfirmation){
                                sendChatLine(user, EChatColor.COLOR_YELLOW + "Placed schematic.");
                            }
                        }catch(Exception e){
                            sendChatLine(user, EChatColor.COLOR_RED.toString() + EChatColor.FORMAT_BOLD.toString() + EChatColor.FORMAT_UNDERLINE.toString() + "Unable to place schematic!");
                            e.printStackTrace();
                        }
                    }else{
                        sendChatLine(user, EChatColor.COLOR_RED + "Illegal args!  Use /schemload <path_to_schematic> [x] [y] [z]");
                    }
                }else{
                    sendChatLine(user, EChatColor.COLOR_RED + "The specified schematic was not found!");
                }
            }
        }catch(NumberFormatException e){
            sendChatLine(user, EChatColor.COLOR_RED + "Illegal args!  x, y, and z must be integers!");
        }catch(Exception e){
            sendChatLine(user, EChatColor.COLOR_RED.toString() + EChatColor.FORMAT_BOLD.toString() + EChatColor.FORMAT_UNDERLINE.toString() + "An error occurred while placing schematic!");
            e.printStackTrace();
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Imports a schematic into the world.";
    }
}
