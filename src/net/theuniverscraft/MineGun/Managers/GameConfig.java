package net.theuniverscraft.MineGun.Managers;

import net.theuniverscraft.MineGun.MineGun;

import org.bukkit.configuration.file.FileConfiguration;

public class GameConfig {
	/*public static final String BDD_HOST = "localhost";
	public static final Integer BDD_PORT = 3306;
	public static final String BDD_NAME = "minecraft";
	public static final String BDD_USER = "root";
	public static final String BDD_PASSWORD = "";
	public static final String BDD_PREFIX = "minegun_";
	
	public static final Integer SPAWN_POINTS_NUMBER = 12;*/
	
	public static String BDD_HOST = "localhost";
	public static Integer BDD_PORT = 3306;
	public static String BDD_NAME = "MineGun";
	public static String BDD_USER = "root";
	public static String BDD_PASSWORD = "5p3p28wq";
	public static String BDD_PREFIX = "minegun_";
	
	public static Integer SPAWN_POINTS_NUMBER = 9;
	public static boolean SOLO_MODE = true;
	
	public static void initConfig(MineGun plugin) {
		FileConfiguration config = plugin.getConfig();
		
		config.addDefault("mysql.host", BDD_HOST);
		config.addDefault("mysql.port", BDD_PORT);
		config.addDefault("mysql.name", BDD_NAME);
		config.addDefault("mysql.user", BDD_USER);
		config.addDefault("mysql.password", BDD_PASSWORD);
		config.addDefault("mysql.prefix", BDD_PREFIX);
		
		config.addDefault("spawn_points_number", SPAWN_POINTS_NUMBER);
		config.addDefault("solo_mode", SOLO_MODE);
		config.options().copyDefaults(true);
			
		plugin.saveConfig();
		
		BDD_HOST = config.getString("mysql.host");
		BDD_PORT = config.getInt("mysql.port");
		BDD_NAME = config.getString("mysql.name");
		BDD_USER = config.getString("mysql.user");
		BDD_PASSWORD = config.getString("mysql.password");
		BDD_PREFIX = config.getString("mysql.prefix");
		
		SPAWN_POINTS_NUMBER = config.getInt("spawn_points_number");
		SOLO_MODE = config.getBoolean("solo_mode");
	}
}
