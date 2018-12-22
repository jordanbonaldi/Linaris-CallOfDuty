package net.theuniverscraft.MineGun.Listeners;

import net.theuniverscraft.MineGun.Managers.RespawnTimerManager;
import net.theuniverscraft.MineGun.Managers.RespawnTimerManager.Respawner;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class DeadProtectionListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(RespawnTimerManager.getInstance().getRespawner(event.getPlayer()) != null) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(RespawnTimerManager.getInstance().getRespawner(event.getPlayer()) != null)
			event.setCancelled(true);
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(RespawnTimerManager.getInstance().getRespawner(event.getPlayer()) != null)
			event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(RespawnTimerManager.getInstance().getRespawner(event.getPlayer()) != null)
			event.setCancelled(true);
	}
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			if(RespawnTimerManager.getInstance().getRespawner((Player) event.getEntity()) != null)
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Respawner respawner = RespawnTimerManager.getInstance().getRespawner(event.getPlayer());
		if(respawner != null) {
			Location now = event.getPlayer().getLocation();
			Location last = respawner.getLastLocation();
			
			if(now.getX() != last.getX() || now.getY() != last.getY() || now.getZ() != last.getZ()) {
				Location zoneLoc = respawner.getSpawnPoint().getLocation();
				event.getPlayer().teleport(zoneLoc);
				respawner.setLastLocation(zoneLoc);
			}
		}
	}
}
