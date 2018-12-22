package net.theuniverscraft.MineGun.Weapons.Weapons;

import net.theuniverscraft.MineGun.Managers.RadarTimerManager;
import net.theuniverscraft.MineGun.Weapons.Utils.PlayerRadar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Radar extends PowerWeapon {
	public Radar(String name, Material material) {
		super(name, material);
	}
	
	public void onLeftClick(PlayerInteractEvent event) {}
	
	public void onRightClick(PlayerInteractEvent event) {
		drawMenu(event.getPlayer());
		super.onRightClick(event);
	}
	
	@SuppressWarnings("deprecation")
	public void drawMenu(Player player) {
		/*int playerSize = BGPlayers.getPlayers().size() -1;
		if(playerSize >= 18) playerSize = 18;
		Inventory inv = Bukkit.createInventory(player, (playerSize / 9 + 1)*9, "Radar");
		int i = 0;
		for(PlayerDistance pd : Utils.getPlayerNear(player, false)) {
			if(i > 18) break;
			ItemStack skull = new ItemStackBuilder(Material.SKULL_ITEM).setData((short) 2);
			SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
			skullMeta.setDisplayName(pd.getPlayer().getName());
			skullMeta.setOwner(pd.getPlayer().getName());
			skull.setItemMeta(skullMeta);
			inv.setItem(i, skull);
			i++;
		}
		player.openInventory(inv);*/
		
		PlayerInventory inv = player.getInventory();
		
		ItemStack compass = new ItemStack(Material.COMPASS);
		PlayerRadar playerRadar = new PlayerRadar(player, compass);
		RadarTimerManager.getInstance().addRadar(playerRadar);
		
		inv.setItem(8, compass);
		
		player.updateInventory();
	}
}
