package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

public class ForceManager {
	private List<Forcer> m_forcers = new LinkedList<Forcer>();
	
	private static ForceManager instance = null;
	public static ForceManager getInstance() {
		if(instance == null) instance = new ForceManager();
		return instance;
	}
	
	public ForceManager() {}
	
	public void addForcer(Player player, Integer level) {
		if(!playerHasForce(player)) m_forcers.add(new Forcer(player, level));
	}
	public void removeForcer(Player player) {
		for(int i = 0; i < m_forcers.size(); i++) {
			if(m_forcers.get(i).getPlayer().getName().equalsIgnoreCase(player.getName())) {
				m_forcers.remove(i);
				return;
			}
		}
	}
	public Boolean playerHasForce(Player player) {
		for(Forcer forcer : m_forcers) {
			if(forcer.getPlayer().getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public Forcer getForcer(Player player) {
		for(Forcer forcer : m_forcers) {
			if(forcer.getPlayer().getName().equalsIgnoreCase(player.getName())) {
				return forcer;
			}
		}
		return null;
	}
	
	public Double getDamage(Player player, Double degat) {
		try {
			Forcer forcer = getForcer(player);
			Double coef = (forcer.getLevel() / 10D) + 1;
			return degat*coef;
		}
		catch(NullPointerException e) {}
		return degat;
	}
	
	public Double multiplierLaunch(Player player, Double multiplier) {
		try {
			Forcer forcer = getForcer(player);
			Double coef = ((forcer.getLevel()+1) / 10D) + 1;
			return multiplier*coef;
		}
		catch(NullPointerException e) {}
		return multiplier;
	}
	
	public Boolean getKnifeOnShot(Player player) {
		Random rand = new Random();
		try {
			Forcer forcer = getForcer(player);
			Integer tirage = rand.nextInt(5);
			// BGChat.sendAll("level = "+forcer.getLevel()+" ; tirage = "+tirage);
			if(forcer.getLevel() == 1) return tirage == 0 || tirage == 1;
			else if(forcer.getLevel() == 2) return tirage == 0 || tirage == 1 || tirage == 2;
			else if(forcer.getLevel() == 3) return tirage == 0 || tirage == 1 || tirage == 2 || tirage == 3;
		}
		catch(NullPointerException e) {}
		return rand.nextInt(5) == 0;
	}
	
	public class Forcer {
		private Player m_player;
		private Integer m_level;
		
		private Forcer(Player player, Integer level) {
			m_player = player;
			m_level = level;
		}
		
		public Player getPlayer() { return m_player; }
		public Integer getLevel() { return m_level; }
	}
}
