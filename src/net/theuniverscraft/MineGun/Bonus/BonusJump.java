package net.theuniverscraft.MineGun.Bonus;

import java.util.List;

import net.theuniverscraft.MineGun.MineGun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BonusJump extends Bonus {
	public BonusJump(String name, Integer price, List<String> lore, Integer slot, ItemStack is, Integer level) {
		super(name, price, lore, slot, is, level);
	}
	
	public void onActive(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, m_level));
	}
	
	public void onReload(final Player player) {
		if(player.hasPotionEffect(PotionEffectType.JUMP))
			player.removePotionEffect(PotionEffectType.JUMP);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, m_level));
			}
		}, 3L);
	}
	
	public void onDelete(Player player) {
		if(player.hasPotionEffect(PotionEffectType.JUMP))
			player.removePotionEffect(PotionEffectType.JUMP);
	}
}
