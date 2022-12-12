package fr.ayato.saphyritems.commands;

import fr.ayato.saphyritems.Main;
import fr.ayato.saphyritems.inventories.CommandMenu;
import fr.ayato.saphyritems.utils.Config;
import fr.ayato.saphyritems.utils.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class GiveItems implements CommandExecutor {

    // Plugin
    private final Main plugin;
    public GiveItems(Main main) {
        this.plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            if (!sender.hasPermission("sitems.gui.open")) return false;
            Player player = (Player) sender;
            player.openInventory(CommandMenu.getMenu());
            return true;
        } else if (args.length == 4) {
            final Player player = Bukkit.getPlayer(args[1]);
            if (player.hasPermission("saphyritems") || sender instanceof ConsoleCommandSender) {
                final String item = args[2];
                if (plugin.getConfig().contains("items." + item)) {
                    if (!Bukkit.getOnlinePlayers().contains(player)) return false;
                    final int number = Integer.parseInt(args[3]);
                    for (int i = 0; i < number; i++) {
                        player.getInventory().addItem(CreateItem.data(item, args[1]));
                    }
                    player.updateInventory();
                }
                return true;
            }
        } else {
            sender.sendMessage("§b§lSaphyrItems §f» §b/sitems");
        }
        return false;
    }
}
