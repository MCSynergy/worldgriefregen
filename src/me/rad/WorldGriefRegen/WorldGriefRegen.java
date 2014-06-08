package me.rad.WorldGriefRegen;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGriefRegen {

	private static final Logger log = Logger.getLogger("Minecraft");
	
	@Override
	public void onEnable() {
		log.info("[World Grief Regen] has been enabled!");
		PluginManager pm = getServer().getPluginManager();
		
	}
	
	@Override
	public void onDisable() {
		log.info("[World Grief Regen] has been disabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
}
