package me.shawshark.votecount;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandManager implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender s, Command c, String l, String[] args) {
		if(c.getName().equalsIgnoreCase("addvote")) {
			
			if(!s.hasPermission("votecount.addvotes")) {
				s.sendMessage(ChatColor.RED + "You need the permission node 'votecount.addvotes' to perform this command!");
				return true;
			}
			
			if(!(args.length == 2)) {
				s.sendMessage(ChatColor.GOLD + "/addvote <player name> <votes>");
				return true;
			}
			
			try {
				@SuppressWarnings("unused")
				int t = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				s.sendMessage(ChatColor.RED + args[1] + " isn't a number!");
				return true;
			}
			
			final String player = args[0];
			int addvotes = Integer.parseInt(args[1]);
			
			SQLManager.updatePlayer(Utils.getUUID(player), player, addvotes);
			
			s.sendMessage(ChatColor.GREEN + "You have updated " + ChatColor.DARK_GREEN + 
					player + ChatColor.GREEN + " with " + ChatColor.DARK_GREEN + "+" + addvotes + 
						ChatColor.GREEN + "!");
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					s.sendMessage(ChatColor.DARK_GREEN + player + ChatColor.GREEN + " Now has: " + 
							ChatColor.DARK_GREEN  + SQLManager.loadPlayersVotes(Utils.getUUID(player)) + 
								ChatColor.GREEN + " votes!");
				}
			}.runTaskLaterAsynchronously(VoteCount.plugin, 20);
			
			return true;
			
		}
		
		if(c.getName().equalsIgnoreCase("votes")) {
			
			s.sendMessage(ChatColor.GREEN + "You have " + ChatColor.DARK_GREEN + 
					SQLManager.loadPlayersVotes(Utils.getUUID(s.getName())) + 
						ChatColor.GREEN + " votes!");
			
			return true;
		}
		
		return false;
	}

}
 
