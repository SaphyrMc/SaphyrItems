package fr.ayato.saphyritems.commands;

import fr.ayato.saphyritems.Main;
import fr.ayato.saphyritems.utils.Config;
import fr.ayato.saphyritems.utils.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class GiveItems implements CommandExecutor {

    // Plugin
    private final Main plugin;
    public GiveItems(Main main) {
        this.plugin = main;
    }

    // /sitems give <player> <item> <amount>
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 4) {
            final Player player = Bukkit.getPlayer(args[1]);
            final String item = args[2];
            final int number = Integer.parseInt(args[3]);

            if (player.hasPermission("saphyritems") || sender instanceof ConsoleCommandSender) {
                if (plugin.getConfig().contains("items." + item)) {
                    if (!Bukkit.getOnlinePlayers().contains(player)) return false;

                    for (int i = 0; i < number; i++) {
                        player.getInventory().addItem(CreateItem.data(item, args[1]));
                    }
                    player.updateInventory();
                } else {
                    player.sendMessage("§b§lSaphyrItems §f» §cChoisissez un item de la liste !");
                    for (String items : Main.configItems) {
                        player.sendMessage("§b§l» §c" + items);
                    }
                }
                return true;
            }
        } else {
            sender.sendMessage("§b§lSaphyrItems §f» /sitems give <pseudo> <item> <nombre>");
        }
        return false;
    }
}
