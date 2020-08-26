package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class EntityDamage implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        Set<UUID> bosses = plugin.getBossesManager().getBossesVivos().keySet();
        HashMap<UUID, List<LivingEntity>> servos = plugin.getBossesManager().getMapServos();

        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;

            if (getDamageCauses().contains(event.getCause())) {
                if (bosses.contains(entity.getUniqueId())) {
                    event.setCancelled(true);
                } else {
                    boolean bool = false;
                    for (UUID u : servos.keySet()) {
                        for (LivingEntity e : servos.get(u)) {
                            if (e.getUniqueId().equals(entity.getUniqueId())) {
                                bool = true;
                                event.setCancelled(true);
                                break;
                            }
                        }
                        if(bool) {
                            break;
                        }
                    }
                }
            }

            if ((float) living.getNoDamageTicks() <= (float) living.getMaximumNoDamageTicks() / 2.0F) {
                if (bosses.contains(entity.getUniqueId())) {
                    plugin.getMobHealthBar().parseMobHit(living, event instanceof EntityDamageByEntityEvent);
                } else {
                    for (UUID uuid : servos.keySet()) {
                        for (LivingEntity e : servos.get(uuid)) {
                            if (e.getUniqueId().equals(entity.getUniqueId())) {
                                plugin.getMobHealthBar().parseMobHit(living, event instanceof EntityDamageByEntityEvent);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    protected List<EntityDamageEvent.DamageCause> getDamageCauses() {
        List<EntityDamageEvent.DamageCause> damageCauses = new ArrayList<>();

        damageCauses.add(EntityDamageEvent.DamageCause.SUFFOCATION);
        damageCauses.add(EntityDamageEvent.DamageCause.DROWNING);
        damageCauses.add(EntityDamageEvent.DamageCause.FIRE);
        damageCauses.add(EntityDamageEvent.DamageCause.FIRE_TICK);
        damageCauses.add(EntityDamageEvent.DamageCause.FALL);
        damageCauses.add(EntityDamageEvent.DamageCause.FALLING_BLOCK);
        damageCauses.add(EntityDamageEvent.DamageCause.LAVA);

        return damageCauses;
    }
}
