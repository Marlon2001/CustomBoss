package me.marlon.customboss;

import me.marlon.customboss.Utils.MobBarUtils;
import me.marlon.customboss.Utils.StringBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MobHealthBar {

    private static LinkedHashMap<Integer, StringBoolean> namesTable;
    private static Map<Integer, Integer> mobTable;
    private final Plugin plugin;
    private final String[] barArray;
    private final BukkitScheduler scheduler;
    private final int mobHideDelay = 5;

    public MobHealthBar() {
        plugin = CustomBoss.getInstance();
        scheduler = Bukkit.getScheduler();
        mobTable = new HashMap<>();
        namesTable = new LinkedHashMap<>();
        barArray = MobBarUtils.getDefaultsBars(((CustomBoss) plugin).getConfigurationManager().getMobBarStyle());
    }

    public void showMobHealthBar(LivingEntity mob) {
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            double health = mob.getHealth();
            double max = mob.getMaxHealth();

            if (health > 0.0D) {
                if (barArray != null) {
                    mob.setCustomName("§r" + barArray[MobBarUtils.roundUpPositiveWithMax(health / max * 20.0D, 20)]);
                } else {
                    String sb = "§rHealth: " + MobBarUtils.roundUpPositive(health) +
                            "/" +
                            MobBarUtils.roundUpPositive(max);
                    mob.setCustomName(sb);
                }

                mob.setCustomNameVisible(true);
            }
        });
    }

    public void parseMobHit(LivingEntity mob, boolean damagedByEntity) {
        EntityType type = mob.getType();

        if (type != EntityType.WITHER && type != EntityType.ENDER_DRAGON) {
            if (type != EntityType.HORSE || mob.isEmpty()) {
                String customName = mob.getCustomName();

                if (customName != null && !customName.startsWith("§r")) {
                    StringBoolean stringBoolean = new StringBoolean(customName, mob.isCustomNameVisible());
                    namesTable.put(mob.getEntityId(), stringBoolean);
                }

                if (mobHideDelay == 0L) {
                    showMobHealthBar(mob);
                } else if (damagedByEntity) {
                    Integer eventualTaskID = mobTable.remove(mob.getEntityId());
                    if (eventualTaskID != null) {
                        scheduler.cancelTask(eventualTaskID);
                    }

                    showMobHealthBar(mob);
                    hideMobBarLater(mob);
                } else {
                    if (mobTable.containsKey(mob.getEntityId())) {
                        showMobHealthBar(mob);
                    }
                }
            }
        }
    }

    private void hideMobBarLater(final LivingEntity mob) {
        int id = mob.getEntityId();
        mobTable.put(id, scheduler.scheduleSyncDelayedTask(plugin, () -> hideBar(mob), mobHideDelay * 20));
    }

    public void hideBar(LivingEntity mob) {
        String customName = mob.getCustomName();

        if (customName == null || customName.startsWith("§r")) {
            Integer id = mobTable.remove(mob.getEntityId());
            if (id != null) {
                scheduler.cancelTask(id);
            }

            int idForName = mob.getEntityId();
            StringBoolean sb = namesTable.remove(idForName);
            if (sb != null) {
                mob.setCustomName(sb.getString());
                mob.setCustomNameVisible(sb.getBoolean());
            } else {
                mob.setCustomName("");
                mob.setCustomNameVisible(false);
            }
        }
    }

    public static String getNameWhileHavingBar(LivingEntity mob) {
        if(mob != null) {
            String customName = mob.getCustomName();

            if (customName == null) {
                return null;
            } else if (customName.startsWith("§r")) {
                int id = mob.getEntityId();
                StringBoolean sb = namesTable.get(id);
                if (sb != null) {
                    return sb.getString();
                }

                return null;
            } else {
                return customName;
            }
        } else {
            return "";
        }
    }
}
