package fr.ayato.saphyritems.utils;

import fr.ayato.saphyritems.Main;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static List<String> helpMessage = new ArrayList<>();

    public static void setHelpMessage(List<String> helpMessage) {
        String version = Main.getInstance().getDescription().getVersion();
        helpMessage.add("");
        helpMessage.add("        §b§lSaphyrItems §7- §d§l§ov" + version);
        helpMessage.add("");
        helpMessage.add("§b§l/sitems §f» §7§oOuvre le menu");
        helpMessage.add("§b§l/sitems give <player> <item> <nombre> §f» §7§oDonne un item à un joueur");
        helpMessage.add("");
        helpMessage.add("        §b§lItems §f»");
        helpMessage.add("");
        for (String item : Main.configItems) {
            helpMessage.add("§f➜ §b§l" + item);
        }
        helpMessage.add("");
    }

    public static List<String> getHelpMessage() {
        return helpMessage;
    }

    public static void replacePlaceHolders(List<String> lore, String owner, String kills, String lastPlayerKilled, Integer blockbroken, Integer damageDealt) {
        if (kills == null) {
            kills = "0";
        }
        if (lastPlayerKilled == null) {
            lastPlayerKilled = "Aucun";
        }
        if (blockbroken == null) {
            blockbroken = 0;
        }

        if (damageDealt == null) {
            damageDealt = 0;
        }

        for (int i = 0; i < lore.size(); i++) {
            try {
                lore.set(i, lore.get(i).replace("%owner%", owner));
                lore.set(i, lore.get(i).replace("%kills%", kills));
                lore.set(i, lore.get(i).replace("%lastkill%", lastPlayerKilled));
                lore.set(i, lore.get(i).replace("%blocks_broken%", String.valueOf(blockbroken)));
                lore.set(i, lore.get(i).replace("%damage_dealt%", String.valueOf(damageDealt)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
