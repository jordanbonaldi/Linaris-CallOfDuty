package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

public class SpeedManager {
	private List<Speeder> m_speeders = new LinkedList<Speeder>();
	
	private static SpeedManager instance = null;
	public static SpeedManager getInstance() {
		if(instance == null) instance = new SpeedManager();
		return instance;
	}
	
	private SpeedManager() {
		
	}
	
	public void addSpeeder(Player player, Integer level) {
		if(!playerHasSpeed(player)) m_speeders.add(new Speeder(player, level));
	}
	public void removeSpeeder(Player player) {
		for(int i = 0; i < m_speeders.size(); i++) {
			if(m_speeders.get(i).getPlayer().getName().equalsIgnoreCase(player.getName())) {
				m_speeders.remove(i);
				return;
			}
		}
	}
	
	public Boolean playerHasSpeed(Player player) {
		for(Speeder speeder : m_speeders) {
			if(speeder.getPlayer().getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public Speeder getSpeeder(Player player) {
		for(Speeder speeder : m_speeders) {
			if(speeder.getPlayer().getName().equalsIgnoreCase(player.getName())) {
				return speeder;
			}
		}
		return null;
	}
	
	public Long getPlayerTimeToRecharge(Player player) {
		if(getSpeeder(player) != null) {
			Speeder speeder = getSpeeder(player);
			if(speeder.getLevel() == 1) { return 2000L; }
			else if(speeder.getLevel() == 2) { return 1000L; }
			else if(speeder.getLevel() == 3) { return 0L; }
		}
		return 2500L;
	}
	
	public class Speeder {
		private Player m_player;
		private Integer m_level;
		
		private Speeder(Player player, Integer level) {
			m_player = player;
			m_level = level;
		}
		
		public Player getPlayer() { return m_player; }
		public Integer getLevel() { return m_level; }
	}
}
