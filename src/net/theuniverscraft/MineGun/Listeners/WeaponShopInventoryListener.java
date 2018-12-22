package net.theuniverscraft.MineGun.Listeners;

import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Managers.WeaponManager;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Weapons.Weapons.GunWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponShopInventoryListener implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		try {
			Inventory inv = event.getInventory();
			final Player player = (Player) event.getWhoClicked();
			Integer points = PlayersManager.getInstance().getPlayerPoints(player);
						
			if(inv.getName().contains("Munitions")) {
				event.setCancelled(true);
				ItemStack is = event.getCurrentItem();
				
				Integer nbChargeur = 0;
				Integer price = 0;
				Weapon weapon = null;
				
				if(is.getType() == Material.FIREWORK_CHARGE) {
					weapon = WeaponManager.getInstance().getWeapon(is.getType());
					nbChargeur = is.getAmount();
					price = weapon.getWeaponPrice()*nbChargeur;
				}
				else {
					GunWeapon gun = WeaponManager.getInstance().getGunWeaponByItemCharger(is);
					weapon = gun;
					nbChargeur = is.getAmount();
					price = gun.getChargerPrice()*nbChargeur;
				}
				
				boolean haveAdd = false;
				
				if(points >= price) {
					for(int i = 0; i < 7; i++) {
						ItemStack isToUp = player.getInventory().getItem(i);
						if(isToUp != null) {
							if(isToUp.getType() == weapon.getMaterial()) {
								isToUp.setAmount(isToUp.getAmount() + nbChargeur);
								PlayersManager.getInstance().subPlayerPoints(player, price);
								Utils.updateInterface(player);
								player.closeInventory();
								
								player.sendMessage(Translation.getString("YOU_HAVE_BUY")
										.replaceAll("<bonus>", weapon.getName()+" *"+nbChargeur+ChatColor.RESET));
								haveAdd = true;
							}
						}
					}
					if(!haveAdd && is.getType() == Material.FIREWORK_CHARGE) {
						for(int i = 0; i < 7; i++) {
							ItemStack isToUp = player.getInventory().getItem(i);
							if(isToUp == null) {
								ItemStack newIs = new ItemStack(Material.FIREWORK_CHARGE, nbChargeur);
								
								ItemMeta im = newIs.getItemMeta();
								im.setDisplayName(WeaponManager.getInstance().getWeapon(Material.FIREWORK_CHARGE).getName());
								newIs.setItemMeta(im);
								
								player.getInventory().setItem(i, newIs);
								
								PlayersManager.getInstance().subPlayerPoints(player, price);
								Utils.updateInterface(player);
								player.closeInventory();
								
								player.sendMessage(Translation.getString("YOU_HAVE_BUY")
										.replaceAll("<bonus>", weapon.getName()+" *"+nbChargeur+ChatColor.RESET));
								return;
							}
						}
					}
				}
				else {
					player.sendMessage(Translation.getString("YOU_HAVE_NOT_ENOUGH_POINT"));
				}
			}
			else if(inv.getName().contains("Armes")) {
				event.setCancelled(true);
				ItemStack is = event.getCurrentItem();
				
				Weapon weapon = WeaponManager.getInstance().getWeaponByItemShop(is);
				if(points >= weapon.getWeaponPrice()) {
					ItemStack isToGive = new ItemStack(weapon.getMaterial());
					
					ItemMeta im = isToGive.getItemMeta();
					im.setDisplayName(weapon.getName());
					isToGive.setItemMeta(im);
					
					if(!Utils.givePlayerWeapon(player, isToGive)) {
						player.sendMessage(Translation.getString("YOU_TOO_LOT_WEAPON"));
						return;
					}
					
					PlayersManager.getInstance().subPlayerPoints(player, weapon.getWeaponPrice());
					Utils.updateInterface(player);
					player.closeInventory();
					
					player.sendMessage(Translation.getString("YOU_HAVE_BUY")
							.replaceAll("<bonus>", weapon.getName()+ChatColor.RESET));
				}
				else {
					player.sendMessage(Translation.getString("YOU_HAVE_NOT_ENOUGH_POINT"));
				}
			}
		}
		catch(NullPointerException e) {}
	}
}
