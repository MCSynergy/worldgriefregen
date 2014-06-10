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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		if ( args.length == 1 )
		{
			if ( args[0].equalsIgnoreCase( "snapshot" ) )
			{
				snap.worldSnapShot();
			}

			if ( args[0].equalsIgnoreCase( "regen" ) )
			{
				regen.regenWorld();
			}
		}

		// TODO Auto-generated method stub
		return false;
	}

}
