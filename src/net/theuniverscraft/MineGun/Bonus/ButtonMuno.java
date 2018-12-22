package net.theuniverscraft.MineGun.Bonus;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.WeaponManager;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Weapons.Weapons.GunWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ButtonMuno extends ButtonSlot {
	public ButtonMuno(String name, List<String> lore, Integer slot, ItemStack is) {
		super(name, lore, slot, is);
	}
	
	public void onClick(final Player player) {
		player.closeInventory();
		
		final Integer points = PlayersManager.getInstance().getPlayerPoints(player);
		final Inventory inv = Bukkit.createInventory(player, 54, "Munitions ! "+points.toString()+" points !");
		
		final List<Material> mats = new LinkedList<Material>();
		for(int i = 0; i < 7; i++) {
			if(player.getInventory().getItem(i) != null) {
				mats.add(player.getInventory().getItem(i).getType());
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
			public void run() {
				// Boucler sur les armes (gun)
				int slot = 0;
				for(Weapon weapon : WeaponManager.getInstance().getListWeapon()) {
					if(weapon instanceof GunWeapon) {
						GunWeapon gun = (GunWeapon) weapon;
						
						if(mats.contains(gun.getMaterial())) {
							// C'est bon il a l'arme
							for(int i = 0; i < 9; i += 2) {
								int nbChargeur = i == 0 ? 1 : i == 2 ? 3 : i == 4 ? 5 : i == 6 ? 7 : i == 8 ? 10 : 0;
								ItemStack is = gun.getItemStackCharger();
								is.setAmount(nbChargeur);
								Integer price = gun.getChargerPrice()*nbChargeur;
								
								ItemMeta im = is.getItemMeta();
								String name = gun.getName();
								if(points < gun.getChargerPrice()) name = ChatColor.DARK_RED+name;
								im.setDisplayName(name);
								
								List<String> lore = new LinkedList<String>();
								String unite = nbChargeur <= 0 ? "chargeur" : "chargeurs";
								lore.add(nbChargeur+" "+unite);
								lore.add("");
								lore.add(price.toString()+" points");
								im.setLore(lore);
								
								is.setItemMeta(im);
								inv.setItem(slot*9 + i, is);
							}
							slot++;
						}
					}
				}
				if(Utils.canGivePlayerWeapon(player, Material.FIREWORK_CHARGE)) {
					Weapon grenade = WeaponManager.getInstance().getWeapon(Material.FIREWORK_CHARGE);
					for(int i = 0; i < 9; i += 2) {
						int nbChargeur = i == 0 ? 1 : i == 2 ? 3 : i == 4 ? 5 : i == 6 ? 7 : i == 8 ? 10 : 0;
						ItemStack is = new ItemStack(grenade.getMaterial());
						is.setAmount(nbChargeur);
						Integer price = grenade.getWeaponPrice()*nbChargeur;
						
						ItemMeta im = is.getItemMeta();
						String name = grenade.getName();
						if(points < grenade.getWeaponPrice()) name = ChatColor.DARK_RED+name;
						im.setDisplayName(name);
						
						List<String> lore = new LinkedList<String>();
						String unite = nbChargeur <= 0 ? "grenade" : "grenades";
						lore.add(nbChargeur+" "+unite);
						lore.add("");
						lore.add(price.toString()+" points");
						im.setLore(lore);
						
						is.setItemMeta(im);
						inv.setItem(slot*9 + i, is);
					}
				}
				
				player.openInventory(inv);
			}
		}, 3L);
	}
}
