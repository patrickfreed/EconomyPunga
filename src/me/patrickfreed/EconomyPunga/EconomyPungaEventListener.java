package me.patrickfreed.EconomyPunga;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EconomyPungaEventListener implements Listener{

	public static HashMap<Player, Player> data = new HashMap<Player, Player>();

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.isCancelled()) return;
		if(!(event.getEntity() instanceof Player)) return;
		if(!(event instanceof EntityDamageByEntityEvent)){
			data.put((Player) event.getEntity(), null);
			return;
		}

		EntityDamageByEntityEvent newevent = (EntityDamageByEntityEvent) event;
			
		if(event.getCause().toString() == "ENTITY_ATTACK"){
			if(newevent.getDamager() instanceof Player){
				Player pvper = (Player) newevent.getDamager();
				data.put((Player) event.getEntity(), pvper);
			}else if(newevent.getDamager() instanceof Arrow){
				Player pvper = (Player) ((Arrow) newevent.getDamager()).getShooter();
				data.put((Player) event.getEntity(), pvper);
			}else if(newevent.getDamager() instanceof Monster){
				
			}else{
				data.put((Player) event.getEntity(), null);
			}
		}else{
			data.put((Player) event.getEntity(), null);
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		
		if(!(event.getEntity() instanceof Player))return;
		if(data.get((Player)event.getEntity()) == null) return;

		Player player = (Player)event.getEntity();
		Player pvper = data.get(player);
		            
			if ((player.hasPermission("economypunga.use") || player.isOp() || player.hasPermission("economyPunga.victim")) && (pvper.hasPermission("economypunga.use") || pvper.hasPermission("EconomyPunga.killer") || pvper.isOp())){

				double victimBalance = EconomyPunga.economy.getBalance(player.getName());
				
				double percentage;
				double amount;

				if (Config.getMode().compareToIgnoreCase("random percentage") == 0) {
					percentage = getRandomPercentage();
					amount = (percentage * victimBalance) / 100;
				} else if (Config.getMode().compareToIgnoreCase("static percentage") == 0) {
					percentage = Config.getStaticPercentage();
					amount = (percentage * victimBalance) / 100;
				} else if (Config.getMode().compareToIgnoreCase("Static Amount") == 0) {
					amount = Config.getStaticAmount();
				} else if (Config.getMode().compareToIgnoreCase("Random Amount") == 0) {
					amount = getRandomAmount();
				} else {
					System.err.println("Invalid Mode, defaulting to static amount of 1.");
					amount = 1;
				}

				if (victimBalance > amount) {
					EconomyPunga.economy.withdrawPlayer(player.getName(), amount);
					EconomyPunga.economy.depositPlayer(pvper.getName(), amount);
				} else if (victimBalance > 0) {
					EconomyPunga.economy.depositPlayer(pvper.getName(), victimBalance);
					amount = victimBalance;
					EconomyPunga.economy.withdrawPlayer(player.getName(), victimBalance);
				} else {
					amount = 0;
				}

				String Attacker = Config.getKillMsg().replace("%d", player.getName());
				String Dead = Config.getDeathmsg().replace("%d", player.getName());

				Attacker = Attacker.replace("%a", pvper.getName());
				Dead = Dead.replace("%a", pvper.getName());

				Attacker = Attacker.replace("%n", EconomyPunga.economy.format(amount));
				Dead = Dead.replace("%n", EconomyPunga.economy.format(amount));

				pvper.sendMessage(parseColors(Attacker));
				player.sendMessage(parseColors(Dead));
			}
		}

	public String parseColors(String message){	
		message = message.replace("&4", ChatColor.DARK_RED.toString());
		message = message.replace("&c", ChatColor.RED.toString());
		message = message.replace("&e", ChatColor.YELLOW.toString());
		message = message.replace("&6", ChatColor.GOLD.toString());
		message = message.replace("&2", ChatColor.DARK_GREEN.toString());
		message = message.replace("&a", ChatColor.GREEN.toString());
		message = message.replace("&b", ChatColor.AQUA.toString());
		message = message.replace("&3", ChatColor.DARK_AQUA.toString());
		message = message.replace("&1", ChatColor.DARK_BLUE.toString());
		message = message.replace("&9", ChatColor.BLUE.toString());
		message = message.replace("&d", ChatColor.LIGHT_PURPLE.toString());
		message = message.replace("&5", ChatColor.DARK_PURPLE.toString());
		message = message.replace("&f", ChatColor.WHITE.toString());
		message = message.replace("&7", ChatColor.GRAY.toString());
		message = message.replace("&8", ChatColor.DARK_GRAY.toString());
		message = message.replace("&0", ChatColor.BLACK.toString());	
		return message;	
	}

	public double getRandomAmount() {
		Random generator = new Random();
		double number = generator.nextInt(Config.getMaxA() + 1 - Config.getMinA()) + Config.getMinA();
		return number;
	}

	public double getRandomPercentage() {
		Random generator = new Random();
		double number = generator.nextInt(Config.getMaxP() + 1- Config.getMinP())+ Config.getMinP();
		return number;
	}
}