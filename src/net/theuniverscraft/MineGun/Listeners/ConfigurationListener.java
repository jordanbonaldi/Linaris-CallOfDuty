package net.theuniverscraft.MineGun.Listeners;

import net.theuniverscraft.MineGun.Managers.Translation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class ConfigurationListener implements Listener {
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		if(!event.getPlayer().isOp()) {
			event.disallow(Result.KICK_OTHER, Translation.getString("ONLY_OP_FOR_THE_MOMENT"));
		}
	}
}
