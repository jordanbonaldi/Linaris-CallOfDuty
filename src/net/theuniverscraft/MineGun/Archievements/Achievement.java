package net.theuniverscraft.MineGun.Archievements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Managers.AchievementsManager;
import net.theuniverscraft.MineGun.Managers.KitManager.Kit;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Achievement {
	protected String m_name;
	protected ItemStack m_is;
	protected Integer m_level;
	
	protected Integer m_kills = 0;
	protected Integer m_doubleKills = 0;
	protected Integer m_tripleKills = 0;
	protected Integer m_quadrupleKills = 0;
	
	protected HashMap<Weapon, Integer> m_weaponKill = new HashMap<Weapon, Integer>();
	protected HashMap<Kit, Integer> m_kitKill = new HashMap<Kit, Integer>();
	
	public Achievement(String name, ItemStack is, Integer level) {
		m_name = name;
		m_is = is;
		m_level = level;
	}
	
	public Achievement setKills(Integer kills) {
		m_kills = kills;
		return this;
	}
	public Achievement setDoubleKills(Integer kills) {
		m_doubleKills = kills;
		return this;
	}
	public Achievement setTripleKills(Integer kills) {
		m_tripleKills = kills;
		return this;
	}
	public Achievement setQuadrupleKills(Integer kills) {
		m_quadrupleKills = kills;
		return this;
	}
	public Achievement addWeaponKill(Weapon weapon, Integer kills) {
		m_weaponKill.put(weapon, kills);
		return this;
	}
	public Achievement addKitKill(Kit kit, Integer kills) {
		m_kitKill.put(kit, kills);
		return this;
	}
	
	public Integer getKills() { return m_kills; }
	public Integer getDoubleKills() { return m_doubleKills; }
	public Integer getTripleKills() { return m_tripleKills; }
	public Integer getQuadrupleKills() { return m_quadrupleKills; }
	
	public Integer getWeaponKills(Weapon weapon) { return m_weaponKill.containsKey(weapon) ? m_weaponKill.get(weapon) : null; }
	public HashMap<Weapon, Integer> getWeaponsKills() { return m_weaponKill; }
	
	public Integer getKitKills(Kit kit) { return m_kitKill.containsKey(kit) ? m_kitKill.get(kit) : null; }
	public HashMap<Kit, Integer> getKitsKills() { return m_kitKill; }
	
	
	public int getSlot() { return m_level+9; }
	
	public String getName() { return ChatColor.translateAlternateColorCodes('&', m_name); }
	public Integer getLevel() { return m_level; }
	
	public ItemStack getIs(Player player) {
		return getIs(player, false);
	}
	public ItemStack getIs(Player player, boolean isEnded) {
		ItemMeta im = m_is.getItemMeta();
		im.setDisplayName(getName());
		
		List<String> lore = new LinkedList<String>();
		if(AchievementsManager.getInstance().getPlayerAchievement(player).getAchievement().getLevel() > m_level || isEnded) {
			// Si le joueur a déjà cet achievement
			if(m_kills > 0) {
				lore.add(ChatColor.GREEN+Translation.getString("ACH_KILL_ENNEMIS").replaceAll("<kill>", m_kills.toString())
						.replaceAll("<max_kill>", m_kills.toString()));
			}
						
			if(m_doubleKills > 0) {
				lore.add(ChatColor.GREEN+Translation.getString("ACH_DOUBLE_KILL").replaceAll("<kill>", m_doubleKills.toString())
						.replaceAll("<max_kill>", m_doubleKills.toString()));
			}
			
			if(m_tripleKills > 0) {
				lore.add(ChatColor.GREEN+Translation.getString("ACH_TRIPLE_KILL").replaceAll("<kill>", m_tripleKills.toString())
						.replaceAll("<max_kill>", m_tripleKills.toString()));
			}
			
			if(m_quadrupleKills > 0) {
				lore.add(ChatColor.GREEN+Translation.getString("ACH_QUADRUPLE_KILL").replaceAll("<kill>", m_quadrupleKills.toString())
						.replaceAll("<max_kill>", m_quadrupleKills.toString()));
			}
			
			Iterator<Weapon> itWeapon = m_weaponKill.keySet().iterator();
			while(itWeapon.hasNext()) {
				Weapon weapon = itWeapon.next();
				Integer kills = m_weaponKill.get(weapon);
				
				lore.add(ChatColor.GREEN+Translation.getString("ACH_WEAPON_KILL").replaceAll("<weapon>", weapon.getName()+ChatColor.GREEN)
						.replaceAll("<kill>", kills.toString()).replaceAll("<max_kill>", kills.toString()));
			}
			
			Iterator<Kit> itKit = m_kitKill.keySet().iterator();
			while(itKit.hasNext()) {
				Kit kit = itKit.next();
				Integer kills = m_kitKill.get(kit);
				
				lore.add(ChatColor.GREEN+Translation.getString("ACH_KIT_KILL").replaceAll("<class>", kit.getName()+ChatColor.GREEN)
						.replaceAll("<kill>", kills.toString()).replaceAll("<max_kill>", kills.toString()));
			}
		}
		else if(AchievementsManager.getInstance().getPlayerAchievement(player).getAchievement().getLevel() < m_level) {
			// Si le joueur n'a pas du tout cet achievement
			lore.add(ChatColor.DARK_RED+"Vous devez débloquer");
			lore.add(ChatColor.DARK_RED+"le grade précédent !");
		}
		// Sinon le PlayerAchievement ce charge des lores
		im.setLore(lore);
		m_is.setItemMeta(im);
		
		return m_is;
	}
	
	public boolean equals(Object object) {
		if(object == this) return true;
		if(!(object instanceof Achievement)) return false;
		
		Achievement Achievement = (Achievement) object;
		if(Achievement.getLevel() == m_level)
			return true;
		
		return false;
	}
	
	public int hashCode() {
		int result = 7;
		final int multiplier = 17;
		
		result = multiplier*result + m_level.hashCode();
		
		return result;
	}
}
