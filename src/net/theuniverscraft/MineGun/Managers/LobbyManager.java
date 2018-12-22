package net.theuniverscraft.MineGun.Managers;

import java.io.File;

import net.theuniverscraft.MineGun.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LobbyManager {
	private File file;
	private FileConfiguration fileSave;
	
	private Location m_lobby;
	
	private static LobbyManager instance = null;
	public static LobbyManager getInstance() {
		if(instance == null) instance = new LobbyManager();
		return instance;
	}
	
	private LobbyManager() {
		try {
			File dirs = new File("plugins/MineGun/saves");
			dirs.mkdirs();
			
			file = new File("plugins/MineGun/saves/lobby.yml");
			if(!file.exists()) file.createNewFile();
			fileSave = YamlConfiguration.loadConfiguration(file);
			
			
			if(fileSave.contains("lobby")) 
				m_lobby = Utils.getLocation(fileSave.getConfigurationSection("lobby"));
			else 
				m_lobby = Bukkit.getWorld("world").getSpawnLocation();
		}
		catch(Exception e) {}
	}
	
	public Location getLobby() { return m_lobby; }
	
	public void setLobby(Location lobby) {
		try {
			m_lobby = lobby;
			if(!fileSave.contains("lobby")) fileSave.createSection("lobby");
			Utils.setLocation(fileSave.getConfigurationSection("lobby"), m_lobby);
			fileSave.save(file);
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
