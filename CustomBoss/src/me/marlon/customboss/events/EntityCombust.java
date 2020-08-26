package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class EntityCombust implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityCombustEvent(EntityCombustEvent event) {
        Entity en = event.getEntity();
        Set<UUID> bosses = plugin.getBossesManager().getBossesVivos().keySet();

        if (bosses.contains(en.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    /*
    @EventHandler(priority = EventPriority.NORMAL)
    public void entityCombustByEntityEvent(EntityCombustByEntityEvent event) {
        Entity en = event.getEntity();
        ArrayList<UUID> bosses = plugin.getBossesManager().getBossesVivos();

        plugin.getServer().broadcastMessage("3");
        if (bosses.contains(en.getUniqueId())) {
            event.setCancelled(true);
        }
    }
    */
}
