package me.synergy.worldgriefregen.commands;

import me.synergy.worldgriefregen.Regen;
import me.synergy.worldgriefregen.Snapshot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WorldGriefRegenCommands implements CommandExecutor
{

	Snapshot snap = new Snapshot();

	Regen regen = new Regen();

	@Override
	//These commands can be executed via the console.
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		if ( args.length == 1 )
		{
			//Type /wgr snapshot
			//This will take a snapshot of the world, logging all blocks to the sql database saving the config defaults for each block.
			//This will NOT delete or modify existing data. Need separate commands.
			if ( args[0].equalsIgnoreCase( "snapshot" ) )		
			{
				snap.worldSnapShot();
			}
			
			//CREATE commands to: delete the database, and snapshot with overwrite.

			//Type /wgr regen
			//This will regen the world using the snapshotted blocks and flags in the SQL database. (Defaults are not used, only what was saved at the time of the snapshot.)
			if ( args[0].equalsIgnoreCase( "regen" ) )
			{
				regen.regenWorld();
			}
		}

		// TODO Auto-generated method stub
		return false;
	}

}
