package net.theuniverscraft.MineGun.Utils;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;

public class WeaponSound {
	List<FinalSound> m_sounds = new LinkedList<FinalSound>();
	
	public WeaponSound(FinalSound... sounds) {
		for(FinalSound sound : sounds) {
			m_sounds.add(sound);
		}
	}
	
	public void playSounds(Location loc) {
		for(FinalSound sound : m_sounds) {
			sound.playSound(loc);
		}
	}
}
