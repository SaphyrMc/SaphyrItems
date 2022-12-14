package fr.ayato.saphyritems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.saphyritems.Main;
import fr.ayato.saphyritems.builder.ItemBuilder;
import fr.ayato.saphyritems.builder.NBTBuilder;
import fr.ayato.saphyritems.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static fr.ayato.saphyritems.inventories.CommandMenu.*;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inv)) {
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.AIR) return;
            event.setCancelled(true);
            if (event.getCurrentItem().getType() == Material.PAPER) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                for (String message : Messages.getHelpMessage()) {
                    player.sendMessage(message);
                }
            }

            if (event.getCurrentItem().getType() == Material.CHEST) {
                if (event.getCurrentItem() == null) return;
                if (event.getCurrentItem().getType() == Material.AIR) return;
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.openInventory(initItemGui(player.getName()));
            }
        } else if (event.getInventory().equals(itemInv)) {
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.AIR) return;
            event.setCancelled(true);
            NBTItem nbtItem = new NBTItem(event.getCurrentItem());
            String itemName = nbtItem.getString("saphyr-item");
            for (String item : Main.configItems) {
                try {
                    if (itemName.equals(item)) {
                        Player player = (Player) event.getWhoClicked();
                        ItemStack itemStack = ItemBuilder.data(item, player.getName());
                        NBTItem nbtItem1 = new NBTItem(itemStack);
                        player.getInventory().addItem(ItemBuilder.data(item, player.getName()));
                        player.updateInventory();
                    }
                } catch (NullPointerException e) {
                    return;
                }
            }
        }
    }
}
