package net.theuniverscraft.MineGun.Bonus;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.WeaponManager;
import net.theuniverscraft.MineGun.Weapons.Weapons.Drone;
import net.theuniverscraft.MineGun.Weapons.Weapons.GunWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.Radar;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ButtonWeapon extends ButtonSlot {
	public ButtonWeapon(String name, List<String> lore, Integer slot, ItemStack is) {
		super(name, lore, slot, is);
	}
	
	public void onClick(final Player player) {
		player.closeInventory();
		
		final Integer points = PlayersManager.getInstance().getPlayerPoints(player);
		final Inventory inv = Bukkit.createInventory(player, 27, "Armes ! "+points.toString()+" points !");
		
		final List<Material> mats = new LinkedList<Material>();
		for(int i = 0; i < 7; i++) {
			if(player.getInventory().getItem(i) != null) {
				mats.add(player.getInventory().getItem(i).getType());
			}
		}
		if(!mats.contains(Material.FIREWORK_CHARGE)) mats.add(Material.FIREWORK_CHARGE);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
			public void run() {
				// Boucler sur les armes (gun)
				int slot = 0;
				for(Weapon weapon : WeaponManager.getInstance().getListWeapon()) {						
					if(!mats.contains(weapon.getMaterial())) {
						// C'est bon il a l'arme
						ItemStack is = weapon.getItemStackShop();
						
						ItemMeta im = is.getItemMeta();
						String name = weapon.getName();
						if(points < weapon.getWeaponPrice()) name = ChatColor.DARK_RED+name;
						im.setDisplayName(name);
						List<String> lore = new LinkedList<String>();
						if(weapon instanceof GunWeapon) lore.add("une armes + un chargeur");
						else if(weapon instanceof Radar) lore.add("Localise les 18 énnemis les plus proches");
						else if(weapon instanceof Drone) {
							Drone drone = (Drone) weapon;
							lore.add("Bombarde "+drone.getNbTarget()+" énnemis aléatoirement");
						}
						lore.add("");
						lore.add(weapon.getWeaponPrice().toString()+" points");
						im.setLore(lore);
						
						is.setItemMeta(im);
						inv.setItem(slot, is);
						
						slot++;
					}
				}
				player.openInventory(inv);
			}
		}, 3L);
	}
}
