package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Archievements.Achievement;
import net.theuniverscraft.MineGun.Archievements.PlayerAchievement;
import net.theuniverscraft.MineGun.Managers.KitManager.Kit;
import net.theuniverscraft.MineGun.Utils.ItemStackBuilder;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.Material;
import org.bukkit.entity.Player;


public class AchievementsManager {
	private Achievement m_defaultAchievement;
	private List<Achievement> m_achievements = new LinkedList<Achievement>();
	private List<PlayerAchievement> m_playerAchievements = new LinkedList<PlayerAchievement>();
	
	private static AchievementsManager instance = null;
	public static AchievementsManager getInstance() {
		if(instance == null) instance = new AchievementsManager();
		return instance;
	}
	
	public AchievementsManager() {
		Weapon knife = WeaponManager.getInstance().getWeapon(Material.GOLD_SWORD);
		Weapon sniper = WeaponManager.getInstance().getWeapon(Material.IRON_SPADE);
		Weapon ak47 = WeaponManager.getInstance().getWeapon(Material.DIAMOND_SPADE);
		Weapon desert = WeaponManager.getInstance().getWeapon(Material.WOOD_SPADE);
		Weapon m16 = WeaponManager.getInstance().getWeapon(Material.GOLD_SPADE);
		Weapon mp7 = WeaponManager.getInstance().getWeapon(Material.DIAMOND_PICKAXE);
		
		Kit campeur = KitManager.getInstance().getKit("Campeur");
		
		m_defaultAchievement = new Achievement("&bSoldat", new ItemStackBuilder(Material.INK_SACK).setData((short) 15), 0)
				.setKills(30).setDoubleKills(3).addWeaponKill(knife, 5)
				.addKitKill(KitManager.getInstance().getKit("Ninja"), 10);
		m_achievements.add(m_defaultAchievement);
		
		
		m_achievements.add(new Achievement("&bCaporal", new ItemStackBuilder(Material.INK_SACK).setData((short) 14), 1)
				.setKills(100).setDoubleKills(8).addWeaponKill(sniper, 25).addWeaponKill(m16, 25));

		
		m_achievements.add(new Achievement("&bSergent", new ItemStackBuilder(Material.INK_SACK).setData((short) 13), 2)
				.setKills(170).setDoubleKills(12).addWeaponKill(ak47, 35).addWeaponKill(knife, 20));
		
		
		m_achievements.add(new Achievement("&bMajor", new ItemStackBuilder(Material.INK_SACK).setData((short) 12), 3)
				.setKills(220).setDoubleKills(17).addWeaponKill(m16, 50).addWeaponKill(desert, 25));
		
		
		m_achievements.add(new Achievement("&bLieutenant", new ItemStackBuilder(Material.INK_SACK).setData((short) 11), 4)
				.setKills(280).setDoubleKills(25).addWeaponKill(sniper, 60).addWeaponKill(ak47, 70));
		
		
		m_achievements.add(new Achievement("&bCapitaine", new ItemStackBuilder(Material.INK_SACK).setData((short) 7), 5)
				.setKills(320).addWeaponKill(knife, 40).addWeaponKill(sniper, 70).addWeaponKill(ak47, 100));
		
		
		m_achievements.add(new Achievement("&bCommandant", new ItemStackBuilder(Material.INK_SACK).setData((short) 9), 6)
				.setKills(370).addWeaponKill(knife, 60).addWeaponKill(sniper, 100).addWeaponKill(m16, 100));
		
		
		m_achievements.add(new Achievement("&bColonel", new ItemStackBuilder(Material.INK_SACK).setData((short) 8), 7)
				.setKills(450).addWeaponKill(sniper, 150).addWeaponKill(ak47, 150).addWeaponKill(m16, 150));
		
		
		m_achievements.add(new Achievement("&bGénéral", new ItemStackBuilder(Material.INK_SACK).setData((short) 10), 8)
				.setKills(600).addKitKill(campeur, 100).addWeaponKill(mp7, 200).addWeaponKill(ak47, 200));
	}
	
	public PlayerAchievement getPlayerAchievement(Player player) {
		for(PlayerAchievement pa : m_playerAchievements) {
			if(pa.getPlayer().getName().equalsIgnoreCase(player.getName())) {
				return pa;
			}
		}
		
		PlayerAchievement newPlayerAchievement = new PlayerAchievement(player, m_defaultAchievement);
		m_playerAchievements.add(newPlayerAchievement);
		return newPlayerAchievement;
	}
	
	public Achievement getAchievement(String name) {
		for(Achievement ach : m_achievements) {
			if(ach.getName().contains(name)) return ach;
		}
		return null;
	}
	public Achievement getAchievement(Integer level) {
		for(Achievement ach : m_achievements) {
			if(ach.getLevel() == level) return ach;
		}
		return null;
	}
	
	public Integer getMaxLevelArchievement() {
		int level = 0;
		for(Achievement ach : m_achievements) {
			if(ach.getLevel() > level) level = ach.getLevel();
		}
		return level;
	}
	
	public List<Achievement> getAchievements() { return m_achievements; }
}
