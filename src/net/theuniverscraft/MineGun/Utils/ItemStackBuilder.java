package net.theuniverscraft.MineGun.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackBuilder {
	private ItemStack m_is;
	
	public ItemStackBuilder(Material m) {
		m_is = new ItemStack(m);
	}
	
	public ItemStack setData(short data) {
		m_is.setDurability(data);
		return m_is;
	}
}
