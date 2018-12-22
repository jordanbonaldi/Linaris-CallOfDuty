package net.theuniverscraft.MineGun.Weapons.Weapons;

import java.util.Random;

import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Utils.Utils.PlayerDistance;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;

public class Drone extends PowerWeapon {
	private Integer m_nbTarget;
	private Integer m_tntMultiplier;
	
	public Drone(String name, Material material, Integer nbTarget, Integer tntMultiplier) {
		super(name, material);
		m_nbTarget = nbTarget;
		m_tntMultiplier = tntMultiplier;
	}
	
	public void onLeftClick(PlayerInteractEvent event) {}
	
	public void onRightClick(PlayerInteractEvent event) {
		PlayerDistance[] distances = Utils.getPlayerNear(event.getPlayer(), true);
		
		int nbTarget = (distances.length >= m_nbTarget ? m_nbTarget : distances.length);
		Random rand = new Random();
		for(int i = 0; i < nbTarget; i++) {
			shotPlayer(distances[rand.nextInt(nbTarget)].getPlayer());
		}
		
		super.onRightClick(event);
		
		String msg = Translation.getString("YOU_HAVE_DRONE");
		event.getPlayer().sendMessage(msg.replaceAll("<drone_name>", getName()+ChatColor.RESET+""+ChatColor.getLastColors(msg)));
	}
	
	public void shotPlayer(Player player) {
		Location loc = player.getLocation();
		
		for(int i = 0; i < m_tntMultiplier; i++) {
			((TNTPrimed) loc.getWorld().spawnEntity(loc.clone().add(2.5, 15, 2.5), EntityType.PRIMED_TNT)).setFuseTicks(38);
			((TNTPrimed) loc.getWorld().spawnEntity(loc.clone().add(2.5, 15, -2.5), EntityType.PRIMED_TNT)).setFuseTicks(38);
			((TNTPrimed) loc.getWorld().spawnEntity(loc.clone().add(-2.5, 15, -2.5), EntityType.PRIMED_TNT)).setFuseTicks(38);
			((TNTPrimed) loc.getWorld().spawnEntity(loc.clone().add(-2.5, 15, 2.5), EntityType.PRIMED_TNT)).setFuseTicks(38);
		}
	}
	
	public Integer getNbTarget() { return m_nbTarget; }
}
