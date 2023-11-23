package org.frum.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.frum.network.AppLayer;

public class AIPACommand implements CommandExecutor {

	public static final String NAME = "aipa";
	public static final int MAX_WORDS = 50;
	public static final int MAX_HEIGHT = 256;
	
	private Map<String, String> directions = new HashMap<String, String>();	
	
	public AIPACommand() {
		directions.put("n", "North");
		directions.put("s", "South");
		directions.put("e", "East");
		directions.put("w", "West");
		directions.put("ne", "FlatNorthEast");
		directions.put("nw", "FlatNorthWest");
		directions.put("se", "SouthNorthEast");
		directions.put("sw", "FlatSouthWest");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] comArgs) {
		if (sender instanceof Player) { 
			Player player = (Player)sender;

			if (comArgs.length > 0) {
				String subcommand = comArgs[0];
				
				switch(subcommand) {
				
				case "prompt":
					prompt(Arrays.copyOfRange(comArgs, 1, comArgs.length), player);
					break;
				default: 
					player.sendMessage("&d&lSubcommand " + subcommand + "&d&lis not supported yet!");
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	private void prompt(String[] args, Player player) {
		// Minecraft does not provide a built in way to parse a parameter in quotes as a single parameter
		String[] parsedArgs = parse(args);
		
		String prompt = parsedArgs[0];
		String direction = parsedArgs[1];
		String height = parsedArgs[2];
		player.sendMessage("Prompt is: " + prompt + ". Direction is: " + direction + ". Height is: " + height);
		AppLayer layer = new AppLayer();
		
		if (validate(player, prompt, direction, height)) {
			player.sendMessage("Wait a few moments for the image to be generated..");
			
			if (layer.getImagesByPrompt(prompt)) {
				// Execute the command
				player.performCommand("pp create response0.png " + directions.get(direction.toLowerCase()) + " " + height);		
			}
			else { 
				player.sendMessage("Command failed. Check out the parameters.");
			}
		}
		else {
			player.sendMessage("Command failed. Check out the parameters.");
		}
	}
	
	private String[] parse(String[] args)  {
		List<String> allParameters = new ArrayList<String>();
		String temp = "";
		
		boolean isInside = false;
		for (String arg : args) {
		  if (arg.startsWith("\"")) { 
		     isInside = true;
		     temp += arg.substring(1, arg.length()) + " "; //we don't want the quotation mark
		  }
		  else if (arg.endsWith("\"")) {
		     isInside = false;
		     temp += arg.substring(0, arg.length()-1) + " ";
			 allParameters.add(temp);
			 temp = ""; 
		  }
		  else if (isInside) {
			    temp += arg + " ";
		  }
		  else { 
			 allParameters.add(arg);
		  }
		}
		
		return allParameters.toArray(new String[allParameters.size()]);
	}
	
	private boolean validate(Player player, String prompt, String direction, String height) 
	{
		boolean isValid = false;
		
		if (prompt.length() == 0) { 
			player.sendMessage("Prompt cannot be empty!");
		}
		else if (prompt.split("\\s+").length > MAX_WORDS)
		{
			player.sendMessage("Max words for prompt reached! The maximum is " + MAX_WORDS);
		}
		else if (!directions.containsKey(direction.toLowerCase()))
		{
			player.sendMessage("\"" + direction + " is not a valid direction");
		}
		else
		{
			boolean heightValid = false;
			
			try { 
				Double dHeight = Double.parseDouble(height);
				
				if (dHeight < MAX_HEIGHT) { 
					heightValid = true;
				}
				else {
					player.sendMessage(height + " must be lower that " + MAX_HEIGHT);
				}
			}
			catch (NumberFormatException exception) {
				System.out.println(exception.toString());
				
				player.sendMessage(height + " is not a valid height");
			}
			
			isValid = heightValid;	
		}
		
		return isValid;
	}
}
