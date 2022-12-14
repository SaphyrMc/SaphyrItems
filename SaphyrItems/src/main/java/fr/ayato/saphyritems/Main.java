package fr.ayato.saphyritems;

import fr.ayato.saphyritems.commands.SaphyrHelp;
import fr.ayato.saphyritems.inventories.CommandMenu;
import fr.ayato.saphyritems.listeners.InventoryEvents;
import fr.ayato.saphyritems.listeners.ItemEvents;
import fr.ayato.saphyritems.listeners.PlayerEvents;
import fr.ayato.saphyritems.utils.Config;
import fr.ayato.saphyritems.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    public static List<String> configItems = new ArrayList<>();

    public static Main getInstance() {
        return JavaPlugin.getPlugin(Main.class);
    }

    // This function active the plugin
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[on] " + ChatColor.AQUA + "SaphyrItems Enabled !" + ChatColor.LIGHT_PURPLE + " [on]");
        getCommand("sitems").setExecutor(new SaphyrHelp(this));
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new ItemEvents(), this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        configItems = Config.getAllItems();
        CommandMenu.initCommandGui();
        Messages.setHelpMessage(Messages.helpMessage);
    }

    // This function disable the plugin
    @Override
    public void onDisable(){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[off] " + ChatColor.AQUA + "SaphyrItems Disabled !" + ChatColor.LIGHT_PURPLE + " [off]");
    }
}
