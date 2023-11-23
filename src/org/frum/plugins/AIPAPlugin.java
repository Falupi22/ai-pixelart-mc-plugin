package org.frum.plugins;

import org.frum.commands.AIPACommand;
import org.frum.files.FileHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.*;

public class AIPAPlugin extends JavaPlugin {
	private static AIPAPlugin instance;
	
	public AIPAPlugin() {
		super();
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		FileHelper.createFolder();
		
		System.out.println("AIPA Plugin attached!");
		
		//Bukkit.getPluginManager().registerEvents(new AIPACommand(), this);
		this.getCommand(AIPACommand.NAME).setExecutor(new AIPACommand());
	}
}
