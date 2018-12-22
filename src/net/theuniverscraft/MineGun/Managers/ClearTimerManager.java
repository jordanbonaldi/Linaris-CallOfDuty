package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.MineGun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClearTimerManager {
	private List<Player> m_toClear = new LinkedList<Player>();
	
	private static ClearTimerManager instance = null;
	public static ClearTimerManager getInstance() {
		if(instance == null) instance = new ClearTimerManager();
		return instance;
	}
	
	public ClearTimerManager() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MineGun.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < m_toClear.size(); i++) {
					Player player = m_toClear.get(i);
					if(player == null) {
						m_toClear.remove(i);
						i--;
					}
					else {
						Inventory inv = player.getInventory();
						for(int j = 0; j < inv.getSize(); j++) {
							ItemStack is = inv.getItem(j);
							if(is != null) {
								if(!is.getItemMeta().hasDisplayName()) {
									inv.setItem(j, null);
								}
								else if(is.getItemMeta().getDisplayName().contains("Muno"))
								{
									inv.setItem(j, null);
								}
							}
						}
						m_toClear.remove(i);
						i--;
					}
				}
			}
		}, 20L, 20L);
	}
	
	public void clearPlayer(Player player) {
		for(Player p : m_toClear) {
			if(p.getName().equals(player.getName())) return;
		}
		m_toClear.add(player);
	}
}
