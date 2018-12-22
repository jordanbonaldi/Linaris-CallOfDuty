package net.theuniverscraft.MineGun.Utils;


public class ComboKill {
	public static ComboKill DOUBLE_KILL = new ComboKill("double");
	public static ComboKill TRIPLE_KILL = new ComboKill("triple");
	public static ComboKill QUADRUPLE_KILL = new ComboKill("quadruple");
	
	private String m_name;
	
	private ComboKill(String name) {
		m_name = name;
	}
	
	public String getName() { return m_name; }
	
	public boolean equals(Object object) {
		if(object == this) return true;		
		if(!(object instanceof ComboKill)) return false;
		
		ComboKill comboKill = (ComboKill) object;
		if(comboKill.getName().equalsIgnoreCase(m_name))
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
