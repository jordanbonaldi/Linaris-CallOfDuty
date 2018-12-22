package net.theuniverscraft.MineGun.Archievements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Background.BGChat;
import net.theuniverscraft.MineGun.Managers.AchievementsManager;
import net.theuniverscraft.MineGun.Managers.KitManager;
import net.theuniverscraft.MineGun.Managers.KitManager.Kit;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerAchievement {
	protected Player m_player;
	protected Achievement m_achievement;
	
	protected Integer m_kills = 0;
	protected Integer m_doubleKills = 0;
	protected Integer m_tripleKills = 0;
	protected Integer m_quadrupleKills = 0;
	
	protected HashMap<Weapon, Integer> m_weaponKill = new HashMap<Weapon, Integer>();
	protected HashMap<Kit, Integer> m_kitKill = new HashMap<Kit, Integer>();
	
	protected boolean isEnded = false;
	
	public PlayerAchievement(Player player, Achievement achievement) {
		m_player = player;
		m_achievement = achievement;
	}
	
	public void update() {
		if(m_kills >= m_achievement.getKills() && m_doubleKills >= m_achievement.getDoubleKills() && 
				m_tripleKills >= m_achievement.getTripleKills() && m_quadrupleKills >= m_achievement.getQuadrupleKills()) {
			
			boolean hasUpdate = true;
			
			HashMap<Weapon, Integer> achWeaponsKills = m_achievement.getWeaponsKills();
			Iterator<Weapon> itWeapon = achWeaponsKills.keySet().iterator();
			
			while(itWeapon.hasNext()) {
				Weapon weapon = itWeapon.next();
				Integer killToHave = achWeaponsKills.get(weapon);
				
				if(!m_weaponKill.containsKey(weapon)) { m_weaponKill.put(weapon, 0); }
				
				if(m_weaponKill.get(weapon) < killToHave) { hasUpdate = false; }
			}
			
			HashMap<Kit, Integer> achKitsKills = m_achievement.getKitsKills();
			Iterator<Kit> itKit = achKitsKills.keySet().iterator();
			
			while(itKit.hasNext()) {
				Kit kit = itKit.next();
				Integer killToHave = achKitsKills.get(kit);
				
				if(!m_kitKill.containsKey(kit)) { m_kitKill.put(kit, 0); }
				
				if(m_kitKill.get(kit) < killToHave) { hasUpdate = false; }
			}
			
			if(hasUpdate) { // Update
				if(m_achievement.getLevel() >= AchievementsManager.getInstance().getMaxLevelArchievement()) {
					// Il a finit le dernier achievement
					isEnded = true;
					return;
				}
				
				m_kills = 0;
				m_doubleKills = 0;
				m_tripleKills = 0;
				m_quadrupleKills = 0;
				
				m_weaponKill.clear();
				m_kitKill.clear();
				
				m_achievement = AchievementsManager.getInstance().getAchievement(m_achievement.getLevel()+1);
				
				String msg = Translation.getString("ACH_UNLOCK");
				BGChat.sendAll(msg.replaceAll("<ach>", m_achievement.getName()).replaceAll("<player>", m_player.getName()));
			}
		}
	}
	
	public void onPlayerKill(Weapon weapon) {
		if(weapon != null) {
			try {
				Integer add = 0;
				if(m_weaponKill.containsKey(weapon)) {
					add = m_weaponKill.get(weapon);
				}
				m_weaponKill.put(weapon, add+1);
			}
			catch(NullPointerException e) {}
		}
		
		Kit kit = KitManager.getInstance().getPlayerKit(m_player.getName());
		
		int add = 0;
		if(m_kitKill.containsKey(kit)) { 
			add = m_kitKill.get(kit); 
		}
		m_kitKill.put(kit, add + 1);
		
		
		m_kills++;
		update();
	}
	
	public void onPlayerDoubleKill() { m_doubleKills++; update(); }
	public void onPlayerTripleKill() { m_tripleKills++; update(); }
	public void onPlayerQuadrupleKill() { m_quadrupleKills++; update(); }
	
	public Integer getKills() { return m_kills >= m_achievement.getKills() ? m_achievement.getKills() : m_kills; }
	public Integer getDoubleKills() { return m_doubleKills >= m_achievement.getDoubleKills() ? m_achievement.getDoubleKills() : m_doubleKills; }
	public Integer getTripleKills() { return m_tripleKills >= m_achievement.getTripleKills() ? m_achievement.getTripleKills() : m_tripleKills; }
	public Integer getQuadrupleKills() { return m_quadrupleKills >= m_achievement.getQuadrupleKills() ? m_achievement.getQuadrupleKills() : m_quadrupleKills; }
	
	public Integer getWeaponKills(Weapon weapon) { 
		Integer kills = m_weaponKill.containsKey(weapon) ? m_weaponKill.get(weapon) : 0;
		return kills >= m_achievement.getWeaponKills(weapon) ? m_achievement.getWeaponKills(weapon) : kills;
	}
	public Integer getKitKills(Kit kit) { 
		Integer kills = m_kitKill.containsKey(kit) ? m_kitKill.get(kit) : 0;
		return kills >= m_achievement.getKitKills(kit) ? m_achievement.getKitKills(kit) : kills;
	}
	
	public HashMap<Weapon, Integer> getWeaponsKills() { return m_weaponKill; }
	public HashMap<Kit, Integer> getKitsKills() { return m_kitKill; }
	
	public void setKills(Integer kills) { m_kills = kills; }
	public void setDoubleKills(Integer kills) { m_doubleKills = kills; }
	public void setTripleKills(Integer kills) { m_tripleKills = kills; }
	public void setQuadrupleKills(Integer kills) { m_quadrupleKills = kills; }
	
	public void setWeaponKills(Weapon weapon, Integer kills) { m_weaponKill.put(weapon, kills); }
	public void setKitKills(Kit kit, Integer kills) { m_kitKill.put(kit, kills); }
	
	public void setLevel(Integer level) {
		if(level > AchievementsManager.getInstance().getMaxLevelArchievement())
			return; // Le nouveau level est trop grand
		if(m_achievement.getLevel() >= AchievementsManager.getInstance().getMaxLevelArchievement())
			return; // Level maximum déjà atteind
		
		m_achievement = AchievementsManager.getInstance().getAchievement(level);
	}
	
	
	public Player getPlayer() { return m_player; }
	public Achievement getAchievement() { return m_achievement; }
	
	public ItemStack getIs() {
		if(isEnded) {
			return m_achievement.getIs(m_player, true);
		}
		
		ItemStack is = m_achievement.getIs(m_player);
		
		ItemMeta im = is.getItemMeta();
		// Lore
		List<String> lore = new LinkedList<String>();
		
		if(m_achievement.getKills() > 0) {
			lore.add(ChatColor.GOLD+Translation.getString("ACH_KILL_ENNEMIS").replaceAll("<kill>", getKills().toString())
					.replaceAll("<max_kill>", m_achievement.getKills().toString()));
		}
					
		if(m_achievement.getDoubleKills() > 0) {
			lore.add(ChatColor.GOLD+Translation.getString("ACH_DOUBLE_KILL").replaceAll("<kill>", getDoubleKills().toString())
					.replaceAll("<max_kill>", m_achievement.getDoubleKills().toString()));
		}
		
		if(m_achievement.getTripleKills() > 0) {
			lore.add(ChatColor.GOLD+Translation.getString("ACH_TRIPLE_KILL").replaceAll("<kill>", getTripleKills().toString())
					.replaceAll("<max_kill>", m_achievement.getTripleKills().toString()));
		}
		
		if(m_achievement.getQuadrupleKills() > 0) {
			lore.add(ChatColor.GOLD+Translation.getString("ACH_QUADRUPLE_KILL").replaceAll("<kill>", getQuadrupleKills().toString())
					.replaceAll("<max_kill>", m_achievement.getQuadrupleKills().toString()));
		}
		
		Iterator<Weapon> itWeapon = m_achievement.getWeaponsKills().keySet().iterator();
		while(itWeapon.hasNext()) {
			Weapon weapon = itWeapon.next();
			
			lore.add(ChatColor.GOLD+Translation.getString("ACH_WEAPON_KILL").replaceAll("<weapon>", weapon.getName()+ChatColor.GOLD)
					.replaceAll("<kill>", getWeaponKills(weapon).toString())
					.replaceAll("<max_kill>", m_achievement.getWeaponKills(weapon).toString()));
		}
		
		Iterator<Kit> itKit = m_achievement.getKitsKills().keySet().iterator();
		while(itKit.hasNext()) {
			Kit kit = itKit.next();
			
			lore.add(ChatColor.GOLD+Translation.getString("ACH_KIT_KILL").replaceAll("<class>", kit.getName()+ChatColor.GOLD)
					.replaceAll("<kill>", getKitKills(kit).toString())
					.replaceAll("<max_kill>", m_achievement.getKitKills(kit).toString()));
		}
		
		
		
		im.setLore(lore);
		
		is.setItemMeta(im);
		
		return is;
	}
}
