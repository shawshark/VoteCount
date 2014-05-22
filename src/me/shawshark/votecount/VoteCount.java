package me.shawshark.votecount;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class VoteCount extends JavaPlugin {
	
	public static FileConfiguration config;
	public static Plugin plugin;
	
	public void onEnable() {
		config = getConfig();
		plugin = this;
		
		saveDefaultConfig();
		
		SQLManager.startConnection();
		
		getCommand("addvote").setExecutor(new CommandManager());
		getCommand("votes").setExecutor(new CommandManager());
		
		getServer().getPluginManager().registerEvents(
				new PlayerListener(), this
		);
		
	}
	
	public void onDisable() {

		SQLManager.con = null;
		config = null;
		plugin = null;
	}
	
}
