package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Set;
import java.util.UUID;

public class PlayerInteractEntity implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Set<UUID> bosses = plugin.getBossesManager().getBossesVivos().keySet();
        Player player;

        if (event.getPlayer() != null) {
            player = event.getPlayer();

            if (player.getItemInHand().getType() == Material.NAME_TAG) {
                if (event.getRightClicked() instanceof LivingEntity) {
                    Entity e = event.getRightClicked();

                    if (bosses.contains(e.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
