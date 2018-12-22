package net.theuniverscraft.MineGun.Bonus;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Bonus {
	protected String m_name;
	protected Integer m_price;
	protected ItemStack m_is;
	protected Integer m_slot;
	protected Integer m_killToRemove;
	protected Integer m_level;
	
	public Bonus(String name, Integer price, List<String> lore, Integer slot, ItemStack is, Integer level) {
		m_name = name;
		m_price = price;
		m_is = is;
		m_slot = slot;
		ItemMeta im = is.getItemMeta();
		
		lore.add("");
		lore.add("&b"+price+" points !");
		for(int i = 0; i < lore.size(); i++) {
			lore.set(i, ChatColor.translateAlternateColorCodes('&', "&6"+lore.get(i)));
		}
		im.setLore(lore);
		
		is.setItemMeta(im);
		
		m_level = level;
		m_killToRemove = 3;
	}
	public Bonus setKillToRemove(Integer killToRemove) {
		m_killToRemove = killToRemove;
		return this;
	}
	
	public ItemStack getIs() { return m_is; }
	public String getName() { return ChatColor.translateAlternateColorCodes('&', m_name); }
	public Integer getPrice() { return m_price; }
	public Integer getSlot() { return m_slot; }
	public Integer getLevel() { return m_level; }
	
	public Integer getKillToRemove() { return m_killToRemove; }
	
	public void onActive(Player player) {} // A l'achat, à la reconnection
	public void onExist(Player player) {} // A l'achat si le joueur a deja le bonus
	public void onReload(final Player player) {} // Au respawn
	public void onDelete(Player player) {} // A la suppression
}
