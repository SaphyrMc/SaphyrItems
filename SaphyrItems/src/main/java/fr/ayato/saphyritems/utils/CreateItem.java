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
        boolean isThor = Config.isThor(item);
        boolean isWither = Config.isWither(item);
        boolean isEffect = Config.isGiveEffects(item);
        boolean isBlockBreakCounter = Config.isBlockBreakCounter(item);

        ItemStack itemStack = new ItemStack(Material.getMaterial(itemMaterial));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);

        Messages.replacePlaceHolders(itemLore, playerName, null, null, 1);
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

        if (isThor) {
            nbtItem.setBoolean("thor", true);
            Integer cooldown = Config.getThorCooldown(item);
            nbtItem.setInteger("thor-cooldown", cooldown);
        }

        if (isWither) {
            nbtItem.setBoolean("wither", true);
            Integer cooldown = Config.getWitherCooldown(item);
            Integer duration = Config.getWitherDuration(item) * 20;
            Integer amplifier = Config.getWitherAmplifier(item);
            Integer radius = Config.getWitherRadius(item);
            nbtItem.setInteger("wither-cooldown", cooldown);
            nbtItem.setInteger("wither-duration", duration);
            nbtItem.setInteger("wither-amplifier", amplifier);
            nbtItem.setInteger("wither-radius", radius);
        }

        if (isEffect) {
            nbtItem.setBoolean("effects", true);
            List<String> effects = Config.getEffectsList(item);
            String effectsString = String.join(",", effects);
            String effectType = Config.getEffectsType(item);
            nbtItem.setString("effects-list", effectsString);
            nbtItem.setString("effects-type", effectType);
        }

        if (isBlockBreakCounter) {
            nbtItem.setBoolean("breakblock", true);
            nbtItem.setInteger("breakblock-counter", 0);
        }

        nbtItem.setString("owner", playerName);
        nbtItem.setString("kills", "0");
        String randomString = java.util.UUID.randomUUID().toString();
        nbtItem.setString("rdm", randomString);
        nbtItem.applyNBT(itemStack);

        return itemStack;
    }
}
