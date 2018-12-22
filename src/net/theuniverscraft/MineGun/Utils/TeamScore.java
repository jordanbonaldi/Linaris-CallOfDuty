package net.theuniverscraft.MineGun.Utils;

import net.theuniverscraft.MineGun.Managers.TeamManager.GunTeam;

public class TeamScore {
	private GunTeam m_team;
	private int m_kill;
	
	public TeamScore(GunTeam team, int kill) {
		m_team = team;
		m_kill = kill;
	}
	
	public GunTeam getTeam() { return m_team; }
	public int getKill() { return m_kill; }
	public void addKill() { m_kill += 1; }
	public void addKill(int kill) { m_kill += kill; }
}