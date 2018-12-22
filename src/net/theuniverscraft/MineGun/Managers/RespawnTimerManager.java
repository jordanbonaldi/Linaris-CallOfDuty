package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.SpawnPointManager.SpawnPoint;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RespawnTimerManager {
	private List<Respawner> m_respawners = new LinkedList<Respawner>();
	
	private static RespawnTimerManager instance = null;
	public static RespawnTimerManager getInstance() {
		if(instance == null) instance = new RespawnTimerManager();
		return instance;
	}
	
	public RespawnTimerManager() {		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MineGun.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < m_respawners.size(); i++) {
					try {
						m_respawners.get(i).decrementeTime();
						
						final Player player = m_respawners.get(i).getPlayer();
						if(m_respawners.get(i).timeIsFinish()) {
							player.removePotionEffect(PotionEffectType.INVISIBILITY);
														
							player.sendMessage(Translation.getString("YOU_HAVE_RESPAWN"));
							BonusManager.getInstance().playerRespawn(player);
							m_respawners.remove(i);
							
							i--;
						}
						else {
							if(m_respawners.get(i).getTime() == 1) {
								// Une seconde avant la fin pour être actualisé par tout les joueurs
								InvisibilityManager.getInstance().removeInvisible(player);
							}
							Respawner respawner = m_respawners.get(i);
							
							String unite = respawner.getTime() <= 1 ? "seconde" : "secondes";
							respawner.getPlayer().sendMessage(Translation.getString("YOU_RESPAWN_IN")
									.replaceAll("<x>", respawner.getTime().toString()).replaceAll("<unite>", unite));
						}
					}
					catch(NullPointerException e) { e.printStackTrace(); }
				}
			}
		}, 20L, 20L);
	}
	
	public void addDead(final Player player) { addDead(player, false); }
	public void addDead(final Player player, Boolean teleport) {
		for(PotionEffect potion : player.getActivePotionEffects()) {
			player.removePotionEffect(potion.getType());
		}
		
		SpawnPoint sp = SpawnPointManager.getInstance().getRandomSpawnPoint();
		InvisibilityManager.getInstance().addInvisible(player);
		m_respawners.add(new Respawner(player, sp, 11L));
		
		if(teleport) {
			player.teleport(sp.getLocation());
		}
	}
	
	public Respawner getRespawner(Player player) {
		for(Respawner respawner : m_respawners) {
			if(respawner.getPlayer().getName().equalsIgnoreCase(player.getName())) {
				return respawner;
			}
		}
		return null;
	}
	
	public List<Integer> getSpawnPointsTaken() {
		List<Integer> list = new LinkedList<Integer>();
		
		for(Respawner res : m_respawners) {
			list.add(res.getSpawnPoint().getId());
		}
		
		return list;
	}
	
	public void clear() {
		m_respawners.clear();
	}
	
	public void clearRespawner(Player player) {		
		for(int i = 0; i < m_respawners.size(); i++) {
			if(m_respawners.get(i).getPlayer().getName().equals(player.getName())) {
				m_respawners.remove(i);
				return;
			}
		}
	}
	
	public class Respawner {
		private Player m_player;
		private SpawnPoint m_spawnPoint;
		private Long m_time;
		private Location m_lastLoc;
		
		private Respawner(Player player, SpawnPoint spawnPoint, Long time) {
			m_player = player;
			m_spawnPoint = spawnPoint;
			m_time = time;
			m_lastLoc = player.getLocation();
		}
		
		public void decrementeTime() { m_time--; }
		public Long getTime() { return m_time; }
		public Boolean timeIsFinish() { return m_time <= 0 ? true : false; }
		
		public Player getPlayer() { return m_player; }
		public SpawnPoint getSpawnPoint() { return m_spawnPoint; }
		
		public void setLastLocation(Location lastLoc) { m_lastLoc = lastLoc; }
		public Location getLastLocation() { return m_lastLoc; }
	}
}
