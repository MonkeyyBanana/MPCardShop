package com.stinkymonkey.cardshop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class listener implements Listener{
	private main Main;
	public listener (main Main) {
		this.Main = Main;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) return;
		if (event.getCurrentItem().getItemMeta() == null) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		Player p = (Player) event.getWhoClicked();
		if (p.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString("settings.mainName")))) {
			event.setCancelled(true);
			if (main.catSlot.containsKey(event.getSlot())) {
				String catYml = main.catSlot.get(event.getSlot());
				p.closeInventory();
				Main.getGui().openShopGui(p, catYml);
			}
		} else if (main.catNames.contains(ChatColor.stripColor(p.getOpenInventory().getTitle()))) {
			event.setCancelled(true);
			if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).contains("[Card]")) {
				String branchCard = main.name2Name.get(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
				boolean checker = false;
				ItemStack newItem = null;
				int amount = 0;
				for (ItemStack item : p.getInventory().getContents()) {
					try {
						if (item.hasItemMeta()) {
							ItemMeta meta = item.getItemMeta();
							if (meta.hasLore()) {
								List<String> lore = meta.getLore();
								List<String> newLore = new ArrayList<String>();
								for (String str : lore) {
									newLore.add(ChatColor.stripColor(str) );
								}
								if (newLore.contains(Main.getConfig().getString(branchCard + ".rarity")) && ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))) {
									checker = true;
									newItem = item;
									amount += item.getAmount();
								}
							}
						}
					} catch (Exception e) {
						
					}
				}
				if (event.getClick().equals(ClickType.LEFT) && checker == true) {
					for (int i = 1; i <= 64; i++) {
						newItem.setAmount(i);
						while(p.getInventory().contains(newItem)) {
							p.getInventory().remove(newItem);
						}
					}
					newItem.setAmount(amount - 1);
					p.getInventory().addItem(newItem);
					main.econ.depositPlayer(p, main.price.get(branchCard));
					p.sendMessage(ChatColor.GREEN + "Successfully Sold A Card For $" + String.format("%,.2f", main.price.get(branchCard)));
					p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_PLING"), 3.0F, 3.0F);
				} else if (event.getClick().equals(ClickType.RIGHT) && checker == true) {
					for (int i = 1; i <= 64; i++) {
						newItem.setAmount(i);
						while (p.getInventory().contains(newItem)) {
							p.getInventory().remove(newItem);
						}
					}
					main.econ.depositPlayer(p, (amount * main.price.get(branchCard)));
					p.sendMessage(ChatColor.GREEN + "Successfully Sold Cards For $" + String.format("%,.2f",  (amount * main.price.get(branchCard))));
					p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_PLING"), 3.0F, 3.0F);
				} else {
					p.sendMessage(ChatColor.RED + "No Cards To Sell!");
					p.playSound(p.getLocation(), Sound.valueOf("ENTITY_ITEM_BREAK"), 3.0F, 3.0F);
				}
			} else if (event.getSlot() == 31) {
				p.closeInventory();
				Main.getGui().openCatGui(p);
			}
		}
	}
}
