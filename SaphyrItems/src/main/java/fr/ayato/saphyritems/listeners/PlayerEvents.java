package fr.ayato.saphyritems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.saphyritems.utils.Config;
import fr.ayato.saphyritems.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
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
            if (nbtItem.hasKey("saphyr-item")) {
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
    public void onPlayerKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (victim.getType() != EntityType.PLAYER) return;
        Player killer = victim.getKiller();
        if (killer != null) {
            NBTItem nbtItem = new NBTItem(killer.getItemInHand());
            if (nbtItem.hasKey("saphyr-item")) {
                if (nbtItem.hasKey("kills")) {
                    Integer kills = Integer.parseInt(nbtItem.getString("kills"));
                    String owner = nbtItem.getString("owner");
                    String lastKill = victim.getName();
                    ItemMeta meta = killer.getItemInHand().getItemMeta();
                    List<String> lore = Config.getItemLore(nbtItem.getString("saphyr-item"));
                    kills++;
                    nbtItem.setString("kills", String.valueOf(kills));
                    nbtItem.setString("last-kill", lastKill);
                    killer.setItemInHand(nbtItem.getItem());
                    Messages.replaceOwnerPlaceHolder(lore, owner);
                    Messages.replaceKillsPlaceHolder(lore, String.valueOf(kills));
                    Messages.replaceLastKillPlaceHolder(lore, lastKill);
                    meta.setLore(lore);
                    killer.getItemInHand().setItemMeta(meta);
                    killer.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                ItemStack helmet = player.getInventory().getHelmet();
                ItemStack chestplate = player.getInventory().getChestplate();
                ItemStack leggings = player.getInventory().getLeggings();
                ItemStack boots = player.getInventory().getBoots();
                List<ItemStack> armor = new ArrayList<>();
                if (helmet != null) armor.add(helmet);
                if (chestplate != null) armor.add(chestplate);
                if (leggings != null) armor.add(leggings);
                if (boots != null) armor.add(boots);
                for (ItemStack itemStack : armor) {
                    if (itemStack != null) {
                        NBTItem nbtItem = new NBTItem(itemStack);
                        if (nbtItem.hasKey("nofall")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageTaken(EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                ItemStack item = damager.getInventory().getItemInHand();
                if (item == null || item.getType() == Material.AIR) return;
                NBTItem nbtItem = new NBTItem(item);
                if (nbtItem.hasKey("countdamage")) {
                    double damage = event.getDamage();
                    String damageString = String.valueOf(nbtItem.getInteger("damage-counter"));
                    double damageCounter = Double.parseDouble(damageString);
                    double newDamage = damageCounter + damage;
                    nbtItem.setInteger("damage-counter", (int) newDamage);
                    nbtItem.applyNBT(item);
                    String itemName = nbtItem.getString("saphyr-item");
                    String owner = nbtItem.getString("owner");
                    Integer kills = nbtItem.getInteger("kills");
                    String lastKill = nbtItem.getString("last-kill");
                    List<String> lore = Config.getItemLore(itemName);
                    Messages.replaceOwnerPlaceHolder(lore, owner);
                    Messages.replaceKillsPlaceHolder(lore, String.valueOf(kills));
                    Messages.replaceDamageDealtPlaceHolder(lore, (int) newDamage);
                    Messages.replaceLastKillPlaceHolder(lore, lastKill);
                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    damager.updateInventory();
                }
            }
        }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item.getType() == Material.AIR) return;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasKey("breakblock")) {
            Integer counter = nbtItem.getInteger("breakblock-counter");
            counter = counter + 1;
            nbtItem.setInteger("breakblock-counter", counter);
            nbtItem.applyNBT(item);
            String itemName = nbtItem.getString("saphyr-item");
            List<String> lore = Config.getItemLore(itemName);
            String owner = nbtItem.getString("owner");
            Messages.replaceOwnerPlaceHolder(lore, owner);
            Messages.replaceBlocksBrokenPlaceHolder(lore, counter);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
            Player player = event.getPlayer();
            player.updateInventory();
        }
    }
}
