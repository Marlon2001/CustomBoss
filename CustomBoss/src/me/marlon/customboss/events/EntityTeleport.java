package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;

import java.util.*;

public class EntityTeleport implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityTeleportEvent(EntityTeleportEvent event) {
        Entity entity;

        try {
            Set<UUID> uuids = plugin.getBossesManager().getBossesVivos().keySet();
            Set<UUID> uuidsServos = new HashSet<>();
            HashMap<UUID, List<LivingEntity>> mapServos = plugin.getBossesManager().getMapServos();
            entity = event.getEntity();

            for (UUID u : mapServos.keySet()) {
                for (LivingEntity e : mapServos.get(u)) {
                    uuidsServos.add(e.getUniqueId());
                }
            }

            if (event.getEntityType().equals(EntityType.ENDERMAN)) {
                if (uuids.contains(entity.getUniqueId()) || uuidsServos.contains(entity.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        } catch (Exception ignored) { }
    }
}
