package net.theuniverscraft.MineGun.Utils;

import net.theuniverscraft.MineGun.Managers.GameConfig;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class UtilsFight {
	public static double getArmorReduction(Entity entity, double degats) {
		if(GameConfig.SOLO_MODE || !(entity instanceof Player)) return degats;
		
		// On conscidere que le joueur a son armure en cuire -28% de dégats
		Player player = (Player) entity;
		PlayerInventory inv = player.getInventory();
		ItemStack helmet = inv.getHelmet();
		ItemStack chest = inv.getChestplate();
		ItemStack pants = inv.getLeggings();
		ItemStack boots = inv.getBoots();
		double red = 0.0;
		
		if (helmet != null)
		{
			if (helmet.getType() == Material.LEATHER_HELMET) red += 4.0;
			else if (helmet.getType() == Material.GOLD_HELMET) red += 8.0;
			else if (helmet.getType() == Material.CHAINMAIL_HELMET) red += 8.0;
			else if (helmet.getType() == Material.IRON_HELMET) red += 8.0;
			else if (helmet.getType() == Material.DIAMOND_HELMET) red += 12.0;
			helmet.setDurability((short) 0);
		}
		
		if (boots != null)
		{
			if (boots.getType() == Material.LEATHER_BOOTS) red += 4.0;
			else if (boots.getType() == Material.GOLD_BOOTS) red += 4.0;
			else if (boots.getType() == Material.CHAINMAIL_BOOTS) red += 4.0;
			else if (boots.getType() == Material.IRON_BOOTS) red += 8.0;
			else if (boots.getType() == Material.DIAMOND_BOOTS) red += 12.0;
			boots.setDurability((short) 0);
		}
		
		if (pants != null)
		{
			if (pants.getType() == Material.LEATHER_LEGGINGS) red += 8.0;
			else if (pants.getType() == Material.GOLD_LEGGINGS) red += 12.0;
			else if (pants.getType() == Material.CHAINMAIL_LEGGINGS) red += 16.0;
			else if (pants.getType() == Material.IRON_LEGGINGS) red += 20.0;
			else if (pants.getType() == Material.DIAMOND_LEGGINGS) red += 24.0;
			pants.setDurability((short) 0);
		}
		
		if (chest != null)
		{
			if (chest.getType() == Material.LEATHER_CHESTPLATE) red += 12.0;
			else if (chest.getType() == Material.GOLD_CHESTPLATE) red += 20.0;
			else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE) red += 20.0;
			else if (chest.getType() == Material.IRON_CHESTPLATE) red += 24.0;
			else if (chest.getType() == Material.DIAMOND_CHESTPLATE) red += 32.0;
			chest.setDurability((short) 0);
		}
		
		
		return (100.0 / (100.0 - red)) * degats;
	}
}
