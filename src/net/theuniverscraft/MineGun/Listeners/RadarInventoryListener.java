package net.theuniverscraft.MineGun.Listeners;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Managers.WeaponManager;
import net.theuniverscraft.MineGun.Weapons.Weapons.Radar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RadarInventoryListener implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		final Player owner = (Player) event.getWhoClicked();		
		try {
			if(!event.getInventory().getName().equalsIgnoreCase("Radar")) return;
			if(event.getInventory().getItem(event.getRawSlot()) == null) return;
			
			event.setCancelled(true);
			
			Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName());
			
			owner.sendMessage(ChatColor.GRAY+target.getName());
			owner.sendMessage(ChatColor.GRAY+"    X : "+target.getLocation().getX());
			owner.sendMessage(ChatColor.GRAY+"    Y : "+target.getLocation().getY());
			owner.sendMessage(ChatColor.GRAY+"    Z : "+target.getLocation().getZ());
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
				@Override
				public void run() {
					owner.closeInventory();
				}
			}, 3L);
		}
		catch(NullPointerException e) {
			owner.sendMessage(ChatColor.DARK_RED+Translation.getString("PLAYER_DONT_CO"));
			final Radar radar = (Radar) WeaponManager.getInstance().getWeapon(Material.QUARTZ);
			Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
				@Override
				public void run() {
					owner.closeInventory();
				}
			}, 3L);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
				@Override
				public void run() {
					radar.drawMenu(owner);
				}
			}, 5L);
		}
	}
}
