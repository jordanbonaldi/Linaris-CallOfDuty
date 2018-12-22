package net.theuniverscraft.MineGun.Weapons.Utils;

import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Managers.WeaponManager;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Utils.Utils.PlayerDistance;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerRadar {
	private Player m_player;
	private long m_timestamp;
	private ItemStack m_compass;
	
	public PlayerRadar(Player player, ItemStack compass) {
		m_player = player;
		m_timestamp = System.currentTimeMillis() + 90000L;
		m_compass = compass;
		update();
	}
	
	@SuppressWarnings("deprecation")
	public boolean update() {
		PlayerDistance[] near = Utils.getPlayerNear(m_player, false);
		
		if(near.length < 1) {
			resetInv();
			return false;
		}
		
		PlayerDistance nearest = near[0];
		
		PlayerInventory inv = m_player.getInventory();
		
		// Calcul du temps
		long curentTimeMillis = System.currentTimeMillis();
		if(curentTimeMillis > m_timestamp) {
			resetInv();
			return false;
		}
		long time = (m_timestamp - curentTimeMillis) / 1000L; // En secondes
		
		Long minutes = time / 60L;
		Long secondes = time % 60L;
		
		StringBuilder secondesBuilder = new StringBuilder();
		if(secondes < 10) {
			secondesBuilder.append("0");
		}
		secondesBuilder.append(secondes);
		
		Integer distance = (int) nearest.getDistance().doubleValue();
		
		String compassName = Translation.getString("COMPASS_NAME").replaceAll("<time>", minutes.toString() + ":" + secondesBuilder.toString())
				.replaceAll("<player>", nearest.getPlayer().getName()).replaceAll("<distance>", distance.toString());
		
		ItemMeta imCompass = m_compass.getItemMeta();
		imCompass.setDisplayName(compassName);
		m_compass.setItemMeta(imCompass);
		
		inv.setItem(8, m_compass);
		
		ItemStack inHand = m_player.getItemInHand();
		if(inHand != null) {
			ItemMeta im = inHand.getItemMeta();
			im.setDisplayName(compassName);
			inHand.setItemMeta(im);
			
			inv.setItemInHand(inHand);
		}
		
		m_player.updateInventory();
		
		m_player.setCompassTarget(nearest.getPlayer().getLocation());		
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void resetInv() {
		PlayerInventory inv = m_player.getInventory();
		for(int i = 0; i < 9; i++) {
			Weapon weapon = WeaponManager.getInstance().getWeapon(inv.getItem(i).getType());
			if(weapon != null) {
				ItemStack is = inv.getItem(i);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(weapon.getName());
				is.setItemMeta(im);
				
				inv.setItem(i, is);
			}
		}
		m_player.updateInventory();
	}
	
	public Player getPlayer() { return m_player; }
}
