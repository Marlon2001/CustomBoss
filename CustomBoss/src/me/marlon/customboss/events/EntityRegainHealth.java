package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EntityRegainHealth implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    public void entityRegainHealthEvent(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        Set<UUID> bossesVivos = plugin.getBossesManager().getBossesVivos().keySet();
        HashMap<UUID, List<LivingEntity>> mapServos = plugin.getBossesManager().getMapServos();

        //plugin.getMobHealthBar().parseMobHit((LivingEntity) entity, true);
        if (bossesVivos.contains(entity.getUniqueId())) {
            event.setCancelled(true);
        } else {
            boolean b = false;
            for (UUID u : mapServos.keySet()) {
                for (LivingEntity e : mapServos.get(u)) {
                    if (e.getUniqueId().equals(entity.getUniqueId())) {
                        event.setCancelled(true);
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
}
