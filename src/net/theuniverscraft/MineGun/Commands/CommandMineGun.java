package net.theuniverscraft.MineGun.Commands;

import net.theuniverscraft.MineGun.Managers.ConfigurationManager;
import net.theuniverscraft.MineGun.Managers.GameConfig;
import net.theuniverscraft.MineGun.Managers.GameTimerManager;
import net.theuniverscraft.MineGun.Managers.LobbyManager;
import net.theuniverscraft.MineGun.Managers.PlayersManager;
import net.theuniverscraft.MineGun.Managers.SpawnPointManager;
import net.theuniverscraft.MineGun.Managers.Translation;
import net.theuniverscraft.MineGun.Utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMineGun implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.isOp()) {
			sender.sendMessage(Translation.getString("ONLY_OP"));
			return true;
		}
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("startconfig")) {
				if(ConfigurationManager.getInstance().setIsInConfig(true))
					sender.sendMessage(Translation.getString("START_CONFIG"));
				else
					sender.sendMessage(ChatColor.DARK_RED+"Error !");
			}
			else if(args[0].equalsIgnoreCase("stopconfig")) {
				if(ConfigurationManager.getInstance().setIsInConfig(false))
					sender.sendMessage(Translation.getString("STOP_CONFIG"));
				else
					sender.sendMessage(Translation.getString("ALL_ZONE_NOT_DEFINED"));
			}
			else if(args[0].equalsIgnoreCase("setlobby")) {
				if(!(sender instanceof Player)) return false;
				Player player = (Player) sender;
				LobbyManager.getInstance().setLobby(player.getLocation());
				
				player.sendMessage(Translation.getString("SET_LOBBY"));
			}
			else if(args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("go")) {
				// Game.getInstance().setStatus(GameStatus.GAME);
				GameTimerManager.getInstance().shortTimer();
			}
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("givemoney")) {
				if(!(sender instanceof Player)) return false;
				Player player = (Player) sender;
				
				try {
					Integer amount = Integer.parseInt(args[1]);
					PlayersManager.getInstance().addPlayerPoints(player, amount);
					Utils.updateInterface(player);
				}
				catch(Exception e) {
					
				}
			}
			else if(args[0].equalsIgnoreCase("setzone") || args[0].equalsIgnoreCase("setspawnpoint")) {
				if(!(sender instanceof Player)) return false;
				Player player = (Player) sender;
				
				try {
					Integer idZone = Integer.parseInt(args[1]);
					idZone--; // Pour avoir un id à partir de 0;
					
					if(idZone < 0) {
						player.sendMessage(Translation.getString("SMALL_NUMBER"));
						return true;
					}
					else if(idZone >= GameConfig.SPAWN_POINTS_NUMBER) {
						player.sendMessage(Translation.getString("BIG_NUMBER"));
						return true;
					}
					
					SpawnPointManager.getInstance().saveZone(idZone, player.getLocation());
					
					player.sendMessage(Translation.getString("ZONE_DEFINE"));
				}
				catch(NumberFormatException e) {
					player.sendMessage(ChatColor.DARK_RED+"/"+label+" "+args[0]+" <id spawn point> (id a partir de 1)");
				}
			}
		}
		return true;
	}
}
