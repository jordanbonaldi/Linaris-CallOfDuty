package net.theuniverscraft.MineGun.Listeners;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Bonus.Bonus;
import net.theuniverscraft.MineGun.Bonus.ButtonSlot;
import net.theuniverscraft.MineGun.Managers.BonusManager;
import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopInventoryListener implements Listener {
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();
		if(inv.getName().contains("Vous Avez")) {
			Integer points = PlayersManager.getInstance().getPlayerPoints(player);
			for(Bonus bonus : BonusManager.getInstance().getAllBonus()) {
				ItemStack is = bonus.getIs().clone();
				ItemMeta im = is.getItemMeta();
				if(bonus.getPrice() > points) im.setDisplayName(ChatColor.DARK_RED+ChatColor.stripColor(bonus.getName()));
				else im.setDisplayName(ChatColor.DARK_GREEN+ChatColor.stripColor(bonus.getName()));
				is.setItemMeta(im);
				
				inv.setItem(bonus.getSlot(), is);
			}
			
			for(ButtonSlot button : BonusManager.getInstance().getAllButton()) {
				ItemStack is = button.getIs().clone();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(button.getName());
				is.setItemMeta(im);
				
				inv.setItem(button.getSlot(), is);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(!event.getInventory().getName().contains("Vous Avez")) return;
		final Player player = (Player) event.getWhoClicked();
		
		try {
			event.setCancelled(true);
			
			Integer points = PlayersManager.getInstance().getPlayerPoints(player);
			
			ItemStack is = event.getCurrentItem();
			
			Bonus bonus = BonusManager.getInstance().getBonus(is.getItemMeta().getDisplayName());
			if(points >= bonus.getPrice()) {
				PlayersManager.getInstance().subPlayerPoints(player, bonus.getPrice());
				Utils.updateInterface(player);
				BonusManager.getInstance().addPlayerBonus(player, bonus);
				Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
					public void run() {
						player.closeInventory();
					}
				}
				, 1L);
				player.sendMessage(Translation.getString("YOU_HAVE_BUY").replaceAll("<bonus>", ChatColor.AQUA+bonus.getName()+ChatColor.RESET));
			}
			else {
				player.sendMessage(Translation.getString("YOU_HAVE_NOT_ENOUGH_POINT"));
			}
		}
		catch(Exception e) { // c'est surement un bouton
			try {
				ItemStack is = event.getCurrentItem();
				ButtonSlot button = BonusManager.getInstance().getButton(is.getItemMeta().getDisplayName());
				button.onClick((Player) event.getWhoClicked());
			}
			catch(Exception ee) {}
		}
	}
}
