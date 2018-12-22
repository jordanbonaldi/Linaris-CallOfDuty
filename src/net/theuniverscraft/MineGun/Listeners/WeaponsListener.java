package net.theuniverscraft.MineGun.Listeners;

import net.theuniverscraft.MineGun.Game;
import net.theuniverscraft.MineGun.GameStatus;
import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.RespawnTimerManager;
import net.theuniverscraft.MineGun.Managers.TeamManager;
import net.theuniverscraft.MineGun.Managers.WeaponManager;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Utils.UtilsFight;
import net.theuniverscraft.MineGun.Weapons.Utils.PlayerGun;
import net.theuniverscraft.MineGun.Weapons.Weapons.CacWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.GunWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.LaunchWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.PowerWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class WeaponsListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(RespawnTimerManager.getInstance().getRespawner(event.getPlayer()) != null || 
				Game.getInstance().getStatus() == GameStatus.LOBBY) {
			event.setCancelled(true);
			return;
		}
		
		if(event.getPlayer().getItemInHand() == null) {	return;	}
		
		Weapon weapon = WeaponManager.getInstance().getWeapon(event.getPlayer().getItemInHand().getType());
		try {
			if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
				if(weapon instanceof GunWeapon) { // zoom
					PlayerGun playerGun = PlayersManager.getInstance().getPlayerGun(event.getPlayer(), (GunWeapon) weapon);
					playerGun.onZoom();
				}
				else if(weapon instanceof PowerWeapon) {
					PowerWeapon powerWeapon = (PowerWeapon) weapon;
					powerWeapon.onLeftClick(event);
				}
			}
			else {
				if(weapon instanceof GunWeapon) { // shot
					PlayerGun playerGun = PlayersManager.getInstance().getPlayerGun(event.getPlayer(), (GunWeapon) weapon);
					playerGun.onShot(event.getPlayer().getItemInHand());
				}
				else if(weapon instanceof LaunchWeapon) {
					LaunchWeapon launchWeapon = (LaunchWeapon) weapon;
					launchWeapon.onRightClick(event);
				}
				else if(weapon instanceof PowerWeapon) {
					PowerWeapon powerWeapon = (PowerWeapon) weapon;
					powerWeapon.onRightClick(event);
				}
			}
		}
		catch(NullPointerException e) {}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player || event.getDamager() instanceof Projectile)) {
			return;
		}
		
		try {
			Player damager = null;
			if(event.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) event.getDamager();
				if(projectile.hasMetadata("isForTarget")) {
					event.setDamage(2D);
					return;
				}
				if(!(projectile.getShooter() instanceof Player)) return;
				damager = (Player) projectile.getShooter();
				
				GunWeapon weapon = (GunWeapon) Utils.getMetadataValue("weapon", projectile).value();
				if(weapon == null) return;
				
				if(weapon instanceof GunWeapon) {
					GunWeapon gunWeapon = (GunWeapon) weapon;
					gunWeapon.onProjectilHit(event, damager);
				}
				return;
			}
			else {
				damager = (Player) event.getDamager();
			}
			
			if(event.getEntity() instanceof Player) {
				if(!TeamManager.getInstance().areEnnemis(damager, (Player) event.getEntity())) {
					event.setCancelled(true);
					return;
				}
			}
			
			Weapon weapon = WeaponManager.getInstance().getWeapon(damager.getItemInHand().getType());
			
			if(weapon instanceof CacWeapon) {
				CacWeapon cacWeapon = (CacWeapon) weapon;
				cacWeapon.onCac(event, damager);
			}

			event.setDamage(UtilsFight.getArmorReduction(event.getEntity(), event.getDamage()));
		}
		catch(NullPointerException e) {}
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		try {
			Player player = event.getPlayer();
			Weapon weapon = WeaponManager.getInstance().getWeapon(player.getInventory().getItem(event.getPreviousSlot()).getType());
			if(weapon instanceof GunWeapon) {
				PlayerGun playerGun = PlayersManager.getInstance().getPlayerGun(player, (GunWeapon) weapon);
				if(playerGun.getIsZoom()) playerGun.onZoom();
			}
		}
		catch(NullPointerException e) {}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		//event.setCancelled(true);
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		//event.setCancelled(true);
	}
}
