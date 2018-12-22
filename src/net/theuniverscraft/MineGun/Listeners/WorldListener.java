package net.theuniverscraft.MineGun.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

public class WorldListener implements Listener {
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onHangingBreak(HangingBreakEvent event) {
		event.setCancelled(true);
	}
}
