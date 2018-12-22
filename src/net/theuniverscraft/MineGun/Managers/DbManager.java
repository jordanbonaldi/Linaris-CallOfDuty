package net.theuniverscraft.MineGun.Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import net.theuniverscraft.MineGun.Game;
import net.theuniverscraft.MineGun.GameStatus;
import net.theuniverscraft.MineGun.Archievements.PlayerAchievement;
import net.theuniverscraft.MineGun.Background.BGPlayers;
import net.theuniverscraft.MineGun.Bonus.Bonus;
import net.theuniverscraft.MineGun.Managers.BonusManager.PlayerBonus;
import net.theuniverscraft.MineGun.Managers.KitManager.Kit;
import net.theuniverscraft.MineGun.Weapons.Weapons.Weapon;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class DbManager {
	private Connection connection;
	
	private final String bddVipName = GameConfig.BDD_PREFIX+"vips";
	private final String bddPlayersPoints = GameConfig.BDD_PREFIX+"playersPoints";
	private final String bddPlayersBonus = GameConfig.BDD_PREFIX+"playersBonus";
	private final String bddPlayersTexture = GameConfig.BDD_PREFIX+"playersTexture";
	private final String bddPlayersAch = GameConfig.BDD_PREFIX+"playersAchievements";
	
	private static DbManager dbManager = null;
	
	public static DbManager getInstance() {
		if(dbManager == null) {
			dbManager = new DbManager();
		}
		return dbManager;
	}
	public static void closeInstance() {
		if(dbManager != null) {
			try {
				dbManager.connection.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private DbManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://"+GameConfig.BDD_HOST+":"+
					GameConfig.BDD_PORT.toString()+
					"/"+GameConfig.BDD_NAME;
			String user = GameConfig.BDD_USER;
			String password = GameConfig.BDD_PASSWORD;
			
			connection = DriverManager.getConnection(url, user, password);
			Statement state = connection.createStatement();
			
			String sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS `").append(bddVipName).append("` (")
					.append("`id` int(11) NOT NULL AUTO_INCREMENT,")
					.append("`pseudo` varchar(50) NOT NULL,")
					.append("PRIMARY KEY (`id`),")
					.append("UNIQUE KEY `pseudo` (`pseudo`)")
					.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;").toString();
            
			state.executeUpdate(sql);
			
			
			sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS `").append(bddPlayersPoints).append("` (")
					.append("`id` int(11) NOT NULL AUTO_INCREMENT,")
					.append("`pseudo` varchar(50) NOT NULL,")
					.append("`points` int(11) NOT NULL,")
					.append("PRIMARY KEY (`id`),")
					.append("UNIQUE KEY (`pseudo`)")
					.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;").toString();
            
			state.executeUpdate(sql);
			
			
			sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS `").append(bddPlayersBonus).append("` (")
			.append("`id` int(11) NOT NULL AUTO_INCREMENT,")
			.append("`pseudo` varchar(50) NOT NULL,")
			.append("`bonusName` varchar(50) NOT NULL,")
			.append("`killToRemove` int(11) NOT NULL,")
			.append("PRIMARY KEY (`id`)")
			.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;").toString();
    
			state.executeUpdate(sql);
			
			
			sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS `").append(bddPlayersTexture).append("` (")
					.append("`id` int(11) NOT NULL AUTO_INCREMENT,")
					.append("`pseudo` varchar(50) NOT NULL,")
					.append("PRIMARY KEY (`id`),")
					.append("UNIQUE KEY `pseudo` (`pseudo`)")
					.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;").toString();
            
			state.executeUpdate(sql);
			
			
			sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS `").append(bddPlayersAch).append("` (")
					.append("`id` int(11) NOT NULL AUTO_INCREMENT,")
					.append("`pseudo` varchar(50) NOT NULL,")
					.append("`level` int(11) NOT NULL,")
					.append("`kills` int(11) NOT NULL,")
					.append("`double_kills` int(11) NOT NULL,")
					.append("`triple_kills` int(11) NOT NULL,")
					.append("`quadruple_kills` int(11) NOT NULL,")
					.append("`weapon_kills` text NOT NULL,")
					.append("`kit_kills` text NOT NULL,")
					.append("PRIMARY KEY (`id`),")
					.append("UNIQUE KEY `pseudo` (`pseudo`)")
					.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;").toString();
            
			state.executeUpdate(sql);
			
			state.close();
		}
		catch(NullPointerException e) {}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public void restorePlayer(Player player) {
		// Reset player
		player.setHealth(20D);
		player.setMaxHealth(20D);
		player.setFoodLevel(20);
		player.setExp(0F);
		player.setRemainingAir(20);
		
		for(PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
				
		// Achievements
		try {
			String sql = "SELECT * FROM "+bddPlayersAch+" WHERE pseudo=?";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player.getName());
			ResultSet result = state.executeQuery();
			
			if(result.next()) {
				PlayerAchievement pa = AchievementsManager.getInstance().getPlayerAchievement(player);
				pa.setLevel(result.getInt("level"));
				pa.setKills(result.getInt("kills"));
				pa.setDoubleKills(result.getInt("double_kills"));
				pa.setTripleKills(result.getInt("triple_kills"));
				pa.setQuadrupleKills(result.getInt("quadruple_kills"));
				
				String[] splitWeaponsKills = result.getString("weapon_kills").split(";");
				for(String weaponKills : splitWeaponsKills) {
					String[] splitWeaponKills = weaponKills.split(",");
					if(splitWeaponKills.length == 2) {
						Material mWeapon = Material.getMaterial(splitWeaponKills[0]);
						Integer kills = Integer.parseInt(splitWeaponKills[1]);
						
						pa.setWeaponKills(WeaponManager.getInstance().getWeapon(mWeapon), kills);
					}
				}
				
				String[] splitKitsKills = result.getString("kit_kills").split(";");
				for(String kitKills : splitKitsKills) {
					String[] splitKitKills = kitKills.split(",");
					if(splitKitKills.length == 2) {
						Integer kills = Integer.parseInt(splitKitKills[1]);
						pa.setKitKills(KitManager.getInstance().getKit(splitKitKills[0]), kills);
					}
				}
			}
			
			state.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		// Players Vip / PlayersManager
		PlayersManager.getInstance().initPlayer(player);
		try {
			String sql = "SELECT * FROM "+bddVipName+" WHERE pseudo=?";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player.getName());
			ResultSet result = state.executeQuery();
			
			if(result.next()) {
				PlayersManager.getInstance().addVip(player);
			}
			
			state.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Players Points
		try {
			String sql = "SELECT * FROM "+bddPlayersPoints+" WHERE pseudo=?";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player.getName());
			ResultSet result = state.executeQuery();
			
			if(result.next()) {
				PlayersManager.getInstance().setPlayerPoints(player, result.getInt("points"));
			}
			
			state.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// GhostManager
		GhostManager.getInstance().addPlayerInTeam(player);
		
		if(Game.getInstance().getStatus() == GameStatus.GAME) {
			restoreForTheGame(player);
		}
	}
	
	public void restoreForTheGame(Player player) {
		// Player Bonus
		try {
			String ignoredBonus = GameConfig.SOLO_MODE ? "Jump" : "Ghost";
					
			String sql = "SELECT * FROM `"+bddPlayersBonus+"` WHERE pseudo=? AND bonusName NOT LIKE ?";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player.getName());
			state.setString(2, ignoredBonus);
			ResultSet result = state.executeQuery();
			
			while(result.next()) {
				BonusManager.getInstance().addPlayerBonus(player, new OfflineBonus(
						BonusManager.getInstance().getBonus(result.getString("bonusName")), result.getInt("killToRemove")));
			}
				
			state.close();
					
			state = connection.prepareStatement("DELETE FROM `"+bddPlayersBonus+"` WHERE pseudo=? AND bonusName NOT LIKE ?");
			state.setString(1, player.getName());
			state.setString(2, ignoredBonus);
			state.executeUpdate();
			state.close();
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public boolean isVip(OfflinePlayer player) {
		boolean isVip = false;
		try {
			String sql = "SELECT * FROM "+bddVipName+" WHERE pseudo=?";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player.getName());
			ResultSet result = state.executeQuery();
			
			if(result.next()) {
				isVip = true;
			}
			
			state.close();
		} catch(Exception e) { e.printStackTrace(); }
		
		return isVip;
	}
	
	
	/*///////////////////////////////////// /|\ //////////////////// /|\ ///////////////////////////////////////
	///////////////////////////////////////  |  //  RESTAURATION  //  |  ///////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////  |  ///// SAUVEGARDE  //  |  ///////////////////////////////////////
	//////////////////////////////////////  \|/ //////////////////// \|/ /////////////////////////////////////*/
	
	public void savePlayer(Player player) {
		// Achievements
		try {
			PlayerAchievement pa = AchievementsManager.getInstance().getPlayerAchievement(player);
			int level = pa.getAchievement().getLevel();
			int kills = pa.getKills();
			int double_kills = pa.getDoubleKills();
			int triple_kills = pa.getTripleKills();
			int quadruple_kills = pa.getQuadrupleKills();
			
			StringBuilder builderWeapon = new StringBuilder();
			
			HashMap<Weapon, Integer> weaponsKills = pa.getWeaponsKills();
			Iterator<Weapon> itWeapon = weaponsKills.keySet().iterator();
			
			int i = 0;
			while(itWeapon.hasNext()) {
				Weapon weapon = itWeapon.next();
				Integer weaponKills = weaponsKills.get(weapon);
				
				if(i > 0) builderWeapon.append(";");
				builderWeapon.append(weapon.getMaterial().toString()).append(",").append(weaponKills);
				i++;
			}
			
			String weapon_kills = builderWeapon.toString();
			
			
			
			StringBuilder builderKit = new StringBuilder();
			
			HashMap<Kit, Integer> kitsKills = pa.getKitsKills();
			Iterator<Kit> itKit = kitsKills.keySet().iterator();
			
			i = 0;
			while(itKit.hasNext()) {
				Kit kit = itKit.next();
				Integer kitKills = kitsKills.get(kit);
				
				if(i > 0) builderKit.append(";");
				builderKit.append(ChatColor.stripColor(kit.getName())).append(",").append(kitKills);
				i++;
			}			
			String kit_kills = builderKit.toString();
			
			
			String sql = "INSERT INTO `"+bddPlayersAch+"`(`pseudo`,`level`,`kills`,`double_kills`,`triple_kills`," +
					"`quadruple_kills`,`weapon_kills`,`kit_kills`) VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE level=?," +
					"kills=?,double_kills=?, triple_kills=?, quadruple_kills=?, weapon_kills=?, kit_kills=?;";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player.getName());
			
			state.setInt(2, level);
			state.setInt(3, kills);
			state.setInt(4, double_kills);
			state.setInt(5, triple_kills);
			state.setInt(6, quadruple_kills);
			state.setString(7, weapon_kills);
			state.setString(8, kit_kills);
			
			state.setInt(9, level);
			state.setInt(10, kills);
			state.setInt(11, double_kills);
			state.setInt(12, triple_kills);
			state.setInt(13, quadruple_kills);
			state.setString(14, weapon_kills);
			state.setString(15, kit_kills);
			
			
			state.executeUpdate();
			
			state.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Players Vip / PlayersManager
		PlayersManager.getInstance().savePlayer(player);
		try {
			if(PlayersManager.getInstance().playerIsVip(player)) {
				String sql = "INSERT IGNORE INTO "+bddVipName+"(pseudo) VALUES(?)";
				PreparedStatement state = connection.prepareStatement(sql);
				
				state.setString(1, player.getName());
				state.executeUpdate();
				
				state.close();
			}
			else {
				String sql = "DELETE FROM "+bddVipName+" WHERE pseudo=?;";
				PreparedStatement state = connection.prepareStatement(sql);
				
				state.setString(1, player.getName());
				state.executeUpdate();
				
				state.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Player Points
		try {
			String sql = "INSERT INTO "+bddPlayersPoints+"(pseudo, points) VALUES(?,?) ON DUPLICATE KEY UPDATE points=?;";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player.getName());
			
			state.setInt(2, PlayersManager.getInstance().getPlayerPoints(player));
			state.setInt(3, PlayersManager.getInstance().getPlayerPoints(player));
			state.executeUpdate();
			
			state.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
				
		// Player Bonus
		for(PlayerBonus pb : BonusManager.getInstance().getAllPlayerBonus(player)) {
			try {
				PreparedStatement state = connection.prepareStatement("INSERT INTO `"+bddPlayersBonus+"`(`pseudo`, " +
						"`bonusName`, `killToRemove`) VALUES(?, ?, ?)");
				
				state.setString(1, pb.getPlayer().getName());
				state.setString(2, pb.getBonus().getName());
				state.setInt(3, pb.getKillToRemove());
				state.executeUpdate();
				
				state.close();
			}
			catch(Exception e) { e.printStackTrace(); }
		}
		BonusManager.getInstance().clearBonus(player);
		
		// Divers
		RespawnTimerManager.getInstance().clearRespawner(player);
		GhostManager.getInstance().removePlayerInTeam(player);
		BGPlayers.deletePlayer(player);
	}
	
	public class OfflineBonus {
		private Bonus m_bonus;
		private Integer m_killToRemove;
		
		private OfflineBonus(Bonus bonus, Integer killToRemove) {
			m_bonus = bonus;
			m_killToRemove = killToRemove;
		}
		
		public Bonus getBonus() { return m_bonus; }
		public Integer getKillToRemove() { return m_killToRemove; }
	}
}
