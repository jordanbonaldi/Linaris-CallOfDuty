package net.theuniverscraft.MineGun.Managers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;


public class GhostManager {
	private Team team;
	
	private static GhostManager instance = null;
	public static GhostManager getInstance() {
		if(instance == null) instance = new GhostManager();
		return instance;
	}
	
	private GhostManager() {
		team = TucScoreboard.getScoreBoard().registerNewTeam("minegun");
		team.setCanSeeFriendlyInvisibles(GameConfig.SOLO_MODE);
	}
		
	public void addPlayer(Player player) {
		if(!GameConfig.SOLO_MODE) return;
		
		if(!player.hasPotionEffect(PotionEffectType.INVISIBILITY))
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
	}
	public void removePlayer(Player player) {
		if(!GameConfig.SOLO_MODE) return;
		
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY))
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
	}
	
	public void addPlayerInTeam(Player player) {
		if(!GameConfig.SOLO_MODE) return;
		
		team.addPlayer(player);
		TucScoreboard.setPlayerScoreBoard(player);
	}
	public void removePlayerInTeam(Player player) {
		if(!GameConfig.SOLO_MODE) return;
		
		if(team.hasPlayer(player)) {
			team.removePlayer(player);
		}
	}
}