package net.theuniverscraft.MineGun.Weapons.Weapons;

import net.theuniverscraft.MineGun.Managers.ForceManager;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LaunchWeapon extends Weapon {
	protected Double m_distanceMultiplier;
	public LaunchWeapon(String name, Material material, Double distanceMultiplier) {
		super(name, material, 0D);
		m_distanceMultiplier = distanceMultiplier;
	}
	
	public Item onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		final Item dropped = player.getWorld().dropItem(player.getEyeLocation().subtract(0, 0.1, 0), new ItemStack(m_material));
		dropped.setPickupDelay(Integer.MAX_VALUE);
		dropped.setVelocity(player.getLocation().getDirection().multiply(ForceManager.getInstance()
				.multiplierLaunch(event.getPlayer(), m_distanceMultiplier)));
		
		ItemStack inHand = player.getItemInHand();
		if(inHand.getAmount() <= 1) {
			player.setItemInHand(null);
		}
		else {
			inHand.setAmount(inHand.getAmount()-1);
		}
		return dropped;
	}
}
