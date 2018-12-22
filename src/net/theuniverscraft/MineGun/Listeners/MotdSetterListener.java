package net.theuniverscraft.MineGun.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdSetterListener implements Listener {
	private String m_motd;
	
	private static MotdSetterListener instance = null;
	public static MotdSetterListener getInstance() {
		if(instance == null) instance = new MotdSetterListener("...");
		return instance;
	}
	
	private MotdSetterListener(String motd) {
		m_motd = motd;
	}
	
	public void setMotd(String motd) { m_motd = motd; }
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		event.setMotd(m_motd);
	}
}
