package fr.ayato.saphyritems.utils;

import fr.ayato.saphyritems.Main;

import java.util.ArrayList;
import java.util.List;

public class Config {

    //get the all the items from the config file
    public static List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        for (String s : Main.getInstance().getConfig().getKeys(true)) {
            try {
                if (!Main.getInstance().getConfig().getString(s + ".material").isEmpty()) {
                    items.add(s);
                }
            } catch (NullPointerException ignored) {
            }
        }
        return items;
    }

    //get the item's name
    @SuppressWarnings("unused")
    public static String getItemName(String item) {
        return Main.getInstance().getConfig().getString("items." + item + ".name");
    }

    //get the item's material
    @SuppressWarnings("unused")
    public static String getItemMaterial(String item) {
        return Main.getInstance().getConfig().getString("items." + item + ".material");
    }

    //get the item's lore
    @SuppressWarnings("unused")
    public static List<String> getItemLore(String item) {
        return Main.getInstance().getConfig().getStringList("items." + item + ".lore");
    }

    //get if the item want the enchants hidden
    @SuppressWarnings("unused")
    public static boolean isEnchantsHidden(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".hide");
    }

    //get if the item is infinite
    @SuppressWarnings("unused")
    public static boolean isInfinite(String item) {
        return Main.getInstance().getConfig().getBoolean("items." + item + ".infinite");
    }

    //get the items enchants
    @SuppressWarnings("unused")
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
}
