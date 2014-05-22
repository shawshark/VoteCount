package me.shawshark.votecount;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void PJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(SQLManager.loadPlayersVotes(Utils.getUUID(player.getName())) == 0) {
			SQLManager.updatePlayer(Utils.getUUID(player.getName()), player.getName(), 0);
			return;
		}
	}
}
