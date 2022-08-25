package com.stinkymonkey.cardshop;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class gui {
	private main Main;
	public gui (main Main) {
		this.Main = Main;
	}
	
	public void openCatGui(Player p) {
		Inventory categories;
		categories = Bukkit.createInventory(null,  Main.getConfig().getInt("settings.invSlotMain"), ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString("settings.mainName")));
		for (Map.Entry<Integer, String> str : main.catSlot.entrySet()) {
			List<String> lore = new ArrayList<String>();
			for (String s : Main.getConfig().getStringList(str.getValue() + ".lore")) {
				lore.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			ItemStack item = new ItemStack(Material.getMaterial(Main.getConfig().getString(str.getValue() + ".itemType")));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString(str.getValue() + ".displayName")));
			meta.setLore(lore);
			item.setItemMeta(meta);
			categories.setItem(str.getKey(), item);
		}
		p.openInventory(categories);
	}
	
	public void openShopGui(Player p, String cat) {
		Inventory shop;
		shop = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString(cat + ".displayName")));
		for (Map.Entry<String, String> str : main.category.entrySet()) {
			if (str.getValue().equals(cat)) {
				List<String> lore = new ArrayList<String>();
				for (String s : Main.getConfig().getStringList(str.getKey() + ".lore")) {
					lore.add(ChatColor.translateAlternateColorCodes('&', s));
				}
				ItemStack item = new ItemStack(Material.getMaterial(Main.getConfig().getString(str.getKey() + ".itemType")));
				ItemMeta meta = item.getItemMeta();
				lore.add(" ");
				lore.add(ChatColor.GREEN + "$" + String.format("%,.2f", Main.getConfig().getDouble(str.getKey() + ".price")));
				lore.add("Left Click To Sell One");
				lore.add("Right Click To Sell All");
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString(str.getKey() + ".displayName")));
				meta.setLore(lore);
				item.setItemMeta(meta);
				shop.setItem(Main.getConfig().getInt(str.getKey() + ".slot"), item);
			}
		}
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Back");
		item.setItemMeta(meta);
		shop.setItem(31, item);
		p.openInventory(shop);
	}
}
