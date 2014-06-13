package me.synergy.worldgriefregen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import code.husky.mysql.MySQL;

public class MySQLManager {

	private final WorldGriefRegen main;
	private MySQL db;
	
	public MySQLManager(WorldGriefRegen h) {
		this.main = h;
	}
	
	public void setupDB() throws SQLException {
		//ADD CONFIG FILE
		this.db = new MySQL(this.main, "localhost","3306","dbname","username","pass");
		this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS 'wgrsnapshot' ('World' String, 'X' int, 'Y' int, 'Z' int, 'Material' String, 'Timer' int, 'Ignore' boolean, 'Whitelist' boolean, 'ReplacedMaterials' String[]);");
		statement.close();
	}
	
	public void closeDB() {
		this.db.closeConnection();
	}
	
	//Return True if the block should be replaced.
	public boolean regenBlockQuestion(String world, int x,int y, int z, String currentMaterial) throws SQLException {
		if (!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM 'wgrsnapshot' WHERE 'World'='world' AND 'X'='x' AND 'Y'='y' AND 'Z'='z';");
		//Does the entry exists?
		if (!rs.next()) {
			return false;
		}
		//Should the regening be ignored?
		if (rs.getBoolean("Ignore")) {
			return false;
		}
		//Is the currentMaterial in the ReplacedMaterials list?
		boolean containsMaterial = Arrays.asList(rs.getArray("ReplacedMaterials")).contains(currentMaterial);
		//Does the block match the whitelist?
		if (rs.getBoolean("Whitelist")) {
			return containsMaterial;
		}
		//Else, does block NOT match the blacklist
		else {
			return !containsMaterial;
		}
	}
	
	//return the SQL material as a string
	public String regenBlockMaterial(String world, int x, int y, int z) throws SQLException {
		if (!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT 'Material' FROM 'wgrsnapshot' WHERE 'World'='world' AND 'X'='x' AND 'Y'='y' AND 'Z'='z';");
		if (!rs.next()) {
			return null;
		}
		else {
			return rs.getString("Material");
		}
	}
	
	//Add the block to the DB with flags
	public void updateBlockData(boolean overwrite, String world, int x, int y, int z, String material, int timer, boolean ignore, boolean whitelist, String[] replacedMaterials) throws SQLException {
		if (!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM 'wgrsnapshot' WHERE 'World'='world' AND 'X'='x' AND 'Y'='y' AND 'Z'='z';");
		//If entry does NOT exist, add it
		if (!rs.next()){
			statement.executeUpdate("INSERT INTO 'wgrsnapshot' ('World', 'X', 'Y', 'Z', 'Material', 'Timer', 'Ignore', 'Whitelist', 'ReplacedMaterials') VALUES ('world','x','y','z','material','timer','ignore','whitelist','replaceMaterials');");
		}
		//If overwrite is enabled, update every value.
		else if (overwrite) {
			statement.executeUpdate("UPDATE 'wgrsnapshot' SET 'Material'='material','Timer'='timer','Ignore'='ignore','Whitelist'='whitelist,'ReplaceMaterials'='replacedMaterials' WHERE 'World'='world' AND 'X'='x' AND 'Y'='y' AND 'Z'='z';");
		}
		//Else, do nothing, Block already exists in DB and shouldn't be changed.
		
	}
}
