package me.marlon.customboss.Utils;

import me.marlon.customboss.CustomBoss;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Utils {

    public static String color(String msg) {
        if (msg != null) {
            msg = msg.replaceAll("(&([a-f0-9]))", "§$2");
            msg = msg.replaceAll("&l", "" + ChatColor.BOLD);
            msg = msg.replaceAll("&o", "" + ChatColor.ITALIC);
            msg = msg.replaceAll("&k", "" + ChatColor.MAGIC);
            msg = msg.replaceAll("&n", "" + ChatColor.UNDERLINE);
            msg = msg.replaceAll("&m", "" + ChatColor.STRIKETHROUGH);

            return msg;
        } else {
            return null;
        }
    }

    public static ItemStack makeItem(Material material, String name, int amount, int type, List<String> lore, HashMap<Enchantment, Integer> enchantments) {
        if (material != null) {
            ItemStack item = new ItemStack(material, amount, (byte) type);

            ItemMeta m = item.getItemMeta();
            m.setDisplayName(color(name));
            m.setLore(lore);
            item.setItemMeta(m);
            item.addUnsafeEnchantments(enchantments);
            return item;
        } else {
            return new ItemStack(Material.AIR);
        }
    }

    public static ArrayList<ItemStack> getItems(List<String> customdrops) {
        ArrayList<ItemStack> items = new ArrayList<>();

        if (customdrops.size() > 0) {
            for (String customdrop : customdrops) {
                customdrop = customdrop.trim();
                String id = "0";
                String data = "0";
                String name = "";
                int amount = 1;
                ArrayList<String> lore = new ArrayList<>();
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                List<String> drop = new ArrayList<>();

                if (customdrop.contains("|")) {
                    drop.addAll(Arrays.asList(customdrop.split("\\|")));
                } else {
                    drop.add(customdrop);
                }

                for (String i : drop) {
                    i = i.trim();
                    if (i.contains("ItemID:")) {
                        i = i.replaceAll("ItemID:", "");
                        if (i.contains(":") && i.split(":").length >= 2) {
                            id = i.split(":")[0];
                            data = i.split(":")[1];
                        } else {
                            id = i;
                        }
                    } else if (i.contains("Name:")) {
                        i = i.replaceAll("Name:", "");
                        i = i.replaceAll("_", " ");
                        name = color(i);
                    } else if (i.contains("Amount:")) {
                        i = i.replaceAll("Amount:", "");
                        try {
                            amount = Integer.parseInt(i);
                        } catch (Exception ignored) {
                        }
                    } else if (i.contains("Lore:") || i.contains("Lores:")) {
                        if (i.contains("Lore:"))
                            i = i.replaceAll("Lores:", "");
                        else
                            i = i.replaceAll("Lore:", "");

                        if (i.contains(",")) {
                            String[] lores = i.split(",");

                            for (String l : lores) {
                                l = color(l);
                                l = l.replaceAll("_", " ");
                                lore.add(l);
                            }
                        } else {
                            i = color(i);
                            i = i.replaceAll("_", " ");
                            lore.add(i);
                        }
                    } else if (i.contains("Enchants:") || i.contains("Enchant:")) {
                        if (i.contains("Enchants:"))
                            i = i.replaceAll("Enchants:", "");
                        else
                            i = i.replaceAll("Enchant:", "");

                        for (String enchant : getEnchants()) {
                            if (!i.contains(",")) {
                                if (i.contains(enchant + ":")) {
                                    String[] breakdown = i.split(":");
                                    String enchantment = breakdown[0];
                                    int lvl = Integer.parseInt(breakdown[1]);
                                    enchantments.put(Enchantment.getByName(enchantment), lvl);
                                }
                            } else {
                                String[] e = i.split(",");

                                for (String en : e) {
                                    if (en.contains(enchant + ":")) {
                                        String[] breakdown = en.split(":");
                                        String enchantment = breakdown[0];
                                        int lvl = Integer.parseInt(breakdown[1]);
                                        enchantments.put(Enchantment.getByName(enchantment), lvl);
                                    }
                                }
                            }
                        }
                    }
                }

                ItemStack item = new ItemStack(Material.matchMaterial(id), amount, Short.parseShort(data));
                ItemMeta m = item.getItemMeta();
                if (m != null) {
                    m.setDisplayName(name);
                    m.setLore(lore);
                }
                item.setItemMeta(m);
                item.addUnsafeEnchantments(enchantments);
                items.add(item);
            }
        }

        return items;
    }

    public static ItemStack[] makeArmor(String armorType, HashMap<Enchantment, Integer> armorEnchantments) {
        ItemStack helmet = new ItemStack(Material.AIR);
        ItemStack chestplate = new ItemStack(Material.AIR);
        ItemStack leggings = new ItemStack(Material.AIR);
        ItemStack boots = new ItemStack(Material.AIR);

        if (armorType.equalsIgnoreCase("Leather")) {
            helmet = new ItemStack(Material.LEATHER_HELMET);
            helmet.addUnsafeEnchantments(armorEnchantments);

            chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            chestplate.addUnsafeEnchantments(armorEnchantments);

            leggings = new ItemStack(Material.LEATHER_LEGGINGS);
            leggings.addUnsafeEnchantments(armorEnchantments);

            boots = new ItemStack(Material.LEATHER_BOOTS);
            boots.addUnsafeEnchantments(armorEnchantments);
        } else if (armorType.equalsIgnoreCase("ChainMail")) {
            helmet = new ItemStack(Material.CHAINMAIL_HELMET);
            helmet.addUnsafeEnchantments(armorEnchantments);

            chestplate = new ItemStack(Material.CHAINMAIL_HELMET);
            chestplate.addUnsafeEnchantments(armorEnchantments);

            leggings = new ItemStack(Material.CHAINMAIL_HELMET);
            leggings.addUnsafeEnchantments(armorEnchantments);

            boots = new ItemStack(Material.CHAINMAIL_HELMET);
            boots.addUnsafeEnchantments(armorEnchantments);
        } else if (armorType.equalsIgnoreCase("Gold")) {
            helmet = new ItemStack(Material.GOLD_HELMET);
            helmet.addUnsafeEnchantments(armorEnchantments);

            chestplate = new ItemStack(Material.GOLD_HELMET);
            chestplate.addUnsafeEnchantments(armorEnchantments);

            leggings = new ItemStack(Material.GOLD_HELMET);
            leggings.addUnsafeEnchantments(armorEnchantments);

            boots = new ItemStack(Material.GOLD_HELMET);
            boots.addUnsafeEnchantments(armorEnchantments);
        } else if (armorType.equalsIgnoreCase("Iron")) {
            helmet = new ItemStack(Material.IRON_HELMET);
            helmet.addUnsafeEnchantments(armorEnchantments);

            chestplate = new ItemStack(Material.IRON_HELMET);
            chestplate.addUnsafeEnchantments(armorEnchantments);

            leggings = new ItemStack(Material.IRON_HELMET);
            leggings.addUnsafeEnchantments(armorEnchantments);

            boots = new ItemStack(Material.IRON_HELMET);
            boots.addUnsafeEnchantments(armorEnchantments);
        } else if (armorType.equalsIgnoreCase("Diamond")) {
            helmet = new ItemStack(Material.DIAMOND_HELMET);
            helmet.addUnsafeEnchantments(armorEnchantments);

            chestplate = new ItemStack(Material.DIAMOND_HELMET);
            chestplate.addUnsafeEnchantments(armorEnchantments);

            leggings = new ItemStack(Material.DIAMOND_HELMET);
            leggings.addUnsafeEnchantments(armorEnchantments);

            boots = new ItemStack(Material.DIAMOND_HELMET);
            boots.addUnsafeEnchantments(armorEnchantments);
        }

        return new ItemStack[]{helmet, chestplate, leggings, boots};
    }

    public static LivingEntity getBossType(Player player, Location spawnLoc, String bosstype) {
        LivingEntity boss = null;

        Location loc1 = spawnLoc.add(0D, 1D, 0D);
        Location loc2 = spawnLoc;
        if (!loc1.getBlock().getType().equals(Material.AIR) || !loc2.getBlock().getType().equals(Material.AIR)) {
            spawnLoc = player.getLocation();
        }

        if (bosstype.equalsIgnoreCase("Zombie")) {
            Zombie zombie = spawnLoc.getWorld().spawn(spawnLoc, Zombie.class);
            zombie.setVillager(false);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    zombie.setTarget(player);
            boss = zombie;
        } else if (bosstype.equalsIgnoreCase("Skeleton")) {
            Skeleton skeleton = spawnLoc.getWorld().spawn(spawnLoc, Skeleton.class);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    skeleton.setTarget(player);
            boss = skeleton;
        } else if (bosstype.equalsIgnoreCase("Slime")) {
            boss = spawnLoc.getWorld().spawn(spawnLoc, Slime.class);
        } else if (bosstype.equalsIgnoreCase("Witch")) {
            Witch witch = spawnLoc.getWorld().spawn(spawnLoc, Witch.class);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    witch.setTarget(player);
            boss = witch;
        } else if (bosstype.equalsIgnoreCase("Iron Golem") || bosstype.equalsIgnoreCase("IronGolem")) {
            IronGolem ironGolem = spawnLoc.getWorld().spawn(spawnLoc, IronGolem.class);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    ironGolem.setTarget(player);
            boss = ironGolem;
        } else if (bosstype.equalsIgnoreCase("Spider")) {
            Spider spider = spawnLoc.getWorld().spawn(spawnLoc, Spider.class);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    spider.setTarget(player);
            boss = spider;
        } else if (bosstype.equalsIgnoreCase("EnderMan")) {
            Enderman enderman = spawnLoc.getWorld().spawn(spawnLoc, Enderman.class);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    enderman.setTarget(player);
            boss = enderman;
        } else if (bosstype.equalsIgnoreCase("Blaze")) {
            Blaze blaze = spawnLoc.getWorld().spawn(spawnLoc, Blaze.class);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    blaze.setTarget(player);
            boss = blaze;
        } else if (bosstype.equalsIgnoreCase("Silver Fish") || bosstype.equalsIgnoreCase("SilverFish")) {
            Silverfish silverfish = spawnLoc.getWorld().spawn(spawnLoc, Silverfish.class);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    silverfish.setTarget(player);
            boss = silverfish;
        } else if (bosstype.equalsIgnoreCase("Wither Boss") || bosstype.equalsIgnoreCase("WitherBoss")) {
            Wither wither = spawnLoc.getWorld().spawn(spawnLoc, Wither.class);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    wither.setTarget(player);
            boss = wither;
        } else if (bosstype.equalsIgnoreCase("Wither Skeleton") || bosstype.equalsIgnoreCase("WitherSkeleton")) {
            Skeleton s = spawnLoc.getWorld().spawn(spawnLoc, Skeleton.class);
            s.setSkeletonType(Skeleton.SkeletonType.WITHER);
            if (player != null)
                if (!(player.getGameMode() == GameMode.CREATIVE))
                    s.setTarget(player);
            boss = s;
        } else if (bosstype.equalsIgnoreCase("Ender Dragon") || bosstype.equalsIgnoreCase("EnderDragon")) {
            boss = spawnLoc.getWorld().spawn(spawnLoc, EnderDragon.class);
        }

        return boss;
    }

    public static HashMap<UUID, LivingEntity> getBossesInWorlds(CustomBoss plugin) {
        List<World> worlds = Bukkit.getServer().getWorlds();
        ArrayList<UUID> uuids = plugin.getConfigurationManager().getBosses();

        HashMap<UUID, LivingEntity> bossesVivos = new HashMap<>();
        for (World world : worlds) {
            List<Entity> entities = world.getEntities();

            for (Entity e : entities) {
                if (e instanceof LivingEntity) {
                    LivingEntity en = (LivingEntity) e;

                    if (uuids.contains(en.getUniqueId())) {
                        bossesVivos.put(en.getUniqueId(), en);
                    }
                }
            }
        }

        return bossesVivos;
    }

    public static void killAllServos(HashMap<UUID, List<LivingEntity>> uuidsServos) {
        List<World> worlds = Bukkit.getServer().getWorlds();

        for (UUID u : uuidsServos.keySet()) {
            if (uuidsServos.get(u) != null) {
                uuidsServos.get(u).forEach(i -> i.setHealth(0));
            }
        }
        /*
        for (World world : worlds) {
            List<Entity> entities = world.getEntities();

            for (Entity e : entities) {
                if (e instanceof LivingEntity) {
                    if (uuidsServos.get(e.getUniqueId()) != null) {
                        uuidsServos.get(e.getUniqueId()).forEach(i -> i.setHealth(0));
                    }
                }
            }
        }
         */
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    protected static ArrayList<String> getEnchants() {
        ArrayList<String> enchant = new ArrayList<>();
        enchant.add("PROTECTION_ENVIRONMENTAL");
        enchant.add("PROTECTION_FIRE");
        enchant.add("PROTECTION_FALL");
        enchant.add("PROTECTION_EXPLOSIONS");
        enchant.add("ROTECTION_PROJECTILE");
        enchant.add("OXYGEN");
        enchant.add("WATER_WORKER");
        enchant.add("DAMAGE_ALL");
        enchant.add("DAMAGE_UNDEAD");
        enchant.add("DAMAGE_ARTHROPODS");
        enchant.add("KNOCKBACK");
        enchant.add("FIRE_ASPECT");
        enchant.add("LOOT_BONUS_MOBS");
        enchant.add("DIG_SPEED");
        enchant.add("SILK_TOUCH");
        enchant.add("DURABILITY");
        enchant.add("LOOT_BONUS_BLOCKS");
        enchant.add("ARROW_DAMAGE");
        enchant.add("ARROW_KNOCKBACK");
        enchant.add("ARROW_FIRE");
        enchant.add("ARROW_INFINITE");
        return enchant;
    }
}
