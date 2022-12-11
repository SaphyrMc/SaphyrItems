package fr.ayato.saphyritems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.saphyritems.utils.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PlayerEvents implements Listener {

    public HashMap<ItemStack, Integer> itemData = new HashMap<>();
    public HashMap<UUID, HashMap<ItemStack, Integer>> infiniteList = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        UUID uuid = event.getEntity().getUniqueId();
        final Iterator<ItemStack> iterator = event.getDrops().iterator();

        while (iterator.hasNext()) {
            final ItemStack item = iterator.next();
            if (item.getType() == Material.AIR) continue;
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey("item")) {
                if (nbtItem.hasKey("infinite")) {
                    if (infiniteList.containsKey(uuid)) {
                        infiniteList.get(uuid).put(item, 1);
                    } else {
                        infiniteList.put(uuid, new HashMap<ItemStack, Integer>() {{
                            put(item, 1);
                        }});
                    }
                    iterator.remove();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        if (infiniteList.containsKey(playerUUID)) {
            for (Map.Entry<ItemStack, Integer> entry : infiniteList.get(playerUUID).entrySet()) {
                final ItemStack item = entry.getKey();
                final Integer itemAmount = entry.getValue();
                for (int i = 0; i < itemAmount; i++) {
                    player.getInventory().addItem(item);
                }
                itemData.remove(entry.getKey());
                infiniteList.remove(playerUUID);
            }
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer != null) {
            NBTItem nbtItem = new NBTItem(killer.getItemInHand());
            if (nbtItem.hasKey("item")) {
                if (nbtItem.hasKey("kills")) {
                    int kills = Integer.parseInt(nbtItem.getString("kills"));
                    kills++;
                    nbtItem.setString("kills", String.valueOf(kills));
                    killer.setItemInHand(nbtItem.getItem());
                    ItemMeta meta = killer.getItemInHand().getItemMeta();
                    List<String> lore = Config.getItemLore(nbtItem.getString("item"));
                    List<String> newLore = new ArrayList<>();
                    for (String line : lore) {
                        if (line.contains("%kills%")) {
                            line = line.replace("%kills%", String.valueOf(kills));
                        } else if (line.contains("%owner%")) {
                            line = line.replace("%owner%", nbtItem.getString("owner"));
                        }
                        newLore.add(line);
                    }
                    meta.setLore(newLore);
                    killer.getItemInHand().setItemMeta(meta);
                    killer.updateInventory();
                }
            }
        }
    }
}
