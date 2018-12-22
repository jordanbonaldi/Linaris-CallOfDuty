package net.theuniverscraft.MineGun.Listeners;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Background.BGPlayers;
import net.theuniverscraft.MineGun.Managers.KitManager;
import net.theuniverscraft.MineGun.Managers.KitManager.Kit;
import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class KitInventoryListener implements Listener {
	public KitInventoryListener() {
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		KitManager.getInstance().setPlayerKit(event.getPlayer().getName(), KitManager.getInstance().getDefaultKit());
		Utils.setInv(event.getPlayer());
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Utils.setInv(event.getPlayer());
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(!event.getInventory().getName().equalsIgnoreCase("container.crafting")) return;
		final Player player = (Player) event.getWhoClicked();
		// Inventory inv = event.getInventory();
		
		try {
			event.setCancelled(true);
			if(event.getCurrentItem().getType() == Material.EMERALD) { // Shop
				if(!BGPlayers.isPlayer(player)) {
					player.sendMessage(Translation.getString("SHOP_ONLY_IN_GAME"));
					return;
				}
				
				player.closeInventory();
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
					@Override
					public void run() {
						player.openInventory(Bukkit.createInventory(player, 27, "Vous Avez : "+PlayersManager.getInstance().getPlayerPoints(player)+" points !"));
					}
				}, 2L);
			}
			else { // Class / kit
				ItemStack is = event.getCurrentItem();
				Kit kit = KitManager.getInstance().getKit(ChatColor.stripColor(is.getItemMeta().getDisplayName()));
				if(kit.isVipKit() && !PlayersManager.getInstance().playerIsVip(player)) {
					player.sendMessage(Translation.getString("MUST_VIP"));
					return;
				}
				KitManager.getInstance().setPlayerKit(player.getName(), kit);
				player.sendMessage(Translation.getString("CHOOSE_KIT").replaceAll("<kit>", kit.getName()));
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
				public void run() {
					player.closeInventory();
				}
			}
			, 1L);
		}
		catch(NullPointerException e) {}
	}
}
