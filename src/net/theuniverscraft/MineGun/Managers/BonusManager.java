package net.theuniverscraft.MineGun.Managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.MineGun.Bonus.Bonus;
import net.theuniverscraft.MineGun.Bonus.BonusForce;
import net.theuniverscraft.MineGun.Bonus.BonusGhost;
import net.theuniverscraft.MineGun.Bonus.BonusJump;
import net.theuniverscraft.MineGun.Bonus.BonusSpeed;
import net.theuniverscraft.MineGun.Bonus.BonusVip;
import net.theuniverscraft.MineGun.Bonus.ButtonMuno;
import net.theuniverscraft.MineGun.Bonus.ButtonSlot;
import net.theuniverscraft.MineGun.Bonus.ButtonWeapon;
import net.theuniverscraft.MineGun.Bonus.HealBonus;
import net.theuniverscraft.MineGun.Managers.DbManager.OfflineBonus;
import net.theuniverscraft.MineGun.Utils.ItemStackBuilder;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BonusManager {
	public List<Bonus> m_bonus = new LinkedList<Bonus>();
	public List<ButtonSlot> m_button = new LinkedList<ButtonSlot>();
	public HashMap<String, LinkedList<PlayerBonus>> m_playersBonus = new HashMap<String, LinkedList<PlayerBonus>>();
	
	private static BonusManager instance = null;
	public static BonusManager getInstance() {
		if(instance == null) instance = new BonusManager();
		return instance;
	}
	
	private BonusManager() {
		List<String> lore = new LinkedList<String>();
		lore.add("+ 10 coeurs");
		lore.add("+ coeurs au max");
		lore.add("- durée : 3 vies");
		m_bonus.add(new HealBonus("Heal 1", 10, lore, 0, new ItemStack(Material.GRILLED_PORK), 20D, 1));
		
		lore.clear();
		lore.add("+ 20 coeurs");
		lore.add("+ coeurs au max");
		lore.add("- durée : 3 vies");
		m_bonus.add(new HealBonus("Heal 2", 30, lore, 9, new ItemStack(Material.PORK), 40D, 2));
		
		lore.clear();
		lore.add("+ 30 coeurs");
		lore.add("+ coeurs au max");
		lore.add("- durée : 3 vies");
		m_bonus.add(new HealBonus("Heal 3", 50, lore, 18, new ItemStack(Material.POTATO_ITEM), 60D, 3));
		
		
		lore.clear();
		lore.add("+ Speed 1");
		lore.add("+ recharge 2 secondes"); // par default 2.5 sec
		lore.add("- durée : 3 vies");
		m_bonus.add(new BonusSpeed("Speed 1", 10, lore, 2, new ItemStack(Material.STRING), 1));
		
		lore.clear();
		lore.add("+ Speed 2");
		lore.add("+ recharge 1 seconde");
		lore.add("- durée : 3 vies");
		m_bonus.add(new BonusSpeed("Speed 2", 30, lore, 11, new ItemStack(Material.SUGAR), 2));
	
		lore.clear();
		lore.add("+ Speed 3");
		lore.add("+ recharge instantané");
		lore.add("- durée : 3 vies");
		m_bonus.add(new BonusSpeed("Speed 3", 50, lore, 20, new ItemStack(Material.WHEAT), 3));
		
		
		if(GameConfig.SOLO_MODE) {
			lore.clear();
			lore.add("+ Effet Ghost");
			lore.add("- durée : 3 vies");
			m_bonus.add(new BonusGhost("Ghost 1", 10, lore, 4, new ItemStackBuilder(Material.SKULL_ITEM).setData((short) 4), 0D, 1));
			
			lore.clear();
			lore.add("+ Effet Ghost");
			lore.add("+ 3 coeurs");
			lore.add("- durée : 3 vies");
			m_bonus.add(new BonusGhost("Ghost 2", 30, lore, 13, new ItemStack(Material.SKULL_ITEM), 6D, 2));
			
			lore.clear();
			lore.add("+ Effet Ghost");
			lore.add("+ 5 coeurs");
			lore.add("- durée : 3 vies");
			m_bonus.add(new BonusGhost("Ghost 3", 50, lore, 22, new ItemStackBuilder(Material.SKULL_ITEM).setData((short) 3), 10D, 3));
		}
		else {
			lore.clear();
			lore.add("+ Jump 1");
			lore.add("+ Aucun dégat de chute");
			lore.add("- durée : 3 vies");
			m_bonus.add(new BonusJump("Jump 1", 10, lore, 4, new ItemStack(Material.DIRT), 1));
			
			lore.clear();
			lore.add("+ Jump 2");
			lore.add("+ Aucun dégat de chute");
			lore.add("- durée : 3 vies");
			m_bonus.add(new BonusJump("Jump 2", 30, lore, 13, new ItemStack(Material.DIRT), 2));
			
			lore.clear();
			lore.add("+ Jump 3");
			lore.add("+ Aucun dégat de chute");
			lore.add("- durée : 3 vies");
			m_bonus.add(new BonusJump("Jump 3", 50, lore, 22, new ItemStack(Material.DIRT), 3));
		}		
		
		lore.clear();
		lore.add("+ Force 1");
		lore.add("+ 10% degats au gun");
		lore.add("+ 20% de lancé de grenade");
		lore.add("+ le couteau fais 20 dégats 2/5");
		m_bonus.add(new BonusForce("Force 1", 10, lore, 6, new ItemStack(Material.RAW_CHICKEN), 1));
		
		lore.clear();
		lore.add("+ Force 2");
		lore.add("+ 20% degats au gun");
		lore.add("+ 30% de lancé de grenade");
		lore.add("+ le couteau fais 20 dégats 3/5");
		m_bonus.add(new BonusForce("Force 2", 30, lore, 15, new ItemStack(Material.COOKED_CHICKEN), 2));

		lore.clear();
		lore.add("+ Force 3");
		lore.add("+ 30% degats au gun");
		lore.add("+ 40% de lancé de grenade");
		lore.add("+ le couteau fais 20 dégats 4/5");
		m_bonus.add(new BonusForce("Force 3", 50, lore, 24, new ItemStack(Material.CLAY_BALL), 3));
		
		
		
		lore.clear();
		lore.add("Recharger ses munitions");
		m_button.add(new ButtonMuno("&bMunitions", lore, 8, new ItemStack(Material.SEEDS)));
		
		lore.clear();
		lore.add("Acheter des armes");
		m_button.add(new ButtonWeapon("&bArmes", lore, 17, new ItemStack(Material.SADDLE)));
		
		lore.clear();
		lore.add("+ Avantage VIP");
		lore.add("- durée : 50 vies");
		m_bonus.add(new BonusVip("Vip", 1000, lore, 26, new ItemStackBuilder(Material.GOLDEN_APPLE).setData((short) 1), 1).setKillToRemove(50));
	}
	
	public List<Bonus> getAllBonus() {
		return m_bonus;
	}
	public Bonus getBonus(String bonusName) {
		for(Bonus bonus : m_bonus) {
			if(bonusName.contains(bonus.getName())) return bonus;
		}
		return null;
	}
	
	public List<ButtonSlot> getAllButton() {
		return m_button;
	}
	public ButtonSlot getButton(String buttonName) {
		for(ButtonSlot button : m_button) {
			if(buttonName.contains(button.getName())) return button;
		}
		return null;
	}
	
	public void addPlayerBonus(Player player, Bonus bonus) {
		PlayerBonus pb = new PlayerBonus(player, bonus);
		try {
			if(!m_playersBonus.containsKey(player.getName())) {
				LinkedList<PlayerBonus> nowBonus = new LinkedList<PlayerBonus>();
				pb.onActive();
				nowBonus.add(pb);
				m_playersBonus.put(player.getName(), nowBonus);
				return;
			}
			
			Bonus actuelBonus = playerGetBonusType(player, bonus.getClass()).getBonus();
			if(bonus.getLevel() > actuelBonus.getLevel()) {
				LinkedList<PlayerBonus> nowBonus = m_playersBonus.get(player.getName());
				
				for(int i = 0; i < nowBonus.size(); i++) {
					if(nowBonus.get(i).getBonus().getClass().equals(bonus.getClass())) {
						nowBonus.get(i).onDelete();
						nowBonus.remove(i);
						i--;
					}
				}
				
				nowBonus.add(pb);
				pb.onActive();
				m_playersBonus.put(player.getName(), nowBonus);
			}
			else {
				pb.onExist();
			}
		}
		catch(NullPointerException e) {
			LinkedList<PlayerBonus> nowBonus = m_playersBonus.get(player.getName());
			pb.onActive();
			nowBonus.add(pb);
			m_playersBonus.put(player.getName(), nowBonus);
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public void addPlayerBonus(Player player, OfflineBonus bonus) {
		LinkedList<PlayerBonus> nowBonus = new LinkedList<PlayerBonus>();
		if(m_playersBonus.containsKey(player.getName())) {
			nowBonus = m_playersBonus.get(player.getName());
		}
		
		PlayerBonus pb = new PlayerBonus(player, bonus.getBonus());
		pb.setKillToRemove(bonus.getKillToRemove());
		pb.onActive();
		nowBonus.add(pb);
		
		m_playersBonus.put(player.getName(), nowBonus);
	}
	
	
	public void removePlayerBonus(PlayerBonus bonus) {
		if(m_playersBonus.containsKey(bonus.getPlayer().getName())) {
			bonus.onDelete();
			
			LinkedList<PlayerBonus> nowBonus = m_playersBonus.get(bonus.getPlayer().getName());
			for(int i = 0; i < nowBonus.size(); i++) {
				if(bonus.getBonus().getName().contains(nowBonus.get(i).getBonus().getName())) {
					nowBonus.remove(i);
					i--;
				}
			}
			m_playersBonus.put(bonus.getPlayer().getName(), nowBonus);
		}
	}
	
	public PlayerBonus playerGetBonusType(Player player, Class<?> type) {
		if(m_playersBonus.containsKey(player.getName())) {
			for(PlayerBonus pb : m_playersBonus.get(player.getName())) {
				if(pb.getBonus().getClass().equals(type)) {
					return pb;
				}
			}
		}
		return null;
	}
	
	public LinkedList<PlayerBonus> getPlayerBonus(String player) {
		return m_playersBonus.containsKey(player) ? m_playersBonus.get(player) : null;
	}
	
	public void playerDeath(Player dead) {
		try {
			if(!m_playersBonus.containsKey(dead.getName())) return;
			LinkedList<PlayerBonus> tmpList = new LinkedList<PlayerBonus>(getPlayerBonus(dead.getName()));
			for(int i = 0; i < tmpList.size(); i++) {
				if(tmpList.get(i).getKillToRemove() <= 1) {
					// Remove bonus
					removePlayerBonus(tmpList.get(i));
				}
				else {
					tmpList.get(i).setKillToRemove(tmpList.get(i).getKillToRemove() - 1);
				}
			}
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public void playerRespawn(Player dead) {
		if(!m_playersBonus.containsKey(dead.getName())) return;
		for(PlayerBonus pb : m_playersBonus.get(dead.getName())) {
			pb.onReload();
		}
	}
	
	public LinkedList<PlayerBonus> getAllPlayerBonus(Player player) {
		LinkedList<PlayerBonus> allPlayerBonus = new LinkedList<PlayerBonus>();
		if(m_playersBonus.containsKey(player.getName())) {
			allPlayerBonus = m_playersBonus.get(player.getName());
		}
		return allPlayerBonus;
	}
	
	public void clearBonus() {
		Iterator<String> it = m_playersBonus.keySet().iterator();
		while(it.hasNext()) {
			String playerName = it.next();
			for(PlayerBonus pb : m_playersBonus.get(playerName)) {
				pb.onDelete();
			}
		}
		m_playersBonus.clear();
	}
	
	public void clearBonus(Player player) {
		if(!m_playersBonus.containsKey(player.getName())) return;
		
		for(PlayerBonus pb : m_playersBonus.get(player.getName())) {
			pb.onDelete();
		}
		m_playersBonus.remove(player.getName());
	}
	
	public class PlayerBonus {
		private Player m_player;
		private Bonus m_bonus;
		private Integer m_killToRemove;
		
		private PlayerBonus(Player player, Bonus bonus) {
			m_player = player;
			m_bonus = bonus;
			m_killToRemove = bonus.getKillToRemove();
		}
		
		public Player getPlayer() { return m_player; }
		public Bonus getBonus() { return m_bonus; }
		
		public void setKillToRemove(Integer killToRemove) { m_killToRemove = killToRemove; }
		public Integer getKillToRemove() { return m_killToRemove; }
		
		public void onActive() { m_bonus.onActive(m_player); }
		public void onExist() { m_bonus.onExist(m_player); }
		public void onReload() { m_bonus.onReload(m_player); }
		public void onDelete() { m_bonus.onDelete(m_player); }
	}
}
