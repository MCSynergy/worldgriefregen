package me.synergy.worldgriefregen;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

//This will snapshot the world blocks into the SQL and attach default flags.
public class Snapshot
{


	public void worldSnapShot()
	{
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
		
		//Other default variables which will be found in the config file.
		
		//World Name from config file. Needed for multiworld support.
		String worldName = "world";
		//Load the world
		World world = Bukkit.getWorld(worldName);
		
		//Respawn timer (in sec) for the block. (Needed when dynamic regen is added)
		//  int respawnTimer = 15;
		
		//Is the block protected by another plugin.
		//  boolean ignoreRegen = false;
		
		//Whitelist or Blacklist replacement blocks. If true then Whitelist: only replace if the current block is in the list. If false then Blacklist, only replace if block is NOT in the list.
		boolean replaceWhitelist = true;
		
		//Replacement list. Does the opposite if whitelist == false.
		// 0=Air,4=cobblestone,8=Water,9=Stationary Water,10=lava,11=stationary lava,12=sand,13=gravel,49=obsidian,78-80=snow/ice
		//FUTURE: this should be an array of arrays to include data values, but keep it simple for now.
		int[] replacedBlocks = {0,4,8,9,10,11,12,13,49,78,79,80};
		
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
								// 2) Does the SQL entry already exist for (xPoint,yPoint,zPoint)?
								//      If it does, skip the next steps.
								// 3) Create an SQL entry (xPoint,yPoint,zPoint).
								int[] chunkCoord = {xChunk,zChunk};
								int[] blockCoord = {xPoint,yPoint,zPoint};
																
								// 3.1) Record the block {ID:Data} values.
								Block block = world.getBlockAt(xPoint, yPoint, zPoint);
								Material material = block.getType();
								//use chunk snapshot instead
								// 3.2) Record the default flags according to the config file.
							}
						}
					} //else skip this chunk
				}
			}
		}
			
			
	}
	
}
