package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Weapons.Utils.PlayerRadar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RadarTimerManager {
	private List<PlayerRadar> m_radars = new LinkedList<PlayerRadar>();
	
	private static RadarTimerManager instance = null;
	public static RadarTimerManager getInstance() {
		if(instance == null) instance = new RadarTimerManager();
		return instance;
	}
	
	public RadarTimerManager() {		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MineGun.getPluginInstance(), new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for(int i = 0; i < m_radars.size(); i++) {
					if(!m_radars.get(i).update()) {
						// C'est fini on retir la bousole
						m_radars.get(i).getPlayer().getInventory().setItem(8, null);
						m_radars.get(i).getPlayer().updateInventory();
						m_radars.remove(i);
						i--;
					}
				}
			}
		}, 20L, 20L);
	}
	
	public void addRadar(PlayerRadar radar) {
		m_radars.add(radar);
	}
	
	public void deleteRadar(Player player) {
		for(int i = 0; i < m_radars.size(); i++) {
			if(m_radars.get(i).getPlayer().getName().equals(player.getName())) {
				m_radars.remove(i);
				return;
			}
		}
	}
	
	public PlayerRadar getRadar(Player player) {
		for(PlayerRadar playerRadar : m_radars) {
			if(playerRadar.getPlayer().getName().equals(player.getName())) {
				return playerRadar;
			}
		}
		return null;
	}
}
