package me.patrickfreed.EconomyPunga;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyPunga extends JavaPlugin {
	public static YamlConfiguration config = null;
	public static Economy economy = null;
	
    @Override
	public void onEnable() {
		System.out.println("[" + this.getDescription().getName()+ "] EconomyPunga v." + this.getDescription().getVersion()+ " enabled!");
		File conf = makeConfig(new File(getDataFolder(), "config.yml"));
		
		if(conf.exists()){
			config = YamlConfiguration.loadConfiguration(new File("plugins/EconomyPunga", "config.yml"));
			System.out.println("[" + this.getDescription().getName() + "] Config loaded successfully!");
		}else{
			System.out.println("[" + this.getDescription().getName() + "] Error loading config, disabling...");
			this.getServer().getPluginManager().disablePlugin(this);	
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EconomyPungaEventListener(), this);
		
		if(!setupEconomy()){
			System.err.println("[EconomyPunga] Vault not found, disabling...");
			pm.disablePlugin(this);
		}
		
		try {
		    Metrics metrics = new Metrics();
		    metrics.beginMeasuringPlugin(this);
		} catch (IOException e) {
			System.err.println("[EconomyPunga]Statistics tracking failure");
		}
	}
	
	/**
	 * @author krinsdeath
	 */
    private File makeConfig(File file){
		if(!file.exists()) {
			System.out.println("[" + this.getDescription().getName() + "] Generating config...");
			new File(file.getParent()).mkdirs();
			InputStream in = EconomyPunga.class.getResourceAsStream("/resources/" + file.getName());
			if (in != null) {
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					byte[] buffer = new byte[2048];
					int length = 0;
					while ((length = in.read(buffer)) > 0){
						out.write(buffer, 0, length);
					}
				}catch (IOException e) {
					e.printStackTrace();
				}finally {
					try{
						in.close();
						out.close();
					}catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return file;
	}
	
    @Override
	public void onDisable() {
		System.out.println("[" + this.getDescription().getName() + "] " + this.getDescription().getName() + " v." + this.getDescription().getVersion() + " disabled!");
	}

    private Boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

}