package net.theuniverscraft.MineGun.Bonus;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ButtonSlot {
	protected String m_name;
	protected ItemStack m_is;
	protected Integer m_slot;
	
	public ButtonSlot(String name, List<String> lore, Integer slot, ItemStack is) {
		m_name = name;
		m_is = is;
		m_slot = slot;
		
		ItemMeta im = m_is.getItemMeta();
		for(int i = 0; i < lore.size(); i++) {
			lore.set(i, ChatColor.translateAlternateColorCodes('&', "&6"+lore.get(i)));
		}
		im.setLore(lore);
		m_is.setItemMeta(im);
	}
	
	public String getName() { return ChatColor.translateAlternateColorCodes('&', m_name); }
	public ItemStack getIs() { return m_is; }
	public Integer getSlot() { return m_slot; }
	
	public void onClick(final Player player) {}
}
