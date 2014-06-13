package me.synergy.worldgriefregen;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;


public class WorldGriefRegen extends JavaPlugin {
	public MySQLManager mysql = new MySQLManager(this);
	public Snapshot snapshot = new Snapshot(this);
	public Commands commands = new Commands(this);
	public Regen regen = new Regen(this);
	
	public boolean errorDB = false;
	
	public void onEnable() {
		try {
			this.mysql.setupDB();
		} catch (SQLException e) {
			this.errorDB = true;
			this.log("Could not connect to SQL database.", Level.WARNING);
			this.log("Error:"+e, Level.INFO);
		}
	//FUTURE: autoregen map when plugin loads.	
	}
	
	public void onDisable()
	{
		this.mysql.closeDB();
	}
	
	public void log(String s, Level l) {
		getLogger().log(l, "[WGR]"+s);
	}
	
}
