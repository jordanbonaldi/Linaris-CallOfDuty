package net.theuniverscraft.MineGun.Weapons.Weapons;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.TeamManager;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Utils.Utils.PlayerDistance;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Wolfs extends PowerWeapon {
	public Wolfs(String name, Material material) {
		super(name, material);
	}
	
	public void onLeftClick(PlayerInteractEvent event) {}
	
	public void onRightClick(PlayerInteractEvent event) {
		PlayerDistance[] pd = Utils.getPlayerNear(event.getPlayer(), true);
		
		int nbWolf = 0;
		while(nbWolf < 5) {
			for(int i = 0; i < (pd.length >= 5 ? 5 : pd.length); i++) {
				final Player target = pd[i].getPlayer();
				Location playerLoc = event.getPlayer().getLocation();
				Location wolfLoc = playerLoc.toVector().add(playerLoc.getDirection().multiply(3)).toLocation(playerLoc.getWorld());
				
				while(wolfLoc.getWorld().getBlockAt(wolfLoc).getType() != Material.AIR) wolfLoc.add(0, 1, 0);
				wolfLoc.add(nbWolf, 0, 0);
				
				final Wolf wolf = (Wolf) playerLoc.getWorld().spawnEntity(wolfLoc, EntityType.WOLF);
				wolf.setCollarColor(TeamManager.getInstance().getPlayerDyeColor(event.getPlayer()));
				wolf.setMaxHealth(22D);
				wolf.setHealth(22L);
				wolf.setAngry(true);
				
				wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
				wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 4));
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
					public void run() {
						wolf.setTarget(target.getPlayer());
						Snowball snowBall = (Snowball) target.getWorld().spawnEntity(wolf.getLocation().add(0,2,0), EntityType.SNOWBALL);
						snowBall.setMetadata("isForTarget", new FixedMetadataValue(MineGun.getPluginInstance(), true));
						snowBall.setShooter(target);
					}
				}, 5L);
				nbWolf++;
				if(nbWolf >= 5) break;
			}
		}
		super.onRightClick(event);
	}
}
