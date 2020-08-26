package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.*;

public class EntityDeath implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityDeathEvent(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();

        HashMap<String, HashMap<String, Object>> bosses = plugin.getBossesManager().getBosses();
        HashMap<UUID, LivingEntity> bossesVivos = plugin.getBossesManager().getBossesVivos();
        HashMap<UUID, List<LivingEntity>> mapServos = plugin.getBossesManager().getMapServos();

        String bossTypeCconfig = "";
        String bossName = "";
        if (entity.getMetadata("BossTypeConfig") != null) {
            List<MetadataValue> entityMetaData;
            entityMetaData = entity.getMetadata("BossTypeConfig");
            for (MetadataValue m : entityMetaData) {
                bossTypeCconfig = String.valueOf(m.value());
            }
            entityMetaData = entity.getMetadata("BossName");
            for (MetadataValue m : entityMetaData) {
                bossName = String.valueOf(m.value());
            }
        }

        if (bossesVivos.containsKey(entity.getUniqueId())) {
            for (String key : bosses.keySet()) {
                HashMap<String, Object> boss = bosses.get(key);

                if (bossTypeCconfig.equalsIgnoreCase(key)) {
                    if (!(boolean) boss.get("NaturalDrops"))
                        event.getDrops().clear();

                    if(boss.get("DropXP") != null) {
                        int dropXP = (int) boss.get("DropXP");
                        event.setDroppedExp(dropXP);
                    }
                    if(boss.get("CustomDrops") != null) {
                        event.getDrops().addAll(Utils.getItems((List<String>) boss.get("CustomDrops")));
                    }

                    if (bossName.equals(""))
                        bossName = (String) boss.get("Name");

                    if(boss.get("MessageToKiller") != null) {
                        List<String> msgs = (List<String>) boss.get("MessageToKiller");

                        for (String msg : msgs) {
                            String m;
                            m = msg.replaceAll("\\{player}", player.getName());
                            m = m.replaceAll("\\{boss}", bossName);
                            player.sendMessage(Utils.color(m));
                        }
                    }

                    List<String> commands = (List<String>) boss.get("Commands");

                    for (String command : commands) {
                        String c;
                        c = command.replaceAll("\\{player}", player.getName());
                        c = Utils.color(c);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c);
                    }

                    bossesVivos.remove(entity.getUniqueId());
                    plugin.getBossesManager().setBossesVivos(bossesVivos);
                    plugin.getBossesManager().removeMapServos(entity.getUniqueId());
                    plugin.getConfigurationManager().saveBosses(bossesVivos.keySet());

                    Set<UUID> uuids = new HashSet<>();
                    uuids.add(entity.getUniqueId());
                    plugin.getBossesManager().cancelTasks(uuids);
                    plugin.getMobHealthBar().hideBar(entity);
                    break;
                }
            }
        } else {
            boolean b = false;
            for (UUID u : mapServos.keySet()) {
                for (LivingEntity e : mapServos.get(u)) {
                    if (e.getUniqueId().equals(entity.getUniqueId())) {
                        plugin.getMobHealthBar().hideBar(entity);
                        event.getDrops().clear();
                        b = true;
                        break;
                    }
                }
                if (b)
                    break;
            }
        }
    }
}
