package com.stinkymonkey.cardshop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin implements CommandExecutor {
	public static Economy econ = null;
	
	private static gui gi;
	
	public static HashMap<String, Double> price = new HashMap<String, Double>();
	public static HashMap<Integer, String> cardSlot = new HashMap<Integer, String>();
	public static HashMap<Integer, String> catSlot = new HashMap<Integer, String>();
	// item name, category name
	public static HashMap<String, String> category = new HashMap<String, String>();
	// Display Name 2 Card
	public static HashMap<String, String> name2Name = new HashMap<String, String>();
	public static List<String> catNames = new ArrayList<String>();
	@Override
	public void onEnable() {
		setupEconomy();
		loadConfig();
		readConfig();
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new listener(this), this);
		getCommand("cardshop").setExecutor(this);
		gi = new gui(this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			return false;
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public void loadConfig() {
		File pluginFolder = new File("plugins" + System.getProperty("file.separator") + this.getDescription().getName());
		if (pluginFolder.exists() == false) {
    		pluginFolder.mkdir();
    		System.out.println("[Monkey-CS] CREATED A FOLDER");
    	}
		
		File configFile = new File("plugins" + System.getProperty("file.separator") + this.getDescription().getName() + System.getProperty("file.separator") + "config.yml");
		if (configFile.exists() == false) {
    		this.saveDefaultConfig();
    		System.out.println("[Monkey-CS] CREATED A CONFIG");
		}
    	
    	try {
    		this.getConfig().load(configFile);
    		System.out.println("[Monkey-CS] LOADED CONFIG");
    	} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Monkey-CS] FAILED TO LOAD CONFIG");
    	}
	}
	
	public void readConfig() {
		int cat = 1;
		int card = 1;
		while (getConfig().contains("cat" + Integer.toString(cat))) {
			catSlot.put(getConfig().getInt("cat" + Integer.toString(cat) + ".slot"), "cat" + Integer.toString(cat));
			catNames.add(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', getConfig().getString("cat" + Integer.toString(cat) + ".displayName"))));
			cat++;
		}
		while (getConfig().contains("card" + Integer.toString(card))) {
			cardSlot.put(getConfig().getInt("card" + Integer.toString(card) + ".slot"), "card" + Integer.toString(card));
			price.put("card" + Integer.toString(card), getConfig().getDouble("card" + Integer.toString(card) + ".price"));
			category.put("card" + Integer.toString(card), getConfig().getString("card" + Integer.toString(card) + ".category"));
			name2Name.put(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', getConfig().getString("card" + Integer.toString(card) + ".displayName"))), "card" + Integer.toString(card));
			if (!getConfig().contains("card" + Integer.toString(card + 1)) && getConfig().contains("card" + Integer.toString(card + 2))) {
				card++;
			}
			card++;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("cardshop")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (p.hasPermission("MonkeyCS.gui")) {
						getGui().openCatGui(p);
					} else {
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else {
					System.out.println("[Monkey-CS] YOU CAN NOT ACCESS THE GUI FROM CONSOLE!");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					price.clear();
					cardSlot.clear();
					catSlot.clear();
					category.clear();
					name2Name.clear();
					catNames.clear();
					reloadConfig();
					readConfig();
					if (sender instanceof Player) {
						Player p = (Player) sender;
						p.sendMessage(ChatColor.DARK_RED + "Monkey Card Shop Has Been Reloaded!");
					}
					System.out.println("[Monkey-CS] SUCCESSFULLY RELOADED!");
				}
			}
		}
		return false;
	}
	
	public gui getGui() {
		return gi;
	}
}
