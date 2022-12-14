package fr.ayato.saphyritems.utils;

import fr.ayato.saphyritems.Main;

import java.util.ArrayList;
import java.util.List;

public class Config {

    //get the all the items from the config file
    public static List<String> getAllItems() {
        return new ArrayList<>(Main.getInstance().getConfig().getConfigurationSection("items").getKeys(false));
    }

    //get the item's name
    public static String getItemName(String item) {
        return Main.getInstance().getConfig().getString("items." + item + ".name");
    }

    //get the item's material
    public static String getItemMaterial(String item) {
        return Main.getInstance().getConfig().getString("items." + item + ".material");
    }

    //get the item's lore
    public static List<String> getItemLore(String item) {
        return Main.getInstance().getConfig().getStringList("items." + item + ".lore");
    }

    //get if the item want the enchants hidden
    public static boolean isEnchantsHidden(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".hide");
    }

    //get if the item is infinite
    public static boolean isInfinite(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".infinite");
    }

    //get the items enchants
    public static List<String> getItemEnchants(String item) {
        List<String> enchants = new ArrayList<>();
        //check if null
        if (Main.getInstance().getConfig().getString("items." + item + ".enchantments") != null) {
            enchants.addAll(Main.getInstance().getConfig().getStringList("items." + item + ".enchantments"));
        }
        return enchants;
    }

    //get if the item is unbreakable
    public static boolean isUnbreakable(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".unbreakable");
    }

    //Get if the item is thor
    public static boolean isThor(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".thor");
    }

    //Get the thor cooldown
    public static int getThorCooldown(String item) {
        return Main.getInstance().getConfig().getInt("items." + item + ".thor-cooldown");
    }

    //Get if the item is Wither
    public static boolean isWither(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".wither");
    }

    //Get the Wither cooldown
    public static int getWitherCooldown(String item) {
        return Main.getInstance().getConfig().getInt("items." + item + ".wither-cooldown");
    }

    //Get the duration of the Wither
    public static int getWitherDuration(String item) {
        return Main.getInstance().getConfig().getInt("items." + item + ".wither-duration");
    }

    //Get the Wither amplifier
    public static int getWitherAmplifier(String item) {
        return Main.getInstance().getConfig().getInt("items." + item + ".wither-amplifier");
    }

    //get the wither radius
    public static int getWitherRadius(String item) {
        return Main.getInstance().getConfig().getInt("items." + item + ".wither-radius");
    }

    //get is the items give effects
    public static boolean isGiveEffects(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".effects");
    }

    //get the effects-type
    public static String getEffectsType(String item) {
        return Main.getInstance().getConfig().getString("items." + item + ".effects-type");
    }

    //get the effects-list
    public static List<String> getEffectsList(String item) {
        return Main.getInstance().getConfig().getStringList("items." + item + ".effects-list");
    }

    public static boolean isBlockBreakCounter(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".breakblock");
    }

    public static boolean isNoFallDamage(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".nofall");
    }

    public static boolean isDamageCounter(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".countdamage");
    }
}
