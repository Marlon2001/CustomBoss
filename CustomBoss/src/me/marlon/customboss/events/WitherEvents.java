package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.Set;
import java.util.UUID;

public class WitherEvents implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityChangeBlockEvent(EntityChangeBlockEvent event) {
        Entity entity;
        try {
            Set<UUID> uuids = plugin.getBossesManager().getBossesVivos().keySet();
            entity = event.getEntity();

            if (entity.getType().equals(EntityType.WITHER) && uuids.contains(entity.getUniqueId())) {
                event.setCancelled(true);
            }

        } catch (Exception ignored) {
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void hangingBreakByEntityEvent(HangingBreakByEntityEvent event) {
        Entity entity;
        try {
            Set<UUID> uuids = plugin.getBossesManager().getBossesVivos().keySet();
            entity = event.getEntity();

            if (entity.getType().equals(EntityType.WITHER) && uuids.contains(entity.getUniqueId())) {
                event.setCancelled(true);
            }
        } catch (Exception ignored) {
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityExplodeEvent(EntityExplodeEvent event) {
        Entity entity;

        try {
            Set<UUID> uuids = plugin.getBossesManager().getBossesVivos().keySet();
            entity = event.getEntity();

            if (entity.getType().equals(EntityType.WITHER) && uuids.contains(entity.getUniqueId())) {
                event.setCancelled(true);
            } else if (entity.getType().equals(EntityType.WITHER_SKULL)) {
                event.setCancelled(true);
            }
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void explosionPrimeEvent(ExplosionPrimeEvent event) {
        Entity entity;

        try {
            Set<UUID> uuids = plugin.getBossesManager().getBossesVivos().keySet();
            entity = event.getEntity();

            if (entity.getType().equals(EntityType.WITHER) && uuids.contains(entity.getUniqueId())) {
                event.setCancelled(true);
            } else if (entity.getType().equals(EntityType.WITHER_SKULL) && uuids.contains(entity.getUniqueId())) {
                event.setCancelled(true);
            }
        } catch (Exception ignored) {
        }
    }
}
