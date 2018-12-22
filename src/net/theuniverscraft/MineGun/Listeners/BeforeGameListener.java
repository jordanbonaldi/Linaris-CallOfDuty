package net.theuniverscraft.MineGun.Listeners;

import net.theuniverscraft.MineGun.Game;
import net.theuniverscraft.MineGun.GameStatus;
import net.theuniverscraft.MineGun.Managers.LobbyManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class BeforeGameListener implements Listener {
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(Game.getInstance().getStatus() == GameStatus.LOBBY) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if(Game.getInstance().getStatus() == GameStatus.LOBBY) {
			event.setRespawnLocation(LobbyManager.getInstance().getLobby());
		}
	}
}
