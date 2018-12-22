package net.theuniverscraft.MineGun.Background;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Managers.GameConfig;
import net.theuniverscraft.MineGun.Managers.RespawnTimerManager;
import net.theuniverscraft.MineGun.Managers.TeamManager;
import net.theuniverscraft.MineGun.Utils.Utils;

import org.bukkit.entity.Player;

public class BGPlayers {
	private static List<Player> m_players = new LinkedList<Player>();
	
	public static void addPlayers(Player player) {
		if(!GameConfig.SOLO_MODE) {
			TeamManager.getInstance().affectPlayerTeam(player);
		}
		m_players.add(player);
		player.setHealth(player.getMaxHealth());
		RespawnTimerManager.getInstance().addDead(player, true);
		Utils.setInv(player);
	}
	
	public static void addPlayersMassive(List<Player> players) {
		if(!GameConfig.SOLO_MODE) {
			TeamManager.getInstance().affectPlayerTeamMassive(players);
		}
		for(Player player : players) {
			m_players.add(player);
			player.setHealth(player.getMaxHealth());
			RespawnTimerManager.getInstance().addDead(player, true);
			Utils.setInv(player);
		}
	}
	
	public static void deletePlayer(Player player) {
		for(int i = 0; i < m_players.size(); i++) {
			if(m_players.get(i).getName().equals(player.getName())) {
				m_players.remove(i);
				return;
			}
		}
	}
	
	public static void clearPlayers() {
		m_players.clear();
	}
	
	public static boolean isPlayer(Player player) {
		for(int i = 0; i < m_players.size(); i++) {
			if(m_players.get(i).getName().equals(player.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static List<Player> getPlayers() {
		return m_players;
	}
}
