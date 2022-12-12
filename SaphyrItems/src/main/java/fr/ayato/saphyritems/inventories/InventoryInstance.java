package fr.ayato.saphyritems.inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class InventoryInstance {
    public static void addItemMenu(Inventory inv, Integer pos, Material mat, String name, ArrayList<String> lore) {
        inv.setItem(pos, createGuiItem(mat, name, lore, true));
    }

    public static ItemStack createGuiItem(Material material, String name, ArrayList<String> lore, Boolean enhanced) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if (enhanced) {
            meta.addEnchant(Enchantment.DURABILITY, 10, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static void addGlass(Inventory inv, Integer pos, Material mat, String name) {
        inv.setItem(pos, createGuiGlass(mat, name));
    }


    public static ItemStack createGuiGlass(Material material, String name) {
        ItemStack nop = new ItemStack(material, 1, (short)1);
        ItemMeta meta = nop.getItemMeta();
        meta.setDisplayName(name);
        nop.setItemMeta(meta);
        return nop;
    }
}
