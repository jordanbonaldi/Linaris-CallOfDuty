package net.theuniverscraft.MineGun.Managers;

import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Game;
import net.theuniverscraft.MineGun.GameStatus;
import net.theuniverscraft.MineGun.Background.BGChat;
import net.theuniverscraft.MineGun.Utils.GunScore;
import net.theuniverscraft.MineGun.Utils.TeamScore;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class TeamManager {
	public final GunTeam m_ghosts;
	public final GunTeam m_federes;
	
	private static TeamManager instance = null;
	public static TeamManager getInstance() {
		if(instance == null) instance = new TeamManager();
		return instance;
	}
	
	public TeamManager() {
		m_ghosts = new GunTeam("Ghost", "Ghosts", ChatColor.WHITE, DyeColor.WHITE);
		m_federes = new GunTeam("Fédéré", "Fédérés", ChatColor.BLACK, DyeColor.BLACK);
	}
	
	public boolean areEnnemis(Player player1, Player player2) {
		if(GameConfig.SOLO_MODE) return true;
		
		if((m_ghosts.isInTeam(player1) && m_ghosts.isInTeam(player2)) || 
				(m_federes.isInTeam(player1) && m_federes.isInTeam(player2))) {
			return false;
		}
		return true;
	}
	
	public GunTeam getWinner() {
		TeamScore ghosts = new TeamScore(m_ghosts, 0);
		TeamScore federes = new TeamScore(m_federes, 0);
		
		for(GunScore score : ScoreManager.getInstance().getScores()) {
			if(m_ghosts.isInTeam(score.getPlayer())) {
				ghosts.addKill(score.getKill());
			}
			else if(m_federes.isInTeam(score.getPlayer())) {
				federes.addKill(score.getKill());
			}
		}
		
		if(ghosts.getKill() > federes.getKill()) {
			return m_ghosts;
		}
		else {
			return m_federes;
		}
	}
	
	public void showScoresTeams() {
		TeamScore ghosts = new TeamScore(m_ghosts, 0);
		TeamScore federes = new TeamScore(m_federes, 0);
		
		for(GunScore score : ScoreManager.getInstance().getScores()) {
			if(m_ghosts.isInTeam(score.getPlayer())) {
				ghosts.addKill(score.getKill());
			}
			else if(m_federes.isInTeam(score.getPlayer())) {
				federes.addKill(score.getKill());
			}
		}
		
		BGChat.sendAll(ChatColor.AQUA+"------------ "+ChatColor.GOLD+"Team"+ChatColor.AQUA+" ------------");
		if(ghosts.getKill() > federes.getKill()) {
			BGChat.sendAll(ChatColor.DARK_RED+"1"+ChatColor.WHITE+". "+m_ghosts.getNamePlur()+ChatColor.WHITE+
					" : "+ChatColor.AQUA+ghosts.getKill()+ChatColor.GOLD+" kills");
			
			BGChat.sendAll(ChatColor.RED+"2"+ChatColor.WHITE+". "+m_federes.getNamePlur()+ChatColor.WHITE+
					" : "+ChatColor.AQUA+federes.getKill()+ChatColor.GOLD+" kills");
		}
		else {
			BGChat.sendAll(ChatColor.DARK_RED+"1"+ChatColor.WHITE+". "+m_federes.getNamePlur()+ChatColor.WHITE+
					" : "+ChatColor.AQUA+federes.getKill()+ChatColor.GOLD+" kills");
			
			BGChat.sendAll(ChatColor.RED+"2"+ChatColor.WHITE+". "+m_ghosts.getNamePlur()+ChatColor.WHITE+
					" : "+ChatColor.AQUA+ghosts.getKill()+ChatColor.GOLD+" kills");
		}
		BGChat.sendAll(ChatColor.AQUA+"------------------------------");
	}
	
	public DyeColor getPlayerDyeColor(Player player) {
		if(GameConfig.SOLO_MODE) {
			return DyeColor.RED;
		}
		
		if(m_ghosts.isInTeam(player)) {
			return m_ghosts.getDyeColor();
		}
		else if(m_federes.isInTeam(player)) {
			return m_federes.getDyeColor();
		}
		
		return DyeColor.RED;
	}
	
	public ChatColor getPlayerChatColor(Player player) {
		if(GameConfig.SOLO_MODE) {
			return ChatColor.RESET;
		}
		
		if(m_ghosts.isInTeam(player)) {
			return m_ghosts.getChatColor();
		}
		else if(m_federes.isInTeam(player)) {
			return m_federes.getChatColor();
		}
		
		return ChatColor.RESET;
	}
	
	public void affectPlayerTeam(Player player) {
		if(m_ghosts.getPlayers().size() > m_federes.getPlayers().size()) {
			m_federes.addPlayer(player);
		}
		else {
			m_ghosts.addPlayer(player);
		}
	}
	public void affectPlayerTeamMassive(List<Player> players) {
		int sizeGhosts = m_ghosts.getPlayers().size();
		int sizeFederes = m_federes.getPlayers().size();
		
		for(Player player : players) {
			if(sizeGhosts > sizeFederes) {
				m_federes.addPlayer(player);
				sizeFederes++;
			}
			else {
				m_ghosts.addPlayer(player);
				sizeGhosts++;
			}
		}
	}
	
	public void setPlayerArmor(Player player) {
		if(GameConfig.SOLO_MODE || Game.getInstance().getStatus() == GameStatus.LOBBY) {
			PlayerInventory inv = player.getInventory();
			inv.setHelmet(null);
			inv.setChestplate(null);
			inv.setLeggings(null);
			inv.setBoots(null);
		}
		else {
			if(m_ghosts.isInTeam(player)) {
				m_ghosts.setArmor(player);
			}
			else if(m_federes.isInTeam(player)) {
				m_federes.setArmor(player);
			}
		}
	}
	
	public GunTeam getPlayerTeam(Player player) {
		if(m_ghosts.isInTeam(player)) {
			return m_ghosts;
		}
		else if(m_federes.isInTeam(player)) {
			return m_federes;
		}
		
		return null;
	}
	
	public class GunTeam {
		private String m_nameSing;
		private String m_namePlur;
		private List<Player> m_players = new LinkedList<Player>();
		private ChatColor m_chatColor;
		private DyeColor m_dyeColor;
		
		private ItemStack m_helmet;
		private ItemStack m_chestplate;
		private ItemStack m_leggings;
		private ItemStack m_boots;
		
		private GunTeam(String nameSing, String namePlur, ChatColor chatColor, DyeColor dyeColor) {
			m_nameSing = nameSing;
			m_namePlur = namePlur;
			m_chatColor = chatColor;
			m_dyeColor = dyeColor;
			
			m_helmet = new ItemStack(Material.LEATHER_HELMET);
			LeatherArmorMeta metaHelmet = (LeatherArmorMeta) m_helmet.getItemMeta();
			metaHelmet.setColor(m_dyeColor.getColor());
			m_helmet.setItemMeta(metaHelmet);
			
			m_chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta metaChestplate = (LeatherArmorMeta) m_chestplate.getItemMeta();
			metaChestplate.setColor(m_dyeColor.getColor());
			m_chestplate.setItemMeta(metaChestplate);
			
			m_leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			LeatherArmorMeta metaLeggings = (LeatherArmorMeta) m_leggings.getItemMeta();
			metaLeggings.setColor(m_dyeColor.getColor());
			m_leggings.setItemMeta(metaLeggings);
			
			m_boots = new ItemStack(Material.LEATHER_BOOTS);
			LeatherArmorMeta metaBoots = (LeatherArmorMeta) m_boots.getItemMeta();
			metaBoots.setColor(m_dyeColor.getColor());
			m_boots.setItemMeta(metaBoots);
		}
		
		public void addPlayer(Player player) {
			m_players.add(player);
		}
		
		public List<Player> getPlayers() { return m_players; }
		
		public boolean isInTeam(Player player) {
			for(Player p : m_players) {
				if(player.getName().equals(p.getName())) return true;
			}
			return false;
		}
		
		public String getNameSing() { return m_chatColor+m_nameSing; }
		public String getNamePlur() { return m_chatColor+m_namePlur; }
		
		public ChatColor getChatColor() { return m_chatColor; }
		public DyeColor getDyeColor() { return m_dyeColor; }
		
		public void setArmor(Player player) {
			PlayerInventory inv = player.getInventory();
			inv.setHelmet(m_helmet);
			inv.setChestplate(m_chestplate);
			inv.setLeggings(m_leggings);
			inv.setBoots(m_boots);
		}
	}
}
