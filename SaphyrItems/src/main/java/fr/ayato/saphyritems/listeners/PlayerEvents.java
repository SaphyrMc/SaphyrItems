package fr.ayato.saphyritems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import fr.ayato.saphyritems.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerEvents implements Listener {

    public HashMap<ItemStack, Integer> itemData = new HashMap<>();
    public HashMap<UUID, HashMap<ItemStack, Integer>> infiniteList = new HashMap<>();
    public HashMap<UUID, Integer> thorCooldown = new HashMap<>();
    public HashMap<UUID, Integer> witherCooldown = new HashMap<>();

    public HashMap<UUID, List<String>> effects = new HashMap<>();

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
                        } else if (line.contains("%lastkill%")) {
                            line = line.replace("%lastkill%", player.getName());
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

    @EventHandler
    public void swordActions(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.AIR) return;
        final Player player = event.getPlayer();
        NBTItem nbtItem = new NBTItem(player.getItemInHand());
        if (nbtItem.hasKey("item")) {
            if (nbtItem.hasKey("thor")) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!thorCooldown.containsKey(player.getUniqueId())) {
                        player.getWorld().strikeLightning(player.getTargetBlock((HashSet<Byte>) null, 100).getLocation());
                        Integer cooldown = nbtItem.getInteger("thor-cooldown");
                        thorCooldown.put(player.getUniqueId(), cooldown);
                        new Thread(() -> {
                            try {
                                Thread.sleep(cooldown * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            thorCooldown.remove(player.getUniqueId());
                        }).start();
                    }
                }
            }
            if (nbtItem.hasKey(("wither"))) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (!witherCooldown.containsKey(player.getUniqueId())) {
                        for (Player target : player.getWorld().getPlayers()) {
                            Location loc = player.getTargetBlock((HashSet<Byte>) null, 100).getLocation();
                            Integer radius = nbtItem.getInteger("wither-radius");
                            if (target.getLocation().distance(loc) <= radius) {
                                Integer cooldown = nbtItem.getInteger("wither-cooldown");
                                Integer duration = nbtItem.getInteger("wither-duration");
                                Integer amplifier = nbtItem.getInteger("wither-amplifier");
                                target.addPotionEffect(PotionEffectType.WITHER.createEffect(duration, amplifier));
                                witherCooldown.put(player.getUniqueId(), cooldown);
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(cooldown * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    witherCooldown.remove(player.getUniqueId());
                                }).start();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void holdItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItem(event.getNewSlot()) == null || player.getInventory().getItem(event.getNewSlot()).getType() == Material.AIR) {
            if (effects.containsKey(player.getUniqueId())) {
                for (String effect : effects.get(player.getUniqueId())) {
                    String[] split = effect.split(":");
                    PotionEffectType type = PotionEffectType.getByName(split[0]);
                    player.removePotionEffect(type);
                }
                effects.remove(player.getUniqueId());
            }
            return;
        }
        ItemStack current = player.getInventory().getItem(event.getNewSlot());
        NBTItem nbtItem = new NBTItem(current);
        if (nbtItem.hasKey("item")) {
            if (nbtItem.hasKey("effects")) {
                String effectType = nbtItem.getString("effects-type");
                if (!Objects.equals(effectType, "hand")) return;
                try {
                    for (String effect : effects.get(player.getUniqueId())) {
                        String[] split = effect.split(":");
                        PotionEffectType type = PotionEffectType.getByName(split[0]);
                        player.removePotionEffect(type);
                    }
                    effects.remove(player.getUniqueId());
                } catch (NullPointerException ignored) {
                }
                String list = nbtItem.getString("effects-list");
                List<String> effectList = Arrays.asList(list.split(","));
                for (String effect : effectList) {
                    String[] split = effect.split(":");
                    PotionEffectType type = PotionEffectType.getByName(split[0]);
                    Integer amplifier = Integer.parseInt(split[1]);
                    player.addPotionEffect(type.createEffect(99999, amplifier));
                }
                effects.put(player.getUniqueId(), effectList);
            }
        }
    }
}
