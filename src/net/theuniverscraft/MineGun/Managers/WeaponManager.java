package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Utils.FinalSound;
import net.theuniverscraft.MineGun.Utils.WeaponSound;
import net.theuniverscraft.MineGun.Weapons.Weapons.Bazooka;
import net.theuniverscraft.MineGun.Weapons.Weapons.Drone;
import net.theuniverscraft.MineGun.Weapons.Weapons.Grenade;
import net.theuniverscraft.MineGun.Weapons.Weapons.GunWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.Knife;
import net.theuniverscraft.MineGun.Weapons.Weapons.Radar;
import net.theuniverscraft.MineGun.Weapons.Weapons.Sniper;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.Wolfs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class WeaponManager {
	private List<Weapon> weapons = new LinkedList<Weapon>();
	
	private static WeaponManager instance = null;
	public static WeaponManager getInstance() {
		if(instance == null) instance = new WeaponManager();
		return instance;
	}
	
	private WeaponManager() {
		weapons.add(new Knife("&b&lCouteau", Material.GOLD_SWORD, 1200L));
		
		weapons.add(new GunWeapon("&b&lPompe", Material.STONE_SPADE, 14D).setShotInterval(1200L).setVelocityMultiplier(4D).setMaxDecalage(0.1)
				.setBulletPerShot(2).setWeaponSound(new WeaponSound(new FinalSound(Sound.GHAST_FIREBALL, 1L, 20L)))
				.setItemStackCharger(new ItemStack(Material.SPIDER_EYE))
				.setChargerMaxBullet(7).setWeaponPrice(10).setChargerPrice(10));
		
		weapons.add(new GunWeapon("&b&lAK47", Material.DIAMOND_SPADE, 6D).setVelocityMultiplier(5D).setMaxDecalage(0.25)
				.setShotInterval(100L).setChargerMaxBullet(30)
				.setWeaponPrice(10).setChargerPrice(10)
				.setWeaponSound(new WeaponSound(new FinalSound(Sound.SKELETON_HURT, 1L, 20L), 
						new FinalSound(Sound.ZOMBIE_WOOD, 1L, 20L), new FinalSound(Sound.GHAST_FIREBALL, 1L, 20L)))
				.setItemStackCharger(new ItemStack(Material.EGG)));
		
		weapons.add(new GunWeapon("&b&lM16", Material.GOLD_SPADE, 4D).setVelocityMultiplier(6D).setMaxDecalage(0.12)
				.setBulletPerShot(1).setShotInterval(50L)
				.setWeaponPrice(10).setChargerPrice(10)
				.setWeaponSound(new WeaponSound(new FinalSound(Sound.SKELETON_HURT, 1L, 20L), 
						new FinalSound(Sound.ZOMBIE_WOOD, 1L, 20L), new FinalSound(Sound.GHAST_FIREBALL, 1L, 20L)))
				.setItemStackCharger(new ItemStack(Material.SLIME_BALL))
				.setChargerMaxBullet(38));
		
		weapons.add(new GunWeapon("&b&lDesert-Eagle", Material.WOOD_SPADE, 5D).setVelocityMultiplier(5D).setMaxDecalage(0.25)
				.setShotInterval(800L).setWeaponSound(new WeaponSound(new FinalSound(Sound.GHAST_FIREBALL, 1L, 20L), 
						new FinalSound(Sound.ZOMBIE_METAL, 1L, 20L)))
				.setItemStackCharger(new ItemStack(Material.GHAST_TEAR))
				.setChargerMaxBullet(12).setWeaponPrice(10).setChargerPrice(10));
		
		weapons.add(new GunWeapon("&b&lMP7", Material.DIAMOND_PICKAXE, 9D).setVelocityMultiplier(25D).setMaxDecalage(0.1)
				.setShotInterval(25L).setWeaponSound(new WeaponSound(new FinalSound(Sound.GHAST_FIREBALL, 1L, 20L), 
						new FinalSound(Sound.ZOMBIE_METAL, 1L, 20L))).setChargerMaxBullet(40)
				.setItemStackCharger(new ItemStack(Material.GLOWSTONE_DUST)).setWeaponPrice(10).setChargerPrice(10));
		
		weapons.add(new Sniper("&b&lSniper", Material.IRON_SPADE, 18D).setVelocityMultiplier(60D).setMaxDecalage(0.1)
				.setShotInterval(2000L).setWeaponSound(new WeaponSound(new FinalSound(Sound.GHAST_FIREBALL, 1L, 20L), 
						new FinalSound(Sound.ZOMBIE_METAL, 1L, 20L))).setZoomIsPrecise(true)
				.setItemStackCharger(new ItemStack(Material.MELON_SEEDS))
				.setChargerMaxBullet(4).setWeaponPrice(10).setChargerPrice(10));
		
		weapons.add(new Grenade("&b&lGrenade", Material.FIREWORK_CHARGE).setWeaponPrice(10));
		
		weapons.add(new Bazooka("&b&lBazooka", Material.WOOD_PICKAXE).setVelocityMultiplier(2D)
				.setItemStackCharger(new ItemStack(Material.FERMENTED_SPIDER_EYE))
				.setChargerMaxBullet(3).setWeaponPrice(10).setChargerPrice(10));
		
		weapons.add(new Radar("&b&lRadar", Material.QUARTZ).setWeaponPrice(20));
		weapons.add(new Drone("&b&lDrone", Material.RECORD_8, 1, 1).setWeaponPrice(30));
		weapons.add(new Drone("&b&lAvion", Material.RECORD_7, 2, 2).setWeaponPrice(40));
		
		weapons.add(new Wolfs("&b&lChiens", Material.RECORD_9).setWeaponPrice(50));
	}
	
	public Weapon getWeapon(Material material) {
		for(Weapon weapon : weapons) {
			if(weapon.getMaterial() == material) return weapon;
		}
		return null;
	}
	public Weapon getWeaponByItemShop(ItemStack is) {
		for(Weapon weapon : weapons) {
			if(weapon.getItemStackShop().getType() == is.getType() && weapon.getItemStackShop().getDurability() == is.getDurability())
				return weapon;
		}
		return null;
	}
	public GunWeapon getGunWeaponByItemCharger(ItemStack is) {
		for(Weapon weapon : weapons) {
			if(weapon instanceof GunWeapon) {
				GunWeapon gun = (GunWeapon) weapon;
				if(gun.getItemStackCharger().getType() == is.getType() && gun.getItemStackCharger().getDurability() == is.getDurability())
					return gun;
			}
		}
		return null;
	}
	
	public List<Weapon> getListWeapon() {
		return weapons;
	}
}
