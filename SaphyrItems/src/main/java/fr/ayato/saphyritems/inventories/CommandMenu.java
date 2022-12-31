package fr.ayato.saphyritems.inventories;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.saphyritems.Main;
import fr.ayato.saphyritems.utils.Config;
import fr.ayato.saphyritems.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandMenu implements Listener {

    public static Inventory inv;
    public static Inventory itemInv;

    public static Inventory initCommandGui() {
        inv = createGlassGui();
        ItemStack command = InventoryInstance.createGuiItem(Material.PAPER, "§b§lInformations", new ArrayList<>(Arrays.asList("§7§oCliquez pour voir les informations")), true);
        ItemStack items = InventoryInstance.createGuiItem(Material.CHEST, "§b§lItems", new ArrayList<>(Arrays.asList("§7§oCliquez pour voir les items")), true);

        inv.setItem(12, command);
        inv.setItem(14, items);
        return inv;
    }

    public static Inventory initItemGui(String owner) {
        itemInv = createGlassGui();
        for (String item : Main.configItems) {
            String itemMaterial = Config.getItemMaterial(item);
            ItemStack itemStack = new ItemStack(Material.getMaterial(itemMaterial), 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(Config.getItemName(item));
            List<String> lore = Config.getItemLore(item);
            Messages.replaceAllPlaceHolders(lore, owner, null, null, 0, null);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setString("saphyr-item", item);
            nbtItem.applyNBT(itemStack);
            int slot = itemInv.firstEmpty();
            itemInv.setItem(slot, itemStack);
        }
        return itemInv;
    }

    public static Inventory createGlassGui() {
        Inventory inv = Bukkit.createInventory(null, 54, "§d§l⚡ §b§lSaphyrItems");
        InventoryInstance.addGlass(inv, 0, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 1, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 9, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(inv, 7, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 8, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 17, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(inv, 45, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 46, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 36, Material.STAINED_GLASS_PANE, " ");

        InventoryInstance.addGlass(inv, 44, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 53, Material.STAINED_GLASS_PANE, " ");
        InventoryInstance.addGlass(inv, 52, Material.STAINED_GLASS_PANE, " ");
        return inv;
    }
}
