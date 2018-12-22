package net.theuniverscraft.MineGun.Weapons.Weapons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PowerWeapon extends Weapon {
	public PowerWeapon(String name, Material material) {
		super(name, material, 0D);
	}
	
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		ItemStack inHand = player.getItemInHand();
		if(inHand.getAmount() <= 1) {
			player.setItemInHand(null);
		}
		else {
			inHand.setAmount(inHand.getAmount()-1);
		}
	}
	public void onLeftClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		ItemStack inHand = player.getItemInHand();
		if(inHand.getAmount() <= 1) {
			player.setItemInHand(null);
		}
		else {
			inHand.setAmount(inHand.getAmount()-1);
		}
	}
}
