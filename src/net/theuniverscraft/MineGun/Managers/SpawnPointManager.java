package net.theuniverscraft.MineGun.Managers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.theuniverscraft.MineGun.Utils.Utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpawnPointManager {
	private File file;
	private FileConfiguration fileSave;
	
	private List<SpawnPoint> m_spawnPoints = new LinkedList<SpawnPoint>();
	
	private static SpawnPointManager instance = null;
	public static SpawnPointManager getInstance() {
		if(instance == null) instance = new SpawnPointManager();
		return instance;
	}
	
	private SpawnPointManager() {
		try {
			File dirs = new File("plugins/MineGun/saves");
			dirs.mkdirs();
			
			file = new File("plugins/MineGun/saves/spawnpoints.yml");
			if(!file.exists()) file.createNewFile();
			fileSave = YamlConfiguration.loadConfiguration(file);
			
			for(int i = 0; i < GameConfig.SPAWN_POINTS_NUMBER; i++) {
				Location loc = Utils.getLocation(fileSave.getConfigurationSection("zones.zone"+i));
				if(loc != null) m_spawnPoints.add(new SpawnPoint(i, loc));
				else m_spawnPoints.add(new SpawnPoint(i));
			}
			
			fileSave.save(file);
		}
		catch(Exception e) {}
	}
	
	public SpawnPoint getRandomSpawnPoint() {
		List<Integer> takenPoints = RespawnTimerManager.getInstance().getSpawnPointsTaken();
		List<SpawnPoint> pointsLibre = new LinkedList<SpawnPoint>();
		
		for(SpawnPoint sp : m_spawnPoints) {
			if(!takenPoints.contains(sp.getId())) pointsLibre.add(sp);
		}
		
		if(pointsLibre.isEmpty()) { // Aucun point libre
			Random rand = new Random();
			Integer pointId = rand.nextInt(m_spawnPoints.size());
			
			return m_spawnPoints.get(pointId);
		}
		else {
			Random rand = new Random();
			Integer pointId = rand.nextInt(pointsLibre.size());
			
			return pointsLibre.get(pointId);
		}
	}
	
	public Boolean allSpawnPointIsDefined() {
		for(int i = 0; i < GameConfig.SPAWN_POINTS_NUMBER; i++) {
			if(!m_spawnPoints.get(i).hasLocation()) {
				return false;
			}
		}
		return true;
	}
	
	public void saveZone(Integer id, Location point) {
		String nameSection = "zones.zone"+id;
		if(!fileSave.contains(nameSection)) fileSave.createSection(nameSection);
		Utils.setLocation(fileSave.getConfigurationSection(nameSection), point);
		
		for(int i = 0; i < GameConfig.SPAWN_POINTS_NUMBER; i++) {
			if(m_spawnPoints.get(i).getId() == id) {
				m_spawnPoints.get(i).setLocation(point);
			}
		}
		
		try { fileSave.save(file); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public class SpawnPoint {
		private Integer m_id;
		private Location m_location;
		
		private SpawnPoint(Integer id, Location location) {
			m_id = id;
			m_location = location;
		}
		private SpawnPoint(Integer id) {
			this(id, null);
		}
		
		public void setLocation(Location location) { m_location = location; }
		public boolean hasLocation() { return m_location != null; }
		public Location getLocation() { return m_location; }
		
		public Integer getId() { return m_id; }
	}
}
