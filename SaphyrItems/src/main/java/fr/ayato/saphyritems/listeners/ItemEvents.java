package fr.ayato.saphyritems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ItemEvents implements Listener {

    private HashMap<UUID, List<String>> effectsHand = new HashMap<>();
    private HashMap<UUID, Integer> thorCooldown = new HashMap<>();
    private HashMap<UUID, Integer> witherCooldown = new HashMap<>();
    private HashMap<UUID, List<String>> effectsHelmet = new HashMap<>();
    private HashMap<UUID, List<String>> effectsChestplate = new HashMap<>();
    private HashMap<UUID, List<String>> effectsLeggings = new HashMap<>();
    private HashMap<UUID, List<String>> effectsBoots = new HashMap<>();

    //Cancel the drop of the item if it's a saphyr
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasKey("saphyr-item")) {
            event.setCancelled(true);
        }
    }

    //Give effect when the player take a saphyr item in his hand, or remove the effect when he takes another item
    @EventHandler
    public void switchItemEffects(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (effectsHand.containsKey(player.getUniqueId())) {
            for (String effect : effectsHand.get(player.getUniqueId())) {
                String[] split = effect.split(":");
                PotionEffectType type = PotionEffectType.getByName(split[0]);
                player.removePotionEffect(type);
            }
            effectsHand.remove(player.getUniqueId());
        }
        if (player.getInventory().getItem(event.getNewSlot()) == null) return;
        if (player.getInventory().getItem(event.getNewSlot()).getType() == Material.AIR) return;

        NBTItem nbtItem = new NBTItem(player.getInventory().getItem(event.getNewSlot()));
        if (!nbtItem.hasKey("effects")) return;

        String effectType = nbtItem.getString("effects-type");
        if (!Objects.equals(effectType, "hand")) return;

        String list = nbtItem.getString("effects-list");
        List<String> effectList = Arrays.asList(list.split(","));
        for (String effect : effectList) {
            String[] split = effect.split(":");
            PotionEffectType type = PotionEffectType.getByName(split[0]);
            Integer amplifier = Integer.parseInt(split[1]);
            player.addPotionEffect(type.createEffect(Integer.MAX_VALUE, amplifier));
        }
        effectsHand.put(player.getUniqueId(), effectList);
    }

    //When player right click with a saphyr item, give the effect
    @EventHandler
    public void rightClickItemEffects(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.AIR) return;
        if (event.getPlayer().getItemInHand() == null) return;
        if (event.getPlayer().getItemInHand().getType() == Material.AIR) return;
        final Player player = event.getPlayer();
        NBTItem nbtItem = new NBTItem(player.getItemInHand());
        if (nbtItem.hasKey("saphyr-item")) {
            if (nbtItem.hasKey("thor")) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                    return;
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

    //Give effects to the player depending on the saphyr armor he wears
    @EventHandler
    public void armorEffects(PlayerMoveEvent event) {
        if (event.getPlayer() != null) {
            Player player = event.getPlayer();
            ItemStack helmet = player.getInventory().getHelmet();
            ItemStack chestplate = player.getInventory().getChestplate();
            ItemStack leggings = player.getInventory().getLeggings();
            ItemStack boots = player.getInventory().getBoots();
            if (helmet != null) {
                NBTItem nbtHelmet = new NBTItem(helmet);
                if (nbtHelmet.hasKey("saphyr-item")) {
                    if (nbtHelmet.hasKey("effects")) {
                        String effectType = nbtHelmet.getString("effects-type");
                        if (!Objects.equals(effectType, "armor")) return;
                        String list = nbtHelmet.getString("effects-list");
                        List<String> effectList = Arrays.asList(list.split(","));
                        for (String effect : effectList) {
                            String[] split = effect.split(":");
                            PotionEffectType type = PotionEffectType.getByName(split[0]);
                            Integer amplifier = Integer.parseInt(split[1]);
                            player.addPotionEffect(type.createEffect(Integer.MAX_VALUE, amplifier));
                        }
                        effectsHelmet.put(player.getUniqueId(), effectList);
                    }
                } else {
                    if (effectsHelmet.containsKey(player.getUniqueId())) {
                        try {
                            for (String effect : effectsHelmet.get(player.getUniqueId())) {
                                String[] split = effect.split(":");
                                PotionEffectType type = PotionEffectType.getByName(split[0]);
                                player.removePotionEffect(type);
                            }
                            effectsHelmet.remove(player.getUniqueId());
                        } catch (NullPointerException ignored) {
                        }
                        effectsHelmet.remove(player.getUniqueId());
                    }
                }
            } else {
                if (effectsHelmet.containsKey(player.getUniqueId())) {
                    try {
                        for (String effect : effectsHelmet.get(player.getUniqueId())) {
                            String[] split = effect.split(":");
                            PotionEffectType type = PotionEffectType.getByName(split[0]);
                            player.removePotionEffect(type);
                        }
                        effectsHelmet.remove(player.getUniqueId());
                    } catch (NullPointerException ignored) {
                    }
                    effectsHelmet.remove(player.getUniqueId());
                }
            }
            if (chestplate != null) {
                NBTItem nbtChestplate = new NBTItem(chestplate);
                if (nbtChestplate.hasKey("saphyr-item")) {
                    if (nbtChestplate.hasKey("effects")) {
                        String effectType = nbtChestplate.getString("effects-type");
                        if (!Objects.equals(effectType, "armor")) return;
                        String list = nbtChestplate.getString("effects-list");
                        List<String> effectList = Arrays.asList(list.split(","));
                        for (String effect : effectList) {
                            String[] split = effect.split(":");
                            PotionEffectType type = PotionEffectType.getByName(split[0]);
                            Integer amplifier = Integer.parseInt(split[1]);
                            player.addPotionEffect(type.createEffect(Integer.MAX_VALUE, amplifier));
                        }
                        effectsChestplate.put(player.getUniqueId(), effectList);
                    }
                } else {
                    if (effectsChestplate.containsKey(player.getUniqueId())) {
                        try {
                            for (String effect : effectsChestplate.get(player.getUniqueId())) {
                                String[] split = effect.split(":");
                                PotionEffectType type = PotionEffectType.getByName(split[0]);
                                player.removePotionEffect(type);
                            }
                            effectsChestplate.remove(player.getUniqueId());
                        } catch (NullPointerException ignored) {
                        }
                        effectsChestplate.remove(player.getUniqueId());
                    }
                }
            } else {
                if (effectsChestplate.containsKey(player.getUniqueId())) {
                    try {
                        for (String effect : effectsChestplate.get(player.getUniqueId())) {
                            String[] split = effect.split(":");
                            PotionEffectType type = PotionEffectType.getByName(split[0]);
                            player.removePotionEffect(type);
                        }
                        effectsChestplate.remove(player.getUniqueId());
                    } catch (NullPointerException ignored) {
                    }
                    effectsChestplate.remove(player.getUniqueId());
                }
            }
            if (leggings != null) {
                NBTItem nbtLeggings = new NBTItem(leggings);
                if (nbtLeggings.hasKey("saphyr-item")) {
                    if (nbtLeggings.hasKey("effects")) {
                        String effectType = nbtLeggings.getString("effects-type");
                        if (!Objects.equals(effectType, "armor")) return;
                        String list = nbtLeggings.getString("effects-list");
                        List<String> effectList = Arrays.asList(list.split(","));
                        for (String effect : effectList) {
                            String[] split = effect.split(":");
                            PotionEffectType type = PotionEffectType.getByName(split[0]);
                            Integer amplifier = Integer.parseInt(split[1]);
                            player.addPotionEffect(type.createEffect(Integer.MAX_VALUE, amplifier));
                        }
                        effectsLeggings.put(player.getUniqueId(), effectList);
                    }
                } else {
                    if (effectsLeggings.containsKey(player.getUniqueId())) {
                        try {
                            for (String effect : effectsLeggings.get(player.getUniqueId())) {
                                String[] split = effect.split(":");
                                PotionEffectType type = PotionEffectType.getByName(split[0]);
                                player.removePotionEffect(type);
                            }
                            effectsLeggings.remove(player.getUniqueId());
                        } catch (NullPointerException ignored) {
                        }
                        effectsLeggings.remove(player.getUniqueId());
                    }
                }
            } else {
                if (effectsLeggings.containsKey(player.getUniqueId())) {
                    try {
                        for (String effect : effectsLeggings.get(player.getUniqueId())) {
                            String[] split = effect.split(":");
                            PotionEffectType type = PotionEffectType.getByName(split[0]);
                            player.removePotionEffect(type);
                        }
                        effectsLeggings.remove(player.getUniqueId());
                    } catch (NullPointerException ignored) {
                    }
                    effectsLeggings.remove(player.getUniqueId());
                }
            }
            if (boots != null) {
                NBTItem nbtBoots = new NBTItem(boots);
                if (nbtBoots.hasKey("saphyr-item")) {
                    if (nbtBoots.hasKey("effects")) {
                        String effectType = nbtBoots.getString("effects-type");
                        if (!Objects.equals(effectType, "armor")) return;
                        String list = nbtBoots.getString("effects-list");
                        List<String> effectList = Arrays.asList(list.split(","));
                        for (String effect : effectList) {
                            String[] split = effect.split(":");
                            PotionEffectType type = PotionEffectType.getByName(split[0]);
                            Integer amplifier = Integer.parseInt(split[1]);
                            player.addPotionEffect(type.createEffect(Integer.MAX_VALUE, amplifier));
                        }
                        effectsBoots.put(player.getUniqueId(), effectList);
                    }
                } else {
                    if (effectsBoots.containsKey(player.getUniqueId())) {
                        try {
                            for (String effect : effectsBoots.get(player.getUniqueId())) {
                                String[] split = effect.split(":");
                                PotionEffectType type = PotionEffectType.getByName(split[0]);
                                player.removePotionEffect(type);
                            }
                            effectsBoots.remove(player.getUniqueId());
                        } catch (NullPointerException ignored) {
                        }
                        effectsBoots.remove(player.getUniqueId());
                    }
                }
            } else {
                if (effectsBoots.containsKey(player.getUniqueId())) {
                    try {
                        for (String effect : effectsBoots.get(player.getUniqueId())) {
                            String[] split = effect.split(":");
                            PotionEffectType type = PotionEffectType.getByName(split[0]);
                            player.removePotionEffect(type);
                        }
                        effectsBoots.remove(player.getUniqueId());
                    } catch (NullPointerException ignored) {
                    }
                    effectsBoots.remove(player.getUniqueId());
                }
            }
        }
    }
}
