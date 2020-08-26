package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.*;

public class EntityTarget implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityTargetEvent(EntityTargetEvent event) {
        Entity entity;
        Entity entityTarget;

        try {
            Set<UUID> uuids = plugin.getBossesManager().getBossesVivos().keySet();
            Set<UUID> uuidsServos = new HashSet<>();
            HashMap<UUID, List<LivingEntity>> mapServos = plugin.getBossesManager().getMapServos();
            entity = event.getEntity();
            entityTarget = event.getTarget();

            for (UUID u : mapServos.keySet()) {
                for (LivingEntity e : mapServos.get(u)) {
                    uuidsServos.add(e.getUniqueId());
                }
            }

            if (uuids.contains(entity.getUniqueId()) || uuidsServos.contains(entity.getUniqueId())) {
                if(!(entityTarget instanceof Player)) {
                    event.setCancelled(true);
                }
            } else if (uuids.contains(entityTarget.getUniqueId()) || uuidsServos.contains(entityTarget.getUniqueId())) {
                event.setCancelled(true);
            }
        } catch (Exception ignored) {
        }
    }
}
