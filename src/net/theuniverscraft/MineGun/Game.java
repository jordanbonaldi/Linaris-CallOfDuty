package net.theuniverscraft.MineGun;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Background.BGPlayers;
import net.theuniverscraft.MineGun.Managers.DbManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class Game {
	private GameStatus m_status;
	
	private static Game instance = null;
	public static Game getInstance() {
		if(instance == null) instance = new Game();
		return instance;
	}
	
	public Game() {
		m_status = GameStatus.LOBBY;
	}
	
	public void setStatus(GameStatus status) {
		if(m_status == status) return;
		
		m_status = status;
		if(m_status == GameStatus.GAME) {
			List<Player> players = new LinkedList<Player>();
			for(Player player : Bukkit.getOnlinePlayers()) {
				DbManager.getInstance().restoreForTheGame(player);
				players.add(player);
			}
			BGPlayers.addPlayersMassive(players);
		}
		else if(m_status == GameStatus.LOBBY) {
			World world = Bukkit.getWorld("world");
			
			for(Entity entity : world.getEntities()) {
				if(!(entity instanceof Player)) {
					entity.remove();
				}
			}
		}
	}
	
	public GameStatus getStatus() { return m_status; }
}
