package net.theuniverscraft.MineGun.Bonus;

import java.util.List;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.ForceManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BonusForce extends Bonus {
	public BonusForce(String name, Integer price, List<String> lore, Integer slot, ItemStack is, Integer level) {
		super(name, price, lore, slot, is, level);
	}
	
	public void onActive(Player player) {
		if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, m_level));
		
		ForceManager.getInstance().addForcer(player, m_level);
	}
	
	public void onReload(final Player player) {
		if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, m_level));
			}
		}, 3L);
	}
	
	public void onDelete(Player player) {
		if(player.hasPotionEffect(PotionEffectType.SPEED)) player.removePotionEffect(PotionEffectType.SPEED);
		ForceManager.getInstance().removeForcer(player);
	}
}
