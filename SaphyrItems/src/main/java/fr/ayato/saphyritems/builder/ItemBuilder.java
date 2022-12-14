package fr.ayato.saphyritems.builder;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.saphyritems.utils.Config;
import fr.ayato.saphyritems.utils.Messages;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class ItemBuilder {

    public static ItemStack data(String item, String playerName) {
        boolean itemGlow = Config.isEnchantsHidden(item);
        boolean itemUnbreakable = Config.isUnbreakable(item);
        boolean itemInfinite = Config.isInfinite(item);
        boolean isThor = Config.isThor(item);
        boolean isWither = Config.isWither(item);
        boolean isEffect = Config.isGiveEffects(item);
        boolean isBlockBreakCounter = Config.isBlockBreakCounter(item);
        boolean noFall = Config.isNoFallDamage(item);
        boolean isDamageCounter = Config.isDamageCounter(item);

        String itemName = Config.getItemName(item);
        List<String> itemLore = Config.getItemLore(item);
        String itemMaterial = Config.getItemMaterial(item);
        List<String> itemEnchants = Config.getItemEnchants(item);

        ItemStack itemStack = new ItemStack(Material.getMaterial(itemMaterial));

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);
        Messages.replaceAllPlaceHolders(itemLore, playerName, null, null, null, null);
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
        NBTBuilder.setSaphyrItem(nbtItem, item);
        NBTBuilder.setOwner(nbtItem, playerName);
        NBTBuilder.setInfinite(nbtItem, itemInfinite);
        NBTBuilder.setThor(nbtItem, isThor, Config.getThorCooldown(item));
        NBTBuilder.setWither(nbtItem, isWither, Config.getWitherCooldown(item), Config.getWitherDuration(item), Config.getWitherAmplifier(item), Config.getWitherRadius(item));
        NBTBuilder.setEffect(nbtItem, isEffect, item);
        NBTBuilder.setBlockBreakCounter(nbtItem, isBlockBreakCounter, 0);
        NBTBuilder.setNoFall(nbtItem, noFall);
        NBTBuilder.setDamageCounter(nbtItem, isDamageCounter, 0);
        NBTBuilder.setKillCounter(nbtItem, 0);
        NBTBuilder.setRandomString(nbtItem);
        NBTBuilder.setLastKill(nbtItem, "Aucun");
        nbtItem.applyNBT(itemStack);

        return itemStack;
    }
}
