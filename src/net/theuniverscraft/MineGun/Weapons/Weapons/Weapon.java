package net.theuniverscraft.MineGun.Weapons.Weapons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Weapon {
	protected String m_name;
	protected Material m_material;
	protected Double m_damage;
	
	protected Integer m_weaponPrice;
	protected ItemStack m_isWeaponShop;
	
	public Weapon(String name, Material material, Double damage) {
		m_name = name;
		m_material = material;
		m_damage = damage;
		m_weaponPrice = 100;
		
		m_isWeaponShop = new ItemStack(m_material);
	}
	
	public Weapon setWeaponPrice(Integer price) {
		m_weaponPrice = price;
		return this;
	}
	public Integer getWeaponPrice() { return m_weaponPrice; }
	
	public Weapon setItemStackShop(ItemStack is) {
		m_isWeaponShop = is;
		return this;
	}
	public ItemStack getItemStackShop() { return m_isWeaponShop; }
	
	public String getName() { return ChatColor.translateAlternateColorCodes('&', m_name); }
	public Material getMaterial() { return m_material; }
	public Double getDamage() { return m_damage; }
	
	public boolean equals(Object other) {
		if(other == null) return false;
		if(!(other instanceof Weapon)) return false;
		if(this == other) return true;
		
		Weapon weapon = (Weapon) other;
		if(weapon.getMaterial() == m_material) return true;
		
		return false;
	}
	
	public int hashCode() {
		int result = 7;
		final int multiplier = 17;
		
		result = multiplier*result + m_material.hashCode();
		
		return result;
	}
}
