package net.theuniverscraft.MineGun.Managers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Weapons.Utils.PlayerGun;
import net.theuniverscraft.MineGun.Weapons.Weapons.GunWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayersManager {
	private List<String> m_playerVip = new LinkedList<String>();
	private HashMap<String, Integer> m_playersPoints = new HashMap<String, Integer>();
	private HashMap<String, LinkedList<PlayerGun>> m_playersGuns = new HashMap<String, LinkedList<PlayerGun>>();
	
	private static PlayersManager instance = null;
	public static PlayersManager getInstance() {
		if(instance == null) instance = new PlayersManager();
		return instance;
	}
	
	public PlayersManager() {
		m_playersGuns.clear();
		for(Player player : Bukkit.getOnlinePlayers()) { // Tout les joueurs ont une liste de tous les guns
			this.initPlayer(player);
		}
	}
	
	public PlayerGun getPlayerGun(Player player, GunWeapon gun) {
		if(m_playersGuns.containsKey(player.getName())) {
			for(PlayerGun pg : m_playersGuns.get(player.getName())) {
				if(pg.getGun().equals(gun)) {
					return pg;
				}
			}
		}
		return null;
	}
	
	public LinkedList<PlayerGun> getPlayerGuns(Player player) {
		if(m_playersGuns.containsKey(player.getName())) {
			return m_playersGuns.get(player.getName());
		}
		return null;
	}
	
	public void initPlayer(Player player)  {
		if(m_playersGuns.containsKey(player.getName())) m_playersGuns.remove(player.getName());
		LinkedList<PlayerGun> playerGuns = new LinkedList<PlayerGun>();
		
		for(Weapon weapon : WeaponManager.getInstance().getListWeapon()) {
			if(weapon instanceof GunWeapon) {
				playerGuns.add(new PlayerGun(player, (GunWeapon) weapon));
			}
		}
		
		m_playersGuns.put(player.getName(), playerGuns);
	}
	
	public void savePlayer(Player player)  {
		if(m_playersGuns.containsKey(player.getName())) {
			m_playersGuns.remove(player.getName());
		}
	}
	
	// Gestion des VIP
	public void addVip(Player player) {
		if(!m_playerVip.contains(player.getName())) m_playerVip.add(player.getName());
	}
	public void removeVip(Player player) {
		if(m_playerVip.contains(player.getName())) m_playerVip.remove(player.getName());
	}
	public boolean playerIsVip(Player player) {
		return m_playerVip.contains(player.getName());
	}
	
	
	// Gestion des Points
	public void setPlayerPoints(Player player, Integer points) {
		m_playersPoints.put(player.getName(), points);
	}
	public void addPlayerPoints(Player player, Integer points) {
		m_playersPoints.put(player.getName(), getPlayerPoints(player) + points);
	}
	public void subPlayerPoints(Player player, Integer points) {
		m_playersPoints.put(player.getName(), getPlayerPoints(player) - points);
	}
	public Integer getPlayerPoints(Player player) {
		return m_playersPoints.containsKey(player.getName()) ? m_playersPoints.get(player.getName()) : 0;
	}
}
