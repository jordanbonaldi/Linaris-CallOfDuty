package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Background.BGPlayers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class InvisibilityManager implements Listener {
	private List<Player> m_invisible = new LinkedList<Player>();
	
	private static InvisibilityManager instance;
	public static InvisibilityManager getInstance() {
		if(instance == null) instance = new InvisibilityManager();
		return instance;
	}
	
	private InvisibilityManager() {
		Bukkit.getPluginManager().registerEvents(this, MineGun.getPluginInstance());
	}
	
	public void addInvisible(Player player) {
		if(!m_invisible.contains(player)) m_invisible.add(player);
		for(Player gamer : BGPlayers.getPlayers()) {
			gamer.hidePlayer(player);
		}
	}
	
	public void removeInvisible(Player player) {
		if(m_invisible.contains(player)) m_invisible.remove(player);
		for(Player gamer : BGPlayers.getPlayers()) {
			gamer.showPlayer(player);
		}
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for(Player player : m_invisible) {
			event.getPlayer().hidePlayer(player);
		}
	}
}
