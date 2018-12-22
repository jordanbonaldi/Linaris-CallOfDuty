package net.theuniverscraft.MineGun.Listeners;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Game;
import net.theuniverscraft.MineGun.GameStatus;
import net.theuniverscraft.MineGun.Archievements.PlayerAchievement;
import net.theuniverscraft.MineGun.Background.BGPlayers;
import net.theuniverscraft.MineGun.Events.PlayerBonusKillEvent;
import net.theuniverscraft.MineGun.Events.PlayerMultiKillEvent;
import net.theuniverscraft.MineGun.Managers.AchievementsManager;
import net.theuniverscraft.MineGun.Managers.ClearTimerManager;
import net.theuniverscraft.MineGun.Managers.DbManager;
import net.theuniverscraft.MineGun.Managers.GameConfig;
import net.theuniverscraft.MineGun.Managers.KillManager;
import net.theuniverscraft.MineGun.Managers.LobbyManager;
import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.RadarTimerManager;
import net.theuniverscraft.MineGun.Managers.RespawnTimerManager;
import net.theuniverscraft.MineGun.Managers.TeamManager;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Managers.WeaponManager;
import net.theuniverscraft.MineGun.Utils.ComboKill;
import net.theuniverscraft.MineGun.Utils.Utils;
import net.theuniverscraft.MineGun.Weapons.Utils.PlayerRadar;
import net.theuniverscraft.MineGun.Weapons.Weapons.GunWeapon;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class PlayersListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerBonusKill(PlayerBonusKillEvent event) {
		Weapon weapon = null;
		if(event.getBonusKill() == 3) {
			weapon = WeaponManager.getInstance().getWeapon(Material.QUARTZ);
		}
		else if(event.getBonusKill() == 4) {
			weapon = WeaponManager.getInstance().getWeapon(Material.RECORD_8);
		}
		else if(event.getBonusKill() == 6) {
			weapon = WeaponManager.getInstance().getWeapon(Material.RECORD_7);
		}
		else if(event.getBonusKill() == 8) {
			weapon = WeaponManager.getInstance().getWeapon(Material.RECORD_9);
		}
		else {
			return;
		}
		
		ItemStack is = new ItemStack(weapon.getMaterial());
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(weapon.getName());
		is.setItemMeta(im);
		
		event.getPlayer().getInventory().setItem(7, is);
	}
	
	@EventHandler
	public void onPlayerMultiKill(PlayerMultiKillEvent event) {
		if(event.getMultiKill().equals(ComboKill.DOUBLE_KILL)) {
			event.getPlayerAchievement().onPlayerDoubleKill();
			event.getPlayer().sendMessage(Translation.getString("DOUBLE_KILL"));
		}
		else if(event.getMultiKill().equals(ComboKill.TRIPLE_KILL)) {
			event.getPlayerAchievement().onPlayerTripleKill();
			event.getPlayer().sendMessage(Translation.getString("TRIPLE_KILL"));
		}
		else if(event.getMultiKill().equals(ComboKill.QUADRUPLE_KILL)) {
			event.getPlayerAchievement().onPlayerQuadrupleKill();
			event.getPlayer().sendMessage(Translation.getString("QUADRUPLE_KILL"));
			KillManager.getInstance().resetMultiKill(event.getPlayer());
		}
	}
	
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Inventory inv = event.getEntity().getInventory();
		List<ItemStack> isDrop = new LinkedList<ItemStack>();
		for(int i = 0; i <= 6; i++) {
			if(inv.getItem(i) != null) {
				try { // Mettre les munitions qui tombent dans le cas d'un gun sinon : l'arme en elle même
					Weapon weapon = WeaponManager.getInstance().getWeapon(inv.getItem(i).getType());
					if(weapon instanceof GunWeapon) {
						GunWeapon gun = (GunWeapon) weapon;
						ItemStack is = gun.getItemStackCharger();
						is.setAmount(inv.getItem(i).getAmount());
						
						ItemMeta im = is.getItemMeta();
						im.setDisplayName("Muno");
						is.setItemMeta(im);
						
						isDrop.add(is);
					}
				}
				catch(NullPointerException e) {} // ce n'est pas un gun
			}
		}
		
		event.getDrops().clear();
		for(ItemStack is : isDrop) {
			event.getDrops().add(is);
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) { // ramasse
		if(event.isCancelled()) return;
		if(RespawnTimerManager.getInstance().getRespawner(event.getPlayer()) != null) {
			event.setCancelled(true);
			return;
		}
		
		ItemStack isPicked = event.getItem().getItemStack();
		if(!isPicked.getItemMeta().hasDisplayName()) {
			event.setCancelled(true);
			return;
		}
		
		String display = isPicked.getItemMeta().getDisplayName();
		if(display.contains("Muno")) { // muno
			GunWeapon gun = WeaponManager.getInstance().getGunWeaponByItemCharger(isPicked);
			
			ItemStack itemToGive = new ItemStack(gun.getMaterial());
			itemToGive.setAmount(isPicked.getAmount());
			
			if(Utils.canStackPlayerWeapon(event.getPlayer(), gun.getMaterial())) // On Give que si il a déjà le gun
				event.setCancelled(!Utils.giveOrStackPlayerWeapon(event.getPlayer(), itemToGive));
			else
				event.setCancelled(true);
		}
		else if(display.contains("Grenade")) {}
		else { event.setCancelled(true); }
		
		if(!event.isCancelled()) {
			// On clean l'inventaire les item non désiré
			Player player = event.getPlayer();
			ClearTimerManager.getInstance().clearPlayer(player);
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		
		if(p.getItemInHand().getType() != Material.AIR) {
			event.setCancelled(true);
		}
		else {
			ItemStack is = event.getItemDrop().getItemStack();
			p.getInventory().setItemInHand(is);
			event.getItemDrop().remove();
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
		if (event.getRegainReason() == RegainReason.SATIATED) event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		PlayerRadar playerRadar = RadarTimerManager.getInstance().getRadar(event.getPlayer());
		if(playerRadar != null) playerRadar.update();
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		/*if(Game.getInstance().getStatus() == GameStatus.GAME) {
			if(!DbManager.getInstance().isVip(event.getPlayer())) {
				event.disallow(Result.KICK_OTHER, Translation.getString("KICK_NOT_VIP_GAME"));
			}
		}*/
		if(Game.getInstance().getStatus() == GameStatus.END_GAME) {
			event.disallow(Result.KICK_OTHER, Translation.getString("KICK_GAME_IS_END"));
			return;
		}
		
		if(event.getResult() == Result.KICK_FULL) {
			if(DbManager.getInstance().isVip(event.getPlayer())) {
				event.allow();
			}
			else {
				event.setKickMessage(Translation.getString("KICK_NOT_VIP_FULL"));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {		
		if(Game.getInstance().getStatus() == GameStatus.GAME) {
			BGPlayers.addPlayers(event.getPlayer());
		}
		else {
			event.getPlayer().teleport(LobbyManager.getInstance().getLobby());
		}
		DbManager.getInstance().restorePlayer(event.getPlayer());
		Utils.setInv(event.getPlayer());
		
		event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + " a rejoint le jeu !");
	}
	
	@EventHandler public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(ChatColor.YELLOW + event.getPlayer().getName() + " a quitté le jeu !");
		onPlayerQuitOrKick(event.getPlayer());
	}
	@EventHandler public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(ChatColor.YELLOW + event.getPlayer().getName() + " a quitté le jeu !");
		onPlayerQuitOrKick(event.getPlayer());
	}
	
	public void onPlayerQuitOrKick(Player player) {
		DbManager.getInstance().savePlayer(player);
	}
		
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		if(event.getCause() == DamageCause.FALL) {
			if(player.hasPotionEffect(PotionEffectType.JUMP)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		PlayerAchievement pa = AchievementsManager.getInstance().getPlayerAchievement(event.getPlayer());
		int nbFleche = pa.getAchievement().getLevel() % 3 +1;
		
		int levelAch = pa.getAchievement().getLevel() / 3;
		ChatColor color;
		if(levelAch == 0) { color = ChatColor.AQUA; } 
		else if(levelAch == 1) { color = ChatColor.BLUE; } 
		else { color = ChatColor.DARK_BLUE; } 
		
		
		StringBuilder sb = new StringBuilder();
		sb.append(color);
		sb.append(ChatColor.BOLD);
		for(int i = 0; i < 3; i++) {
			if(i < nbFleche) sb.append(">");
			else sb.append(" ");
		}
		
		StringBuilder format = new StringBuilder();
		format.append("[");
		format.append(sb.toString());
		format.append(ChatColor.RESET+"] ");
		
		if(!GameConfig.SOLO_MODE) {
			if(BGPlayers.isPlayer(event.getPlayer())) {
				format.append("[");
				format.append(TeamManager.getInstance().getPlayerTeam(event.getPlayer()).getNameSing());
				format.append(ChatColor.RESET+"] ");
			}
		}
		
		if(PlayersManager.getInstance().playerIsVip(event.getPlayer())) {
			format.append(ChatColor.AQUA+"");
		}
		
		format.append("%1$s");
		format.append(ChatColor.RESET+" : %2$s");
		
		
		event.setFormat(format.toString());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(Game.getInstance().getStatus() != GameStatus.GAME) event.setCancelled(true);
	}
}
