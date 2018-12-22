package net.theuniverscraft.MineGun.Weapons.Weapons;

import net.theuniverscraft.MineGun.Managers.RespawnTimerManager;
import net.theuniverscraft.MineGun.Managers.TeamManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CacWeapon extends Weapon {
	public CacWeapon(String name, Material material, Double damage) {
		super(name, material, damage);
	}

	public void onCac(EntityDamageByEntityEvent event, Player damager) {
		if(event.getEntity() instanceof Player) {
			if(!TeamManager.getInstance().areEnnemis(damager, (Player) event.getEntity())) {
				event.setCancelled(true);
				return;
			}
		}
		
		if(RespawnTimerManager.getInstance().getRespawner(damager) != null) {
			event.setCancelled(true);
			return;
		}
		event.setDamage(m_damage);
	}
}
