package me.patrickfreed.EconomyPunga;

public class Config {

	public static  String getMode() {
		return EconomyPunga.config.getString("mode", "Static Amount");
	}

	public static String getDeathmsg() {
		String deathmsg = EconomyPunga.config.getString("death-msg");
		return deathmsg;
	}

	public static String getKillMsg() {
		return EconomyPunga.config.getString("attacker-msg");
	}

	public static int getMaxP() {
		return EconomyPunga.config.getInt("max-percentage", 5);
	}

	public static int getMinP() {
		return EconomyPunga.config.getInt("min-percentage", 1);
	}

	public static int getMaxA() {
		return EconomyPunga.config.getInt("max-amount", 25);
	}

	public static int getMinA() {
		return EconomyPunga.config.getInt("min-amount", 1);
	}

	public static int getStaticAmount() {
		return EconomyPunga.config.getInt("static-amount", 15);
	}

	public static int getStaticPercentage() {
		return EconomyPunga.config.getInt("static-percentage", 1);
	}

}
