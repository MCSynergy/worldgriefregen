package me.synergy.worldgriefregen;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

//This will snapshot the world blocks into the SQL and attach default flags.
public class Snapshot {
	private final WorldGriefRegen main;
	
	public Snapshot(WorldGriefRegen h) {
		this.main = h;
	}

	public void worldSnapShot(String worldName,boolean overwrite) {
		if (this.main.errorDB) {
			this.main.log("Could not execute worldSnapShot.", Level.WARNING);
			this.main.log("Error: could not connect to SQL database.", Level.INFO);
		}
		else {
			//The "defined" world size. This will be found in the config file. Manually calculating it will be hard, keep it easy for now.
			int x1 = -1000;
			int x2 = 1000;
			int z1 = -1000;
			int z2 = 1000;
			
			// The "%" is the modulus or remainder of the number. This converts the numbers into multiples of 16. (size of a chunk)
			int xMin = x1 - (x1 % 16);
			int xMax = x2 - (x2 % 16);
			int zMin = z1 - (z1 % 16);
			int zMax = z2 - (z2 % 16);
				
			int yMin = 0;
			int yMax = 256;
			
			//Load the world
			World world = Bukkit.getWorld(worldName);
			
			//Loop through every ~chunk~ using the "defined" world size.
			//Increment by 16, the size of a chunk.
			for (int xChunk = xMin ; xChunk <= xMax ; xChunk = xChunk + 16)
			{
				for (int zChunk = zMin ; zChunk <= zMax ; zChunk = zChunk + 16)
				{
					//Get the current chunk
					ChunkSnapshot currentCS = world.getChunkAt(xChunk, zChunk).getChunkSnapshot(false,false,false);
					ChunkSnapshot emptyCS = world.getEmptyChunkSnapshot(xChunk, zChunk, false, false);
					//Check to see if the current chunk is NOT equal to the empty chunk. If it is equal, then the chunk is not rendered and should be ignored (or else just air would be saved).
					if (!currentCS.equals(emptyCS))
					{
						//Loop though every ~block~ within the chunk.
						for (int yPoint = yMin ; yPoint <= yMax ; yPoint++)
						{
							for (int xPoint = xChunk ; xPoint < (xChunk + 16) ; xPoint++)
							{
								for (int zPoint = zChunk ; zPoint < (zChunk + 16) ; zPoint++)
								{			
									//Gets the string of the block. Ex. DIAMOND_ORE.
									String blockMaterialName = world.getBlockAt(xPoint, yPoint, zPoint).getType().toString();
									
									//Get the default values for that block from the config file.
									//CREATE procedures for each value.
									//Respawn timer (in sec) for the block. (Needed when dynamic regen is added)
									int respawnTimer = 15;
									
									//Is the block protected by another plugin.
									boolean ignoreRegen = false;
									
									//Whitelist or Blacklist replacement blocks. If true then Whitelist: only replace if the current block is in the list. If false then Blacklist, only replace if block is NOT in the list.
									boolean replaceWhitelist = true;
									
									//Replacement list.
									//  Does the opposite if whitelist = false.
									String[] replacedBlocks = {"AIR","COBBLESTONE","WATER","STATIONARY_WATER","LAVA","STATIONARY_LAVA","SAND","GRAVEL","OBSIDIAN","ICE","SNOW","SNOW_BLOCK"};
									//Use this later:
									//  Material test = Material.getMaterial(replacedBlocks[0]);
									
									//updateBlockData(boolean overwrite, int x, int y, int z, String material, int timer, boolean ignore, boolean whitelist, String[] replacedMaterials)
									try {
										this.main.mysql.updateBlockData(overwrite, worldName, xPoint, yPoint, zPoint, blockMaterialName, respawnTimer, ignoreRegen, replaceWhitelist, replacedBlocks);
									} catch (SQLException e) {
										this.main.log("Could not update block in SQL database. World=["+worldName+"] (x="+xPoint+", y="+yPoint+", z="+zPoint+") Material=["+blockMaterialName+"]", Level.WARNING);
										this.main.log("Error:"+e, Level.INFO);
									}
	
								}
							}
						}
						//else skip this chunk
					}
				}
			}
		}			
	}
}
