package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChunkUnload implements Listener {

    private final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void chunkUnloadEvent(ChunkUnloadEvent event) {
        HashMap<UUID, LivingEntity> bossesVivos = plugin.getBossesManager().getBossesVivos();
        HashMap<UUID, List<LivingEntity>> mapServos = plugin.getBossesManager().getMapServos();
        HashMap<UUID, Integer>  servosTasksId = plugin.getBossesManager().getServosTasksId();
        HashMap<UUID, Integer> bossesTasksId = plugin.getBossesManager().getBossesTasksId();
        Entity[] entities = event.getChunk().getEntities();

        boolean b = false;
        for (Entity ec : entities) {
            for (LivingEntity e : bossesVivos.values()) {
                if (e.getUniqueId().equals(ec.getUniqueId())) {
                    UUID uuid = e.getUniqueId();
                    e.setHealth(0);
                    mapServos.get(uuid).forEach(i -> i.setHealth(0));
                    int id = bossesTasksId.get(uuid);
                    bukkitScheduler.cancelTask(id);
                    id = servosTasksId.get(uuid);
                    bukkitScheduler.cancelTask(id);
                    bossesVivos.remove(uuid);
                    mapServos.remove(uuid);
                    servosTasksId.remove(uuid);
                    bossesTasksId.remove(uuid);

                    plugin.getBossesManager().setBossesVivos(bossesVivos);
                    plugin.getBossesManager().setMapServos(mapServos);
                    plugin.getBossesManager().setServosTasksId(servosTasksId);
                    plugin.getBossesManager().setBossesTasksId(bossesTasksId);
                    b = true;
                    break;
                }
            }
            if (b) {
                break;
            }
        }
    }
}
