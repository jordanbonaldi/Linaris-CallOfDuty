package net.theuniverscraft.MineGun.Weapons.Weapons;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.ForceManager;
import net.theuniverscraft.MineGun.Managers.TeamManager;
import net.theuniverscraft.MineGun.Utils.UtilsFight;
import net.theuniverscraft.MineGun.Utils.WeaponSound;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GunWeapon extends Weapon {
	protected Double m_maxDecalage;
	protected Double m_velocityMultiplier;
	
	protected Integer m_bulletPerShot;
	protected WeaponSound m_sound;
	
	protected Integer m_chargerMaxBullet;
	
	protected Long m_interval;
	protected Boolean m_zoomIsPrecise;
	
	protected Integer m_chargerPrice;
	protected ItemStack m_isChargerShop;
	
	public GunWeapon(String name, Material material, Double damage) {
		super(name, material, damage);
		m_maxDecalage = 0D;
		m_velocityMultiplier = 1D;
		m_bulletPerShot = 1;
		m_interval = 0L;
		m_sound = null;
		m_chargerMaxBullet = 1;
		m_zoomIsPrecise = false;
		
		m_chargerPrice = 50;
		m_isChargerShop = new ItemStack(m_material);
	}
	
	public GunWeapon setItemStackCharger(ItemStack is) {
		m_isChargerShop = is;
		return this;
	}
	public ItemStack getItemStackCharger() { return m_isChargerShop; }
	
	public GunWeapon setVelocityMultiplier(Double velocityMultiplier) {
		m_velocityMultiplier = velocityMultiplier;
		return this;
	}
	public GunWeapon setShotInterval(Long interval) {
		m_interval = interval;
		return this;
	}
	public Long getShotInterval() { return m_interval; }
	
	public GunWeapon setMaxDecalage(Double maxDecalage) {
		m_maxDecalage = maxDecalage;
		return this;
	}
	
	public GunWeapon setBulletPerShot(Integer bulletPerShot) {
		m_bulletPerShot = bulletPerShot;
		return this;
	}
	
	public GunWeapon setWeaponSound(WeaponSound sound) {
		m_sound = sound;
		return this;
	}
	
	public GunWeapon setZoomIsPrecise(Boolean zoomIsPrecise) {
		m_zoomIsPrecise = zoomIsPrecise;
		return this;
	}
	
	public GunWeapon setChargerMaxBullet(Integer chargerMaxBullet) {
		m_chargerMaxBullet = chargerMaxBullet;
		return this;
	}
	public Integer getChargerMaxBullet() { return m_chargerMaxBullet; }
	
	
	public GunWeapon setChargerPrice(Integer price) {
		m_chargerPrice = price;
		return this;
	}
	public Integer getChargerPrice() { return m_chargerPrice; }
	
	public GunWeapon setWeaponPrice(Integer price) {
		m_weaponPrice = price;
		return this;
	}
	
	protected Vector setLocationDecalage(Vector vector, Boolean useZoom) { // pour le sniper
		if(useZoom && m_zoomIsPrecise) return vector;
		
		Double decalageX = Math.random() * m_maxDecalage;
		Double decalageY = Math.random() * m_maxDecalage;
		Double decalageZ = Math.random() * m_maxDecalage;
		
		Double add = 1 - m_maxDecalage;
		
		Vector narmol = new Vector(decalageX + add, decalageY + add, decalageZ + add);
		vector.multiply(narmol);
		return vector;
	}
	
	public void onProjectilHit(EntityDamageByEntityEvent event, Player damager) {
		if(event.getEntity() instanceof Player) {
			if(!TeamManager.getInstance().areEnnemis(damager, (Player) event.getEntity())) {
				event.setCancelled(true);
				return;
			}
		}
		
		event.setDamage(UtilsFight.getArmorReduction(event.getEntity(), ForceManager.getInstance().getDamage(damager, m_damage)));
	}
	
	public void shot(Player player, Boolean useZoom) {
		for(int i = 0; i < m_bulletPerShot; i++) {
			Vector vec = setLocationDecalage(player.getLocation().getDirection().multiply(m_velocityMultiplier), useZoom);
			Snowball snowball = player.launchProjectile(Snowball.class);
			snowball.setMetadata("weapon", new FixedMetadataValue(MineGun.getPluginInstance(), this));
			snowball.setVelocity(vec);
		}
		
		if(m_sound != null) m_sound.playSounds(player.getLocation());
	}
	
	public void zoom(Player player, Boolean isZoom) {		
		if(isZoom) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 24000, 4));
		}
		else {
			player.removePotionEffect(PotionEffectType.SLOW);
		}
	}
}
