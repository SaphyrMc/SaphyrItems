package fr.ayato.saphyritems.inventories;

import fr.ayato.saphyritems.Main;
import fr.ayato.saphyritems.utils.Config;
import fr.ayato.saphyritems.utils.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandMenu implements Listener {

    public static Inventory inv;
    public static Inventory itemInv;

    public static Inventory getMenu() {
        inv = Bukkit.createInventory(null, 27, "§d§l⚡ §b§lSaphyrItems");
        InventoryInstance.addGlass(inv, 0, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 1, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 9, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(inv, 7, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 8, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 17, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(inv, 18, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 19, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(inv, 25, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 26, Material.STAINED_GLASS_PANE, " ");
        ItemStack command = InventoryInstance.createGuiItem(Material.PAPER, "§b§lInformations", new ArrayList<>(Arrays.asList("§7§oCliquez pour voir les informations")), true);
        ItemStack items = InventoryInstance.createGuiItem(Material.CHEST, "§b§lItems", new ArrayList<>(Arrays.asList("§7§oCliquez pour voir les items")), true);

        inv.setItem(12, command);
        inv.setItem(14, items);

        return inv;
    }

    public static void initItemGui() {
        itemInv = Bukkit.createInventory(null, 27, "§d§l⚡ §b§lSaphyrItems");
        InventoryInstance.addGlass(itemInv, 0, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(itemInv, 1, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(itemInv, 9, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(itemInv, 7, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(itemInv, 8, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(itemInv, 17, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(itemInv, 18, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(itemInv, 19, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(itemInv, 25, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(itemInv, 26, Material.STAINED_GLASS_PANE, " ");
        for (String item : Main.configItems) {
            String itemMaterial = Config.getItemMaterial(item);
            ItemStack itemStack = new ItemStack(Material.getMaterial(itemMaterial), 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§b§l" + item);
            //change the lore to §7§oCliquez pour obtenir l'item
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§7§oCliquez pour obtenir l'item");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            int slot = itemInv.firstEmpty();
            itemInv.setItem(slot, itemStack);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inv)) {
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.AIR) return;
            event.setCancelled(true);
            if (event.getCurrentItem().getType() == Material.PAPER) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.sendMessage("");
                player.sendMessage("         §b§lSaphyrItems §f» §b§lInformations :");
                player.sendMessage("");
                player.sendMessage("§b§l/sitems §f» §7§oOuvre le menu");
                player.sendMessage("§b§l/sitems give <player> <item> <nombre> §f» §7§oDonne un item à un joueur");
                player.sendMessage("");
                player.sendMessage("          §b§lItems :");
                player.sendMessage("");
                for (String item : Main.configItems) {
                    player.sendMessage("§f➢ §b§l" + item);
                }
            }

            if (event.getCurrentItem().getType() == Material.CHEST) {
                if (event.getCurrentItem() == null) return;
                if (event.getCurrentItem().getType() == Material.AIR) return;
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.openInventory(itemInv);
            }
        } else if (event.getInventory().equals(itemInv)) {
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.AIR) return;
            event.setCancelled(true);
            for (String item : Main.configItems) {
                try {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§b§l" + item)) {
                        Player player = (Player) event.getWhoClicked();
                        player.getInventory().addItem(CreateItem.data(item, player.getName()));
                        player.updateInventory();
                    }
                } catch (NullPointerException e) {
                    return;
                }
            }
        }
    }
}