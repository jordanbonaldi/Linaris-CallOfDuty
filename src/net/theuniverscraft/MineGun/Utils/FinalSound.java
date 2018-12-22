package net.theuniverscraft.MineGun.Utils;

import org.bukkit.Location;
import org.bukkit.Sound;

public class FinalSound {
	Sound m_sound;
	Long m_volume;
	Long m_pitch;
	
	public FinalSound(Sound sound, Long volume, Long pitch) {
		m_sound = sound;
		m_volume = volume;
		m_pitch = pitch;
	}
	
	public void playSound(Location loc) {
		loc.getWorld().playSound(loc, m_sound, m_volume, m_pitch);
	}
}