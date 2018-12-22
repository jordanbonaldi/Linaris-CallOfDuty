package net.theuniverscraft.MineGun.Weapons.Weapons;

import net.theuniverscraft.MineGun.MineGun;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Grenade extends LaunchWeapon {
	public Grenade(String name, Material material) {
		super(name, material, 1D);
	}
	
	public Item onRightClick(PlayerInteractEvent event) {
		final Item dropped = super.onRightClick(event);
		final Player player = event.getPlayer();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				Location loc = dropped.getLocation();
				TNTPrimed tnt = (TNTPrimed) loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
				tnt.setMetadata("owner", new FixedMetadataValue(MineGun.getPluginInstance(), player));
				tnt.setMetadata("weapon", new FixedMetadataValue(MineGun.getPluginInstance(), this));
				tnt.setFuseTicks(0);
				dropped.remove();
			}
		}, 50L);
		return dropped;
	}
}
