package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Background.BGChat;
import net.theuniverscraft.MineGun.Managers.TeamManager.GunTeam;
import net.theuniverscraft.MineGun.Utils.GunScore;
import net.theuniverscraft.MineGun.Utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ScoreManager {
	private List<GunScore> m_playersScores = new LinkedList<GunScore>();
	
	private static ScoreManager instance = null;
	public static ScoreManager getInstance() {
		if(instance == null) instance = new ScoreManager();
		return instance;
	}
	
	private ScoreManager() {
		
	}
	
	public void addKill(Player player) {
		for(GunScore score : m_playersScores) {
			if(score.getPlayer().getName().equals(player.getName())) {
				score.addKill();
				return;
			}
		}
		m_playersScores.add(new GunScore(player, 1));
	}
	
	public void showWin() {
		int i = 1;
		if(!GameConfig.SOLO_MODE) {
			BGChat.sendAll("");
			TeamManager.getInstance().showScoresTeams();
		}
		
		BGChat.sendAll("");
		if(GameConfig.SOLO_MODE) {
			GunScore[] scores = getScores();
			if(scores.length == 0) {
				BGChat.sendAll(Translation.getString("NO_WIN"));
				return;
			}
			BGChat.sendAll(Translation.getString("GAME_END_SOLO").replace("<player>", scores[0].getPlayer().getName()));
		}
		else {
			GunTeam winner = TeamManager.getInstance().getWinner();
			String winTeam = ChatColor.AQUA + ChatColor.stripColor(winner.getNamePlur());
			BGChat.sendAll(Translation.getString("GAME_END_TEAM").replace("<team>", winTeam));
		}
		BGChat.sendAll("");
		
		BGChat.sendAll(ChatColor.AQUA+"------------ "+ChatColor.GOLD+"Classement"+ChatColor.AQUA+" ------------");
		i = 1;
		for(GunScore score : getScores()) {
			BGChat.sendAll(i+". "+score.getPlayer().getName()+" : "+score.getKill()+" kills");
			
			if(i >= 5) break;
			
			i++;
		}
		BGChat.sendAll(ChatColor.AQUA+"-----------------------------------");
		BGChat.sendAll("");
	}
	
	public GunScore[] getScores() {
		return Utils.scoreDesc(m_playersScores.toArray(new GunScore[m_playersScores.size()]));
	}
	
	public List<GunScore> getListScore() { return m_playersScores; }
	
	public void clear() {
		m_playersScores.clear();
	}
}
