package net.theuniverscraft.MineGun.Weapons.Utils;

import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Managers.SpeedManager;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Weapons.Weapons.GunWeapon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerGun {
	private Player m_player;
	private GunWeapon m_gun;
	
	private Boolean m_zoom;
	private Long m_lastShot;
	
	private Boolean m_recharge;
	private Long m_rechargerTime;
	
	public PlayerGun(Player player, GunWeapon gun) {
		m_player = player;
		m_gun = gun;
		
		m_zoom = false;
		m_lastShot = 0L;
		
		m_recharge = false;
		m_rechargerTime = 0L;
	}
	
	public void onShot(ItemStack itemStack) {
		if(m_lastShot+m_gun.getShotInterval() > System.currentTimeMillis()) return;
		
		Long timeToRecharge = SpeedManager.getInstance().getPlayerTimeToRecharge(m_player);
		if(m_recharge && m_rechargerTime+timeToRecharge > System.currentTimeMillis()) {
			return;
		}
		else if(m_recharge && m_rechargerTime+timeToRecharge < System.currentTimeMillis()) {
			// On enleve le mot "rechargement"
			ItemMeta im = itemStack.getItemMeta();
			im.setDisplayName(m_gun.getName());
			itemStack.setItemMeta(im);
		}
		m_recharge = false;
		m_gun.shot(m_player, m_zoom);
		
		m_lastShot = System.currentTimeMillis();
		
		short add = (short) Math.ceil(itemStack.getType().getMaxDurability()*1.0 / m_gun.getChargerMaxBullet());
		itemStack.setDurability((short) (itemStack.getDurability() + add));
		
		if(itemStack.getDurability() >= itemStack.getType().getMaxDurability()) {
			if(itemStack.getAmount() > 1) {
				itemStack.setAmount(itemStack.getAmount() - 1);
				m_recharge = true;
				m_rechargerTime = System.currentTimeMillis();
				ItemMeta im = itemStack.getItemMeta();
				im.setDisplayName(m_gun.getName() + ChatColor.DARK_RED + "     Rechargement ...");
				itemStack.setItemMeta(im);
				
				final Material mat = itemStack.getType();
				Bukkit.getScheduler().scheduleSyncDelayedTask(MineGun.getPluginInstance(), new Runnable() {
					@Override
					public void run() {
						updateRechargement(mat);
					}
				}, Utils.msToTick(timeToRecharge));
			}
			else {
				m_player.setItemInHand(null);
			}
			
			if(m_zoom) { onZoom(); }
		}
	}
	
	private void updateRechargement(Material mat) {
		for(int i = 0; i < 7; i++) {
			ItemStack is = m_player.getInventory() .getItem(i);
			if(is != null) {
				if(is.getType() == mat) {
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(m_gun.getName());
					is.setItemMeta(im);
					is.setDurability((short) 0);
				}
			}
		}
	}
	
	public void onZoom() {
		m_zoom = !m_zoom;
		m_gun.zoom(m_player, m_zoom);
	}
	public Boolean getIsZoom() { return m_zoom; }
	
	public Player getPlayer() { return m_player; }
	public GunWeapon getGun() { return m_gun; }
}
