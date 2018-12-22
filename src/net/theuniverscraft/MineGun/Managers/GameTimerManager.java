package net.theuniverscraft.MineGun.Managers;

import net.theuniverscraft.MineGun.Game;
import net.theuniverscraft.MineGun.GameStatus;
import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Background.BGChat;
import net.theuniverscraft.MineGun.Background.BGPlayers;
import net.theuniverscraft.MineGun.Listeners.MotdSetterListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameTimerManager {
	private Long m_timer = 301L;
	
	private static GameTimerManager instance = null;
	public static GameTimerManager getInstance() {
		if(instance == null) instance = new GameTimerManager();
		return instance;
	}
	
	public GameTimerManager() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MineGun.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				if(Game.getInstance().getStatus() == GameStatus.LOBBY) {
					if(m_timer <= 0) { // Fini
						int playerNb = Bukkit.getOnlinePlayers().length;
						
						if(playerNb <= 1) {
							BGChat.sendAll(Translation.getString("NOT_ANY_PEOPLE"));
							m_timer = 301L;
						}
						else {
							BGChat.sendAll("");
							BGChat.sendAll(ChatColor.AQUA+"-----------------------------------------");
							BGChat.sendAll("");
							if(GameConfig.SOLO_MODE) BGChat.sendAll(ChatColor.GOLD+"Mélée générale, chacun pour soit");
							else BGChat.sendAll(ChatColor.GOLD+"Match à mort par équipe");
							BGChat.sendAll("");
							BGChat.sendAll(ChatColor.AQUA+"-----------------------------------------");
							BGChat.sendAll("");
							
							BGChat.sendAll(Translation.getString("GAME_START"));
							BGChat.sendAll("");
							
							m_timer = 600L;
							Game.getInstance().setStatus(GameStatus.GAME);
						}
					}
					else if((m_timer % 60D) == 0) { // Minutes plaines
						Long x = m_timer / 60L;
						String unite = x <= 1 ? "minute" : "minutes";
						
						if(x > 0) {
							BGChat.sendAll(Translation.getString("START_IN_PLAIN").replaceAll("<x>", x.toString())
									.replaceAll("<unite>", unite));
							MotdSetterListener.getInstance().setMotd(Translation.getString("GAME_STARTED_IN_MODT_PLAIN_UNITE")
									.replaceAll("<m>", x.toString()));
						}
					}
					else if((m_timer % 60D) == 30) { // Minutes et demi
						Long m = (m_timer - 30) / 60L;
						String unite_m = m <= 1 ? "minute" : "minutes";
						
						Long s = m_timer - m*60;
						String unite_s = s <= 1 ? "seconde" : "secondes";
						
						if(m >= 1) {
							BGChat.sendAll(Translation.getString("START_IN_NOT_PLAIN").replaceAll("<m>", m.toString())
									.replaceAll("<unite_m>", unite_m).replaceAll("<s>", s.toString())
									.replaceAll("<unite_s>", unite_s));
							MotdSetterListener.getInstance().setMotd(Translation.getString("GAME_STARTED_IN_MODT_NOT_PLAIN_UNITE")
									.replaceAll("<m>", m.toString()).replaceAll("<s>", s.toString()));
						}
						else {
							BGChat.sendAll(Translation.getString("START_IN_PLAIN").replaceAll("<x>", s.toString())
									.replaceAll("<unite>", unite_s));
						}
					}
					else if(m_timer <= 15 && m_timer > 0) { // Moins de 15 secondes
						Long x = m_timer;
						String unite = x == 1 ? "seconde" : "secondes";
						
						BGChat.sendAll(Translation.getString("START_IN_PLAIN").replaceAll("<x>", x.toString())
								.replaceAll("<unite>", unite));
					}
				}
				else if(Game.getInstance().getStatus() == GameStatus.GAME) { // game
					if(m_timer <= 0) { // Fini
						ScoreManager.getInstance().showWin();
						ScoreManager.getInstance().clear();
						RespawnTimerManager.getInstance().clear();
						m_timer = 10L;
						Game.getInstance().setStatus(GameStatus.END_GAME);
					}
					else if((m_timer % 60D) == 0) { // Minutes plaines
						Long x = m_timer / 60L;
						String unite = x <= 1 ? "minute" : "minutes";
						
						//if(x == 1 || x == 5) {
							BGChat.sendAll(Translation.getString("END_IN_PLAIN").replaceAll("<x>", x.toString())
									.replaceAll("<unite>", unite));
							
							MotdSetterListener.getInstance().setMotd(Translation.getString("GAME_ENDED_IN_MODT_PLAIN_UNITE")
									.replaceAll("<m>", x.toString()));
						// }
					}
					else if((m_timer % 60D) == 30) { // Minutes et demi
						Long m = (m_timer - 30) / 60L;
						
						Long s = m_timer - m*60;
						String unite_s = s <= 1 ? "seconde" : "secondes";
						
						if(m == 0) {
							BGChat.sendAll(Translation.getString("END_IN_PLAIN").replaceAll("<x>", s.toString())
									.replaceAll("<unite>", unite_s));
							
							MotdSetterListener.getInstance().setMotd(Translation.getString("GAME_STARTED_IN_MODT_NOT_PLAIN_UNITE")
									.replaceAll("<m>", m.toString()).replaceAll("<s>", s.toString()));
						}
					}
					else if(m_timer <= 15 && m_timer > 0) { // Moins de 15 secondes
						Long x = m_timer;
						String unite = x == 1 ? "seconde" : "secondes";
						
						BGChat.sendAll(Translation.getString("END_IN_PLAIN").replaceAll("<x>", x.toString())
								.replaceAll("<unite>", unite));
					}
				}
				else if(Game.getInstance().getStatus() == GameStatus.END_GAME) {
					if(m_timer <= 0) { // Fini
						for(Player player : Bukkit.getOnlinePlayers()) {				
							player.kickPlayer(Translation.getString("KICK_END_GAME"));
						}
						BGPlayers.clearPlayers();
						
						m_timer = 301L;
						Game.getInstance().setStatus(GameStatus.LOBBY);
					}
					else {
						MotdSetterListener.getInstance().setMotd(Translation.getString("GAME_END_MOTD"));
					}
				}
				m_timer --;
			}
		}, 20L, 20L);
	}
	
	public void shortTimer() {
		m_timer = 15L;
	}
}
