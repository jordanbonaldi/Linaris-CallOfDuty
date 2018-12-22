package net.theuniverscraft.MineGun.Utils;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Game;
import net.theuniverscraft.MineGun.GameStatus;
import net.theuniverscraft.MineGun.MineGun;
import net.theuniverscraft.MineGun.Archievements.Achievement;
import net.theuniverscraft.MineGun.Archievements.PlayerAchievement;
import net.theuniverscraft.MineGun.Background.BGPlayers;
import net.theuniverscraft.MineGun.Managers.AchievementsManager;
import net.theuniverscraft.MineGun.Managers.KitManager;
import net.theuniverscraft.MineGun.Managers.KitManager.Kit;
import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.RespawnTimerManager;
import net.theuniverscraft.MineGun.Managers.TeamManager;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Managers.WeaponManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

public class Utils {
	
	public static void updateInterface(Player player) {
		ItemStack itemShop = new ItemStack(Material.EMERALD);
		ItemMeta im = itemShop.getItemMeta();
		im.setDisplayName(ChatColor.DARK_GREEN+"Shop");
		List<String> lore = new LinkedList<String>();
		lore.add(ChatColor.GOLD+"Vous avez : "+ChatColor.AQUA+PlayersManager.getInstance().getPlayerPoints(player)+ChatColor.GOLD+" points !");
		im.setLore(lore);
		itemShop.setItemMeta(im);
		player.getInventory().setItem(22, itemShop);
	}
	
	public static void reloadAchievement(Player player) {
		Inventory inv = player.getInventory();
		
		// Achievements
		PlayerAchievement pa = AchievementsManager.getInstance().getPlayerAchievement(player);
		pa.update();
		for(Achievement ach : AchievementsManager.getInstance().getAchievements()) {
			inv.setItem(ach.getSlot(), ach.getIs(player));
		}
		inv.setItem(pa.getAchievement().getSlot(), pa.getIs());
	}
	
	@SuppressWarnings("deprecation")
	public static void setInv(Player player) {
		PlayerInventory inv = player.getInventory();
		inv.clear();
		
		Utils.reloadAchievement(player);
		
		// Les classes
		Kit playerKit = KitManager.getInstance().getPlayerKit(player.getName());
		for(Kit kit : KitManager.getInstance().getKits()) {
			try {
				ItemStack is = kit.getIcon();
				
				
				List<String> lore = new LinkedList<String>(kit.getDescription());
	
				ItemMeta im = is.getItemMeta();
				
				String nameKit = kit.getName();
				if(kit.getName().equalsIgnoreCase(playerKit.getName())) {
					nameKit = ChatColor.BLUE + ChatColor.stripColor(nameKit);
				}
				im.setDisplayName(nameKit);
				
				if(!PlayersManager.getInstance().playerIsVip(player) && kit.isVipKit()) {
					lore.add("");
					lore.add(Translation.getString("MUST_VIP"));
				}
				im.setLore(lore);
				is.setItemMeta(im);
				inv.setItem(kit.getSlot(), is);
			}
			catch(NullPointerException e) {}
		}
		// Shop
		Utils.updateInterface(player);
		
		// L'armure
		TeamManager.getInstance().setPlayerArmor(player);
		
		if(BGPlayers.isPlayer(player) && Game.getInstance().getStatus() == GameStatus.GAME) {
			// Le kit
			int i = 0;
			for(ItemStack is : KitManager.getInstance().getPlayerKit(player.getName()).getItems()) {
				try {
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(WeaponManager.getInstance().getWeapon(is.getType()).getName());
					is.setItemMeta(im);
				}
				catch(NullPointerException e) {}
				inv.setItem(i, is);
				i++;
			}
		}
		
		player.updateInventory();
	}
	
	public static boolean givePlayerWeapon(Player player, ItemStack is) {
		for(int i = 0; i < 6; i++) {
			if(player.getInventory().getItem(i) == null) {
				player.getInventory().setItem(i, is);
				return true;
			}
		}
		return false;
	}
	
	public static boolean canGivePlayerWeapon(Player player, Material weapon) {
		for(int i = 0; i < 6; i++) {
			if(player.getInventory().getItem(i) == null) {
				return true;
			}
			else if(player.getInventory().getItem(i).getType() == weapon) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean canStackPlayerWeapon(Player player, Material weapon) {
		for(int i = 0; i < 6; i++) {
			if(player.getInventory().getItem(i) != null) {
				if(player.getInventory().getItem(i).getType() == weapon) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean giveOrStackPlayerWeapon(Player player, ItemStack is) {
		for(int i = 0; i < 6; i++) {
			if(player.getInventory().getItem(i) != null) {
				if(player.getInventory().getItem(i).getType() == is.getType()) { // On stack en priorité
					player.getInventory().getItem(i).setAmount(is.getAmount() + player.getInventory().getItem(i).getAmount());
					return true;
				}
			}
		}
		// Si on est ici on a pas pus stack et on verifie si il y a une case vide
		for(int i = 0; i < 6; i++) {
			if(player.getInventory().getItem(i) == null) {
				if(player.getInventory().getItem(i).getType() == is.getType()) { // On stack en priorité
					player.getInventory().setItem(i, is);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static GunScore[] scoreDesc(GunScore[] scores) {
		int i = 0;
		int j = 0;
		
		while(j < scores.length) {
			if(scores[i].getKill() > scores[j].getKill()) {
				// Permutte
				GunScore tmpi = scores[i];
				scores[i] = scores[j];
				scores[j] = tmpi;
				
				i = j;
			}
			else {
				i += 1;
				if(i >= scores.length) {
					j += 1;
					i = j;
				}
			}
		}
		return scores;
	}
	
	public static Long msToTick(Long ms) {
		return ms*20 / 1000;
	}
	
	public static Location getLocation(ConfigurationSection section) {
		try {			
			Location location = new Location(Bukkit.getWorld(section.getString("world")),
					section.getDouble("x"), section.getDouble("y"), section.getDouble("z"),
					(float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
			return location;
		}
		catch(Exception e) {}
		
		return null;
	}
	
	public static void setLocation(ConfigurationSection section, Location loc) {		
		try {
			section.set("world", loc.getWorld().getName());
			section.set("x", loc.getX());
			section.set("y", loc.getY());
			section.set("z", loc.getZ());
			section.set("yaw", loc.getYaw());
			section.set("pitch", loc.getPitch());
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public static PlayerDistance[] getPlayerNear(Player player, boolean ignoreRespawner) {
		PlayerDistance[] distances = new PlayerDistance[BGPlayers.getPlayers().size() -1];
		int i = 0;
    	for (Player online : BGPlayers.getPlayers()) {
    		if(!online.getName().equals(player.getName())) {
    			if(TeamManager.getInstance().areEnnemis(player, online)) {
    				if(!(ignoreRespawner && RespawnTimerManager.getInstance().getRespawner(online) != null)) {
    					distances[i] = new PlayerDistance(online, player.getLocation().distance(online.getLocation()));
    					i++;
    				}
    			}
    		}
    	}
    	
    	i = 0;
		int j = 0;
		
		while(j < distances.length) {
			if(distances[i].getDistance() < distances[j].getDistance()) {
				// Permutte
				PlayerDistance tmpi = distances[i];
				distances[i] = distances[j];
				distances[j] = tmpi;
				
				i = j;
			}
			else {
				i += 1;
				if(i >= distances.length) {
					j += 1;
					i = j;
				}
			}
		}
		return distances;
	}
	
	public static MetadataValue getMetadataValue(String key, Metadatable meta) {
		if(!meta.hasMetadata(key)) return null;
		
		for(MetadataValue value : meta.getMetadata(key)) {
			if(value.getOwningPlugin().getName().equalsIgnoreCase(MineGun.getPluginInstance().getName())) {
				return value;
			}
		}
		return null;
	}
	
	public static class PlayerDistance {
		private Player m_player;
		private Double m_distance;
		
		public PlayerDistance(Player player, Double distance) {
			m_player = player;
			m_distance = distance;
		}
		
		public Player getPlayer() { return m_player; }
		public Double getDistance() { return m_distance; }
	}
}
