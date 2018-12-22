package net.theuniverscraft.MineGun.Utils;

import org.bukkit.entity.Player;

public class GunScore {
	private Player m_player;
	private int m_kill;
	
	public GunScore(Player player, int kill) {
		m_player = player;
		m_kill = kill;
	}
	
	public Player getPlayer() { return m_player; }
	public int getKill() { return m_kill; }
	public void addKill() { m_kill += 1; }
	public void addKill(int kill) { m_kill += kill; }
}