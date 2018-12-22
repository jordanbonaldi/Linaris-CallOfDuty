package net.theuniverscraft.MineGun.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class TucScoreboard {
	private static Scoreboard m_board;
	
	public static Scoreboard getScoreBoard() {
		if(m_board == null) {
			m_board = Bukkit.getScoreboardManager().getNewScoreboard();
		}
		return m_board;
	}
	
	public static void setPlayerScoreBoard(Player player) {
		if(m_board == null) {
			m_board = Bukkit.getScoreboardManager().getNewScoreboard();
		}
		
		player.setScoreboard(m_board);
	}
}
