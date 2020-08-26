package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EntityDamageByEntity implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entityTarget = event.getEntity();
        Entity damager = event.getDamager();
        Set<UUID> bosses = plugin.getBossesManager().getBossesVivos().keySet();
        HashMap<String, HashMap<String, Object>> espadas = plugin.getEspadasManager().getEspadas();
        HashMap<UUID, List<LivingEntity>> servos = plugin.getBossesManager().getMapServos();

        if (damager instanceof Player) {
            Player player = (Player) damager;

            if (entityTarget instanceof LivingEntity) {
                if (swords().contains(player.getItemInHand().getData().getItemType())) {
                    ItemStack sword = player.getItemInHand();
                    int danoPadrao = plugin.getConfigurationManager().getDanoPadrao();

                    boolean bool1 = false;
                    boolean bool2 = false;
                    for (String key : espadas.keySet()) {
                        HashMap<String, Object> espada = espadas.get(key);
                        int dano1 = (int) espada.get("Dano");

                        if (((String) espada.get("Name")).equalsIgnoreCase(sword.getItemMeta().getDisplayName())) {
                            if (bosses.contains(entityTarget.getUniqueId())) {
                                event.setDamage(dano1);
                                bool1 = true;
                                break;
                            } else {
                                for (UUID u : servos.keySet()) {
                                    for (LivingEntity e : servos.get(u)) {
                                        if (e.getUniqueId().equals(entityTarget.getUniqueId())) {
                                            event.setDamage(dano1);
                                            bool2 = true;
                                            bool1 = true;
                                            break;
                                        }
                                    }
                                    if (bool2) {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (!bool1) {
                        event.setDamage(danoPadrao);
                    }
                }
            }
        }
    }

    protected ArrayList<Material> swords() {
        ArrayList<Material> swords = new ArrayList<>();
        swords.add(Material.DIAMOND_SWORD);
        swords.add(Material.IRON_SWORD);
        swords.add(Material.GOLD_SWORD);
        swords.add(Material.STONE_SWORD);
        swords.add(Material.WOOD_SWORD);
        return swords;
    }
}
