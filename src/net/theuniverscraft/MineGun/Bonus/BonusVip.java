package net.theuniverscraft.MineGun.Bonus;

import java.util.List;

import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Utils.Utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BonusVip extends Bonus {
	public BonusVip(String name, Integer price, List<String> lore, Integer slot, ItemStack is, Integer level) {
		super(name, price, lore, slot, is, level);
	}
	
	public void onActive(Player player) {
		PlayersManager.getInstance().addVip(player);
		Utils.setInv(player);
	}
	
	public void onDelete(Player player) {
		PlayersManager.getInstance().removeVip(player);
		Utils.setInv(player);
	}
}
