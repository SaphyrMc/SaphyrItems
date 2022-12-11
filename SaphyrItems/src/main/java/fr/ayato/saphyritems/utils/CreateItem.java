package fr.ayato.saphyritems.utils;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CreateItem {

    public static ItemStack data(String item, String playerName) {
        String itemName = Config.getItemName(item);
        List<String> itemLore = Config.getItemLore(item);
        String itemMaterial = Config.getItemMaterial(item);
        List<String> itemEnchants = Config.getItemEnchants(item);

        boolean itemGlow = Config.isEnchantsHidden(item);
        boolean itemUnbreakable = Config.isUnbreakable(item);
        boolean itemInfinite = Config.isInfinite(item);

        ItemStack itemStack = new ItemStack(Material.getMaterial(itemMaterial));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);

        for (int i = 0; i < itemLore.size(); i++) {
            if (itemLore.get(i).contains("%owner%")) {
                itemLore.set(i, itemLore.get(i).replace("%owner%", playerName));
            } else if (itemLore.get(i).contains("%kills%")) {
                itemLore.set(i, itemLore.get(i).replace("%kills%", "0"));
            } else {
                itemLore.set(i, itemLore.get(i));
            }
        }
        itemMeta.setLore(itemLore);

        for (String enchant : itemEnchants) {
            String[] enchantSplit = enchant.split(":");
            itemMeta.addEnchant(Enchantment.getByName(enchantSplit[0]), Integer.parseInt(enchantSplit[1]), true);
        }

        if (itemGlow) {
            itemMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        }

        if (itemUnbreakable) {
            itemMeta.spigot().setUnbreakable(true);
        }
        itemStack.setItemMeta(itemMeta);

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("item", item);

        if (itemInfinite) {
            nbtItem.setBoolean("infinite", true);
        }
        nbtItem.setString("owner", playerName);
        nbtItem.setString("kills", "0");
        String randomString = java.util.UUID.randomUUID().toString();
        nbtItem.setString("rdm", randomString);
        nbtItem.applyNBT(itemStack);

        return itemStack;
    }
}
