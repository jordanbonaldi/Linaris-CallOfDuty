package net.theuniverscraft.MineGun.Bonus;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HealBonus extends Bonus {
	protected Double m_addHeal;
	
	public HealBonus(String name, Integer price, List<String> lore, Integer slot, ItemStack is, Double addHeal, Integer level) {
		super(name, price, lore, slot, is, level);
		m_addHeal = addHeal;
	}
	
	public Double getHealAdded() {
		return m_addHeal;
	}
	
	public void onActive(Player player) {
		player.setMaxHealth(player.getMaxHealth() + m_addHeal);
		player.setHealth(player.getHealth() + m_addHeal);
	}
	public void onExist(Player player) {
		Double life = player.getHealth() + m_addHeal;
		player.setHealth(life > player.getMaxHealth() ? player.getMaxHealth() : life);
	}
	
	public void onDelete(Player player) {
		player.setMaxHealth(player.getMaxHealth()-m_addHeal);
	}
}
