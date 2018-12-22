package net.theuniverscraft.MineGun.Managers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class KitManager {
	private List<Kit> m_kits = new LinkedList<Kit>();
	private Kit m_defaultKit;
	
	private HashMap<String, Kit> m_playerKits = new HashMap<String, Kit>();
	
	private static KitManager instance = null;
	public static KitManager getInstance() {
		if(instance == null) instance = new KitManager();
		return instance;
	}
	
	private KitManager() {
		List<String> desc = new LinkedList<String>();
		List<ItemStack> kit = new LinkedList<ItemStack>();
		desc.add("- AK47");
		desc.add("- Desert Eagle");
		desc.add("- Une Grenade");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.DIAMOND_SPADE, 2));
		kit.add(new ItemStack(Material.WOOD_SPADE, 2));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_defaultKit = new Kit("&bAssaut", false, desc, new ItemStack(Material.DIAMOND_SPADE), 27, kit);
		m_kits.add(m_defaultKit);
		
		
		
		desc.clear();
		kit.clear();
		desc.add("- M16");
		desc.add("- Desert Eagle");
		desc.add("- Une Grenade");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.GOLD_SPADE, 2));
		kit.add(new ItemStack(Material.WOOD_SPADE, 2));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_kits.add(new Kit("&bLourd", false, desc, new ItemStack(Material.GOLD_SPADE), 28, kit));
		
		
		
		desc.clear();
		kit.clear();
		desc.add("- Desert Eagle");
		desc.add("- Une Grenade");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.WOOD_SPADE, 2));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_kits.add(new Kit("&bNinja", false, desc, new ItemStack(Material.WOOD_SPADE), 29, kit));
		
		
		
		desc.clear();
		kit.clear();
		desc.add("- MP7");
		desc.add("- Desert Eagle");
		desc.add("- Une Grenade");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.DIAMOND_PICKAXE, 2));
		kit.add(new ItemStack(Material.WOOD_SPADE, 2));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_kits.add(new Kit("&bFantôme", false, desc, new ItemStack(Material.DIAMOND_PICKAXE), 30, kit));
		
		
		
		desc.clear();
		kit.clear();
		desc.add("- Bazooka");
		desc.add("- Desert Eagle");
		desc.add("- 3 Grenades");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.WOOD_PICKAXE));
		kit.add(new ItemStack(Material.WOOD_SPADE, 2));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE, 3));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_kits.add(new Kit("&bGrenadier", true, desc, new ItemStack(Material.WOOD_PICKAXE), 31, kit));
		
		
		
		desc.clear();
		kit.clear();
		desc.add("- Sniper");
		desc.add("- Desert Eagle");
		desc.add("- Une Grenade");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.IRON_SPADE, 3));
		kit.add(new ItemStack(Material.WOOD_SPADE, 2));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_kits.add(new Kit("&bSniper", true, desc, new ItemStack(Material.IRON_SPADE), 32, kit));
		
		
		
		desc.clear();
		kit.clear();
		desc.add("- Fusil à pompe");
		desc.add("- Desert Eagle");
		desc.add("- Une Grenade");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.STONE_SPADE, 2));
		kit.add(new ItemStack(Material.WOOD_SPADE, 2));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_kits.add(new Kit("&bCampeur", true, desc, new ItemStack(Material.STONE_SPADE), 33, kit));
		
		
		
		desc.clear();
		kit.clear();
		desc.add("- AK47");
		desc.add("- MP7");
		desc.add("- 3 Grenades");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.DIAMOND_SPADE));
		kit.add(new ItemStack(Material.DIAMOND_PICKAXE));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE, 4));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_kits.add(new Kit("&bTueur", true, desc, new ItemStack(Material.FIREWORK_CHARGE), 34, kit));
		
		
		
		desc.clear();
		kit.clear();
		desc.add("- Sniper");
		desc.add("- Fusil à pompe");
		desc.add("- 4 Grenades");
		desc.add("- Couteau");
		
		kit.add(new ItemStack(Material.IRON_SPADE));
		kit.add(new ItemStack(Material.STONE_SPADE, 2));
		kit.add(new ItemStack(Material.FIREWORK_CHARGE, 3));
		kit.add(new ItemStack(Material.GOLD_SWORD));
		
		m_kits.add(new Kit("&bPlume", true, desc, new ItemStack(Material.GOLD_SWORD), 35, kit));
	}
	
	public List<Kit> getKits() {
		return m_kits;
	}
	
	public Kit getKit(String name) {
		for(Kit kit : m_kits) {
			if(kit.getName().contains(name)) return kit;
		}
		return null;
	}
	
	public void setPlayerKit(String player, Kit kit) {
		if(!m_playerKits.containsKey(player)) m_playerKits.put(player, kit);
		else {
			m_playerKits.remove(player);
			m_playerKits.put(player, kit);
		}
	}
	public boolean playerHasKit(String player) {
		return m_playerKits.containsKey(player);
	}
	public Kit getPlayerKit(String player) {
		if(m_playerKits.containsKey(player)) return m_playerKits.get(player);
		return getDefaultKit();
	}
	
	public Kit getDefaultKit() {
		return m_defaultKit;
	}
	
	public class Kit {
		private String m_name;
		private Boolean m_isVipKit;
		private List<String> m_desc;
		private ItemStack m_icon;
		private Integer m_slot;
		private List<ItemStack> m_kit;
		
		private Kit(String name, Boolean isVipKit, List<String> desc, ItemStack icon, Integer slot, List<ItemStack> kit) {
			m_name = name;
			m_isVipKit = isVipKit;
			m_icon = icon;
			m_slot = slot;
			m_kit = new LinkedList<ItemStack>(kit);
			
			m_desc = new LinkedList<String>(desc);
			for(int i = 0; i < m_desc.size(); i++) {
				m_desc.set(i, ChatColor.translateAlternateColorCodes('&', "&6"+m_desc.get(i)));
			}
		}
		
		public String getName() { return ChatColor.translateAlternateColorCodes('&',m_name); }
		
		public Boolean isVipKit() { return m_isVipKit; }
				
		public List<String> getDescription() {
			return m_desc;
		}
		
		public ItemStack getIcon() { return m_icon; }
		public Integer getSlot() { return m_slot; }
		
		public List<ItemStack> getItems() { return m_kit; }
		
		public boolean equals(Object object) {
			if(object == this) return true;
			if(!(object instanceof Kit)) return false;
			
			Kit kit = (Kit) object;
			if(m_icon.getType() == kit.getIcon().getType() && m_icon.getDurability() == kit.getIcon().getDurability())
				return true;
			
			return false;
		}
		
		public int hashCode() {
			int result = 7;
			final int multiplier = 17;
			
			result = multiplier*result + m_name.hashCode();
			
			return result;
		}
	}
}
