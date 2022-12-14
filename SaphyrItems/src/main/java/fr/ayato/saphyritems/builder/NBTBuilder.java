package fr.ayato.saphyritems.builder;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.saphyritems.utils.Config;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class NBTBuilder {

    public static void setSaphyrItem(NBTItem itemStack, String item) {
        itemStack.setString("saphyr-item", item);
    }
    public static void setInfinite(NBTItem itemStack, boolean infinite) {
        if (!infinite) return;
        itemStack.setBoolean("infinite", true);
    }

    public static void setThor(NBTItem itemStack, boolean isThor, int thorCooldown) {
        if (!isThor) return;
        itemStack.setBoolean("thor", true);
        itemStack.setInteger("thor-cooldown", thorCooldown);
    }

    public static void setWither(NBTItem itemStack, boolean isWither, int witherCooldown, int duration, int amplifier, int radius) {
        if (!isWither) return;
        itemStack.setBoolean("wither", true);
        itemStack.setInteger("wither-cooldown", witherCooldown);
        itemStack.setInteger("wither-duration", duration);
        itemStack.setInteger("wither-amplifier", amplifier);
        itemStack.setInteger("wither-radius", radius);
    }

    public static void setBlockBreakCounter(NBTItem itemStack, boolean isBlockBreakCounter, int blockBreakCounter) {
        if (!isBlockBreakCounter) return;
        itemStack.setBoolean("breakblock", true);
        itemStack.setInteger("breakblock-counter", blockBreakCounter);
    }

    public static void setNoFall(NBTItem itemStack, boolean noFall) {
        if (!noFall) return;
        itemStack.setBoolean("nofall", true);
    }

    public static void setDamageCounter(NBTItem itemStack, boolean isDamageCounter, int damageCounter) {
        if (!isDamageCounter) return;
        itemStack.setBoolean("countdamage", true);
        itemStack.setInteger("damage-counter", damageCounter);
    }

    public static void setKillCounter(NBTItem itemStack, int i) {
        itemStack.setInteger("kills", i);
    }

    public static void setOwner(NBTItem itemStack, String playerName) {
        itemStack.setString("owner", playerName);
    }

    public static void setRandomString(NBTItem itemStack) {
        itemStack.setString("rdm", String.valueOf(Math.random()));
    }

    public static void setEffect(NBTItem itemStack, boolean isEffect, String item) {
        if (!isEffect) return;
        itemStack.setBoolean("effects", true);
        List<String> effects = Config.getEffectsList(item);
        String effectsString = String.join(",", effects);
        String effectType = Config.getEffectsType(item);
        itemStack.setString("effects-list", effectsString);
        itemStack.setString("effects-type", effectType);
    }
}
