package net.theuniverscraft.MineGun;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Background.BGChat;
import net.theuniverscraft.MineGun.Commands.CommandMineGun;
import net.theuniverscraft.MineGun.Listeners.BeforeGameListener;
import net.theuniverscraft.MineGun.Listeners.ConfigurationListener;
import net.theuniverscraft.MineGun.Listeners.DeadProtectionListener;
import net.theuniverscraft.MineGun.Listeners.KitInventoryListener;
import net.theuniverscraft.MineGun.Listeners.MotdSetterListener;
import net.theuniverscraft.MineGun.Listeners.PlayersListener;
import net.theuniverscraft.MineGun.Listeners.RadarInventoryListener;
import net.theuniverscraft.MineGun.Listeners.ShopInventoryListener;
import net.theuniverscraft.MineGun.Listeners.WeaponShopInventoryListener;
import net.theuniverscraft.MineGun.Listeners.WeaponsListener;
import net.theuniverscraft.MineGun.Listeners.WorldListener;
import net.theuniverscraft.MineGun.Managers.BonusManager;
import net.theuniverscraft.MineGun.Managers.ClearTimerManager;
import net.theuniverscraft.MineGun.Managers.ConfigurationManager;
import net.theuniverscraft.MineGun.Managers.DbManager;
import net.theuniverscraft.MineGun.Managers.GameConfig;
import net.theuniverscraft.MineGun.Managers.GameTimerManager;
import net.theuniverscraft.MineGun.Managers.GhostManager;
import net.theuniverscraft.MineGun.Managers.KillManager;
import net.theuniverscraft.MineGun.Managers.RespawnTimerManager;
import net.theuniverscraft.MineGun.Managers.ScoreManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MineGun extends JavaPlugin {
	private static MineGun pluginInstance = null;
	public static MineGun getPluginInstance() {
		return pluginInstance;
	}
	
	public void onEnable() {
		MineGun.pluginInstance = this;
		
		GameConfig.initConfig(this);
		
		GhostManager.getInstance();
		BonusManager.getInstance();
		DbManager.getInstance();
		RespawnTimerManager.getInstance();
		Game.getInstance();
		ClearTimerManager.getInstance();
		ScoreManager.getInstance();
		
		List<String> aliases = new LinkedList<String>();
		aliases.add("mg");
		aliases.add("gun");
		getCommand("minegun").setAliases(aliases);
		getCommand("minegun").setExecutor(new CommandMineGun());
		
		PluginManager pm = getServer().getPluginManager();
		
		if(ConfigurationManager.getInstance().isInConfig()) {
			pm.registerEvents(new ConfigurationListener(), this);
		}
		else {
			GameTimerManager.getInstance();
			pm.registerEvents(new WeaponsListener(), this);
			pm.registerEvents(new WorldListener(), this);
			pm.registerEvents(KillManager.getInstance(), this);
			pm.registerEvents(new PlayersListener(), this);
			pm.registerEvents(new KitInventoryListener(), this);
			pm.registerEvents(new ShopInventoryListener(), this);
			pm.registerEvents(new WeaponShopInventoryListener(), this);
			pm.registerEvents(new DeadProtectionListener(), this);
			pm.registerEvents(new BeforeGameListener(), this);
			pm.registerEvents(new RadarInventoryListener(), this);
		}
		pm.registerEvents(MotdSetterListener.getInstance(), this);
		
		for(Entity entity : Bukkit.getWorld("world").getEntities()) {
			if(!(entity instanceof Player)) {
				entity.remove();
			}
		}
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			try {
				DbManager.getInstance().restorePlayer(player);
			}
			catch(NullPointerException e) { e.printStackTrace(); }
		}
		
		BGChat.sendConsoleMessage(ChatColor.WHITE+"["+ChatColor.GOLD+this.getName()+ChatColor.WHITE+"] Enabled !");
	}
	public void onDisable() {		
		for(Player player : Bukkit.getOnlinePlayers()) {
			try {
				DbManager.getInstance().savePlayer(player);
			}
			catch(NullPointerException e) { e.printStackTrace(); }
		}
		BonusManager.getInstance().clearBonus();
		
		BGChat.sendConsoleMessage(ChatColor.WHITE+"["+ChatColor.GOLD+this.getName()+ChatColor.WHITE+"] Disabled !");
	}
}
