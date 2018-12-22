package net.theuniverscraft.MineGun.Events;

import net.theuniverscraft.MineGun.Archievements.PlayerAchievement;
import net.theuniverscraft.MineGun.Utils.ComboKill;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerMultiKillEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private PlayerAchievement m_pa;
	private ComboKill m_combo;
	
	public PlayerMultiKillEvent(final PlayerAchievement who, ComboKill combo) {
		super(who.getPlayer());
		m_pa = who;
		m_combo = combo;
	}
	
	public PlayerAchievement getPlayerAchievement() {
		return m_pa;
	}
	
	public ComboKill getMultiKill() {
		return m_combo;
	}
	
	public HandlerList getHandlers() {
		return handlers;
    }
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
