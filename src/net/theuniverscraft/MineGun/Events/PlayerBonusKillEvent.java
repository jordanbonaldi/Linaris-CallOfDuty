package net.theuniverscraft.MineGun.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerBonusKillEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private Integer m_kill;
	
	public PlayerBonusKillEvent(final Player who, Integer kill) {
		super(who);
		m_kill = kill;
	}	
	
	public Integer getBonusKill() {
		return m_kill;
	}
	
	public HandlerList getHandlers() {
		return handlers;
    }
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}