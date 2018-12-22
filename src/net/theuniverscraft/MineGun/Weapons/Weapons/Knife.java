package net.theuniverscraft.MineGun.Weapons.Weapons;

import java.util.HashMap;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.ForceManager;
import net.theuniverscraft.MineGun.Managers.RespawnTimerManager;
import net.theuniverscraft.MineGun.Managers.TeamManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Knife extends CacWeapon {
	private Long m_interval;
	private HashMap<String, Long> lastShot = new HashMap<String, Long>();
	public Knife(String name, Material material, Long interval) {
		super(name, material, 10D);
		m_interval = interval;
	}
	
	@Override
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
		
		Boolean cancel = true;
		if(!lastShot.containsKey(damager.getName())) {
			cancel = false;
			lastShot.put(damager.getName(), System.currentTimeMillis());
		}
		else if(lastShot.get(damager.getName())+m_interval < System.currentTimeMillis()) {
			cancel = false;
			lastShot.remove(damager.getName());
			lastShot.put(damager.getName(), System.currentTimeMillis());
		}
		
		if(cancel) {
			event.setCancelled(true);
			return;
		}
		
		final ItemStack is = damager.getItemInHand();
		if(damager.getItemInHand() != null) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
				public void run() {
					is.setDurability((short) -1);
				}
			}, 5L);
		}
		
		if(ForceManager.getInstance().getKnifeOnShot(damager)) event.setDamage(20D);
		else super.onCac(event, damager);
	}
}
