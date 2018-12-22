package net.theuniverscraft.MineGun.Managers;

import java.util.HashMap;

import org.bukkit.ChatColor;

public class Translation {	
	private HashMap<String, String> lang = new HashMap<String, String>();
	
	/*private File file;
	private FileConfiguration fileLang;*/
	
	private static Translation instance = null;
	public static String getString(String key) {
		if(instance == null) instance = new Translation();
		return instance.getStringLocale(key);
	}
	
	private Translation() {
		try {
			// Valeurs par defaut
			lang.put("MUST_VIP", "&4Vous devez être VIP !");
			lang.put("ONLY_OP", "&4Vous devez être op !");
			lang.put("ONLY_OP_FOR_THE_MOMENT", "&4Seul les op peuvent se connecter pour le moment !");
			
			lang.put("BIG_NUMBER", "&4Nombre trop élevé !");
			lang.put("SMALL_NUMBER", "&4Nombre trop bas !");
			lang.put("ZONE_DEFINE", "&2Emplacement definie !");
			lang.put("ALL_ZONE_NOT_DEFINED", "&4Tous les points de spawn ne sont pas définis !");
			
			lang.put("START_CONFIG", "&2Mode configuration activé !");
			lang.put("STOP_CONFIG", "&2Mode configuration désactivé !");
			
			lang.put("YOU_HAVE_BUY", "&6Vous avez achetez &b<bonus> !");
			lang.put("YOU_HAVE_NOT_ENOUGH_POINT", "&cVous n'avez pas assez de points !");
			lang.put("YOU_TOO_LOT_WEAPON", "&cVous avez trop d'armes !");
			
			lang.put("YOU_RESPAWN_IN", "&aVous réapparaitrez dans &6<x> <unite> !");
			lang.put("YOU_HAVE_RESPAWN", "&6Vous venez de réapparaître !");
			lang.put("CHOOSE_KIT", "&6Votre classe sera active à votre prochaine réapparition !");
			
			lang.put("SET_LOBBY", "&2Lobby définit");
			lang.put("SET_TEXTURE_LOBBY", "&2Lobby texture définit");
			lang.put("NOT_START", "&4La parti n'est pas commençée");
			
			lang.put("START_IN_PLAIN", "&6La partie commence dans&b <x> <unite> !");
			lang.put("START_IN_NOT_PLAIN", "&6La partie commence dans&b <m> <unite_m> &6et&b <s> <unite_s> !");
			lang.put("GAME_START", "&6La partie commence !");
			
			lang.put("GAME_STARTED_IN_MODT_PLAIN_UNITE", "&bDebut : <m>m !");
			lang.put("GAME_STARTED_IN_MODT_NOT_PLAIN_UNITE", "&bDebut : <m>m<s>s !");
			lang.put("GAME_ENDED_IN_MODT_PLAIN_UNITE", "&4En cours <m>m !");
			lang.put("GAME_ENDED_IN_MODT_NOT_PLAIN_UNITE", "&4En cours <m>m !");
			lang.put("GAME_END_MOTD", "&4Partie fini !");
			
			lang.put("END_IN_PLAIN", "&6La partie se termine dans &b<x> <unite> !");
			lang.put("GAME_END_SOLO", "&b<player>&6 gagne la partie !");
			lang.put("NO_WIN", "&6Il n'y a aucun gagnant !");
			lang.put("GAME_END_TEAM", "&6Les &b<team>&6 gagnent la partie !");
			
			lang.put("PLAYER_DONT_CO", "&cLe joueur n'est plus connecté !");
			lang.put("YOU_HAVE_DRONE", "&6Vous avez lancé un &b<drone_name> !");
			lang.put("COMPASS_NAME", "&b<player> &f-&6 Distance: &b<distance>m &f- &6Temps: &b<time>");
			
			lang.put("NOT_ANY_PEOPLE", "&cPas assez de personnes connectées !");
			lang.put("SHOP_ONLY_IN_GAME", "&cLe shop est disponible seulement en jeu !");
			
			lang.put("DOUBLE_KILL", "&7&lDouble kill !");
			lang.put("TRIPLE_KILL", "&7&lTriple kill !");
			lang.put("QUADRUPLE_KILL", "&7&lQuadruple kill !");
			
			lang.put("KICK_END_GAME", "&bLa partie est fini !");
			lang.put("KICK_NOT_VIP_GAME", "&4Vous devez être VIP pour rejoindre une partie en cours !");
			lang.put("KICK_NOT_VIP_FULL", "&4Le serveur est plein, devenez VIP pour rejoindre le serveur !");
			lang.put("KICK_GAME_IS_END", "&4Le jeu est fini !");
			
			
			lang.put("ACH_KILL_ENNEMIS", "Tuer un adversaires (<kill>/<max_kill>)");
			lang.put("ACH_DOUBLE_KILL", "Faire un double kill (<kill>/<max_kill>)");
			lang.put("ACH_TRIPLE_KILL", "Faire un triple kill (<kill>/<max_kill>)");
			lang.put("ACH_QUADRUPLE_KILL", "Faire un quadruple kill (<kill>/<max_kill>)");

			lang.put("ACH_WEAPON_KILL", "Tuer au <weapon> (<kill>/<max_kill>)");
			lang.put("ACH_KIT_KILL", "Tuer avec la classe <class> (<kill>/<max_kill>)");
			
			lang.put("ACH_UNLOCK", "&6<player> a débloqué le grade &b<ach>");
			
			/*file = new File("plugins/BukkitGame_TheUniversCraft/lang.yml");
			if(!file.exists()) file.createNewFile();
			fileLang = YamlConfiguration.loadConfiguration(file);
			
			HashMap<String, String> tmpLang = new HashMap<String, String>();
			
			Iterator<String> i = lang.keySet().iterator();
			while (i.hasNext()){
				String key = i.next();
				if(fileLang.contains(key)) {
					tmpLang.put(key, fileLang.getString(key));
				}
				else {
					fileLang.set(key, lang.get(key));
					tmpLang.put(key, lang.get(key));
				}
			}
			
			lang = tmpLang;
			
			fileLang.save(file);*/
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getStringLocale(String key) {
		return ChatColor.translateAlternateColorCodes('&', lang.get(key));
	}
}
