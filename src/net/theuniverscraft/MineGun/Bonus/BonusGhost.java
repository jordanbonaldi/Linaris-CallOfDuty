package net.theuniverscraft.MineGun.Bonus;

import java.util.List;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.GhostManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BonusGhost extends Bonus {
	protected Double m_addHeal;
	public BonusGhost(String name, Integer price, List<String> lore, Integer slot, ItemStack is, Double addHeal, Integer level) {
		super(name, price, lore, slot, is, level);
		m_addHeal = addHeal;
	}
	
	public void onActive(Player player) {
		GhostManager.getInstance().addPlayer(player);
		
		if(m_addHeal > 0) {
			player.setMaxHealth(player.getMaxHealth()+m_addHeal);
			player.setHealth(player.getMaxHealth());
		}
	}
	
	public void onReload(final Player player) {
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY))
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
			}
		}, 3L);
	}
	
	public void onDelete(Player player) {
		GhostManager.getInstance().removePlayer(player);
		if(m_addHeal > 0) player.setMaxHealth(player.getMaxHealth()-m_addHeal);
	}
}
