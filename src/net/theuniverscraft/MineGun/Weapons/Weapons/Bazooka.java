package net.theuniverscraft.MineGun.Weapons.Weapons;

import net.theuniverscraft.MineGun.MineGun;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Bazooka extends GunWeapon {
	public Bazooka(String name, Material material) {
		super(name, material, 0D);
	}
	
	public void shot(Player player, Boolean useZoom) {
		for(int i = 0; i < m_bulletPerShot; i++) {
			Vector vec = player.getLocation().getDirection().multiply(m_velocityMultiplier);
			TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
			tnt.setMetadata("owner", new FixedMetadataValue(MineGun.getPluginInstance(), player));
			tnt.setMetadata("weapon", new FixedMetadataValue(MineGun.getPluginInstance(), this));
			tnt.setFuseTicks(30);
			tnt.setVelocity(vec);
		}
		if(m_sound != null) m_sound.playSounds(player.getLocation());
	}
	
	public void zoom(Player player, Boolean isZoom) {} // Pas de zoom au bazooka
}
