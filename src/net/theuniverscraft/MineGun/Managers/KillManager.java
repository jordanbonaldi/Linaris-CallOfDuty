package net.theuniverscraft.MineGun.Managers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Archievements.PlayerAchievement;
import net.theuniverscraft.MineGun.Events.PlayerBonusKillEvent;
import net.theuniverscraft.MineGun.Events.PlayerMultiKillEvent;
import net.theuniverscraft.MineGun.Managers.RespawnTimerManager.Respawner;
import net.theuniverscraft.MineGun.Utils.ComboKill;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class KillManager implements Listener {
	private HashMap<String, Integer> m_playerKillSameLife = new HashMap<String, Integer>();
	private List<MultiKiller> m_playerLastKill = new LinkedList<MultiKiller>();
	
	private static KillManager instance = null;
	
	
	public static KillManager getInstance() {
		if(instance == null) instance = new KillManager();
		return instance;
	}
	
	public KillManager() {
		
	}
	
	public Integer getKill(String player) {
		if(m_playerKillSameLife.containsKey(player)) {
			return m_playerKillSameLife.get(player);
		}
		return 0;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		
		Player dead = event.getEntity();
		
		if(m_playerKillSameLife.containsKey(dead.getName()))
			m_playerKillSameLife.remove(dead.getName());
		
		BonusManager.getInstance().playerDeath(dead);
		RadarTimerManager.getInstance().deleteRadar(dead);
		
		if(dead.getLastDamageCause() instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) dead.getLastDamageCause();
			if(nEvent.getDamager() instanceof Player || nEvent.getDamager() instanceof Projectile || 
					nEvent.getDamager() instanceof TNTPrimed)
			{
				Player killer = null;
				Weapon weapon = null;
				if(nEvent.getDamager() instanceof Projectile) {
					Projectile projectile = (Projectile) nEvent.getDamager();
					killer = (Player) projectile.getShooter();
					weapon = (Weapon) Utils.getMetadataValue("weapon", projectile).value();
				}
				else if(nEvent.getDamager() instanceof TNTPrimed) {
					TNTPrimed tnt = (TNTPrimed) nEvent.getDamager();
					killer = (Player) Utils.getMetadataValue("owner", tnt).value();
					weapon = (Weapon) Utils.getMetadataValue("weapon", tnt).value();
				}
				else {
					killer = (Player) nEvent.getDamager();
					weapon = (Weapon) WeaponManager.getInstance().getWeapon(killer.getItemInHand().getType());
				}
				if(killer == null || killer.getName().equals(dead.getName())) return;
				
				PlayerAchievement pa = AchievementsManager.getInstance().getPlayerAchievement(killer);
				pa.onPlayerKill(weapon);
				
				if(m_playerKillSameLife.containsKey(killer.getName())) {
					Integer kills = m_playerKillSameLife.get(killer.getName());
					m_playerKillSameLife.remove(killer.getName());
					m_playerKillSameLife.put(killer.getName(), kills+1);
				}
				else {
					m_playerKillSameLife.put(killer.getName(), 1);
				}
				
				boolean addComboKiller = true;
				for(int i = 0; i < m_playerLastKill.size(); i++) {
					if(m_playerLastKill.get(i).equals(killer.getName())) {
						if(m_playerLastKill.get(i).getLastKill() + 7500 > System.currentTimeMillis()) {
							m_playerLastKill.get(i).addComboKill();
							m_playerLastKill.get(i).setLastKill();
							
							ComboKill comboKill = m_playerLastKill.get(i).getComboKill();
							if(comboKill != null) {
								Bukkit.getPluginManager().callEvent(new PlayerMultiKillEvent(pa, comboKill));
							}
						}
						else {
							m_playerLastKill.remove(i);
							break;
						}
						addComboKiller = false;
					}
				}
				
				if(addComboKiller) {
					m_playerLastKill.add(new MultiKiller(killer, System.currentTimeMillis(), 1));
				}
				
				PlayersManager.getInstance().addPlayerPoints(killer, 5);
				ScoreManager.getInstance().addKill(killer);
				
				Utils.updateInterface(killer);
				
				Integer kills = m_playerKillSameLife.get(killer.getName());
				Bukkit.getPluginManager().callEvent(new PlayerBonusKillEvent(killer, kills));
				if(kills > 8) m_playerKillSameLife.remove(killer.getName());
				
				// Actualiser les achievements
				Utils.reloadAchievement(killer);
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		try {
			if(PlayersManager.getInstance().playerIsVip(event.getPlayer())) {
				// VIP respawn direct
				event.setRespawnLocation(SpawnPointManager.getInstance().getRandomSpawnPoint().getLocation());
				event.getPlayer().sendMessage(Translation.getString("YOU_HAVE_RESPAWN"));
				BonusManager.getInstance().playerRespawn(event.getPlayer());
			}
			else {
				// Attente 10 secondes
				RespawnTimerManager.getInstance().addDead(event.getPlayer());
				Respawner respawner = RespawnTimerManager.getInstance().getRespawner(event.getPlayer());
				event.setRespawnLocation(respawner.getSpawnPoint().getLocation());
			}
		}
		catch(NullPointerException e) {
			// Erreur
			event.getPlayer().sendMessage(ChatColor.DARK_RED+"Fatal error ! (Exception)");
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntityType() == EntityType.WOLF) {
			Player killer = event.getEntity().getKiller();
			PlayersManager.getInstance().addPlayerPoints(killer, 10);
			Utils.updateInterface(killer);
		}
	}
	
	public void resetMultiKill(Player player) {
		for(int i = 0; i < m_playerLastKill.size(); i++) {
			if(m_playerLastKill.get(i).getPlayer().getName().equalsIgnoreCase(player.getName())) {
				m_playerLastKill.remove(i);
				return;
			}
		}
	}
	
	public class MultiKiller {
		private Player m_player;
		private Long m_lastKill;
		private Integer m_multiKill;
		
		public MultiKiller(Player player, Long lastKill, Integer multiKill) {
			m_player = player;
			m_lastKill = lastKill;
			m_multiKill = multiKill;
		}
		
		public Player getPlayer() { return m_player; }
		public Long getLastKill() { return m_lastKill; }
		public Integer getMultiKill() { return m_multiKill; }
		
		public void addComboKill() {
			m_multiKill++;
		}
		
		public void setLastKill() { m_lastKill = System.currentTimeMillis(); }
		
		public ComboKill getComboKill() {
			if(m_multiKill == 2) { return ComboKill.DOUBLE_KILL; }
			if(m_multiKill == 3) { return ComboKill.TRIPLE_KILL; }
			if(m_multiKill == 4) { return ComboKill.QUADRUPLE_KILL; }
			return null;
		}
		
		public boolean equals(Object object) {
			if(object == this) return true;
			
			if(object instanceof String) {
				String string = (String) object;
				if(string.equalsIgnoreCase(m_player.getName()))
					return true;
			}
			
			if(!(object instanceof MultiKiller)) return false;
			
			MultiKiller multiKiller = (MultiKiller) object;
			if(multiKiller.getPlayer().getName().equalsIgnoreCase(m_player.getName()))
				return true;
			
			return false;
		}
		
		public int hashCode() {
			int result = 7;
			final int multiplier = 17;
			
			result = multiplier*result + m_player.getName().hashCode();
			
			return result;
		}
	}
}
