package net.theuniverscraft.MineGun.Managers;

import net.theuniverscraft.MineGun.MineGun;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationManager {
	private Boolean m_isInConfig;
	private FileConfiguration config;
	
	private static ConfigurationManager instance = null;
	public static ConfigurationManager getInstance() {
		if(instance == null) instance = new ConfigurationManager();
		return instance;
	}
	
	private ConfigurationManager() {
		config = MineGun.getPluginInstance().getConfig();
		
		if(config.contains("isInConfig")) {
			m_isInConfig = config.getBoolean("isInConfig") && !SpawnPointManager.getInstance().allSpawnPointIsDefined();
		}
		else {
			m_isInConfig = !SpawnPointManager.getInstance().allSpawnPointIsDefined();
		}
	}
	
	public Boolean setIsInConfig(Boolean f_isInConfig) {
		if(config.contains("isInConfig") && f_isInConfig == config.getBoolean("isInConfig")) {
			return true;
		}
		
		if(f_isInConfig) {
			// on met le mode config
			config.set("isInConfig", true);
			MineGun.getPluginInstance().saveConfig();
			Bukkit.reload();
			return true;
		}
		else if(!f_isInConfig && SpawnPointManager.getInstance().allSpawnPointIsDefined()) {
			// on enleve le mode config
			config.set("isInConfig", false);
			MineGun.getPluginInstance().saveConfig();
			Bukkit.reload();
			return true;
		}
		return false;
	}
	
	public Boolean isInConfig() { return m_isInConfig; }
}
