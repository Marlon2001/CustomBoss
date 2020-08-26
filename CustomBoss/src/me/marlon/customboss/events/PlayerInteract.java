package me.marlon.customboss.events;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;
import me.marlon.customboss.managers.FileManager;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PlayerInteract implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerInteractEvent(PlayerInteractEvent event) {
        final FileManager fileManager = plugin.getFileManager();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem() && event.getMaterial() == Material.MONSTER_EGG) {
            Player player = event.getPlayer();
            ItemStack mEgg = player.getItemInHand();
            HashMap<String, HashMap<String, Object>> bossesSpawnEgg = plugin.getBossesManager().getBossesSpawnEgg();

            for (String key : bossesSpawnEgg.keySet()) {
                HashMap<String, Object> bossSpawnEgg = bossesSpawnEgg.get(key);

                //String[] itemID = (String[]) bossSpawnEgg.get("ItemID");
                String spawnEggName = (String) bossSpawnEgg.get("Name");
                List<String> item_lore = (List<String>) bossSpawnEgg.get("ItemLore");
                item_lore.replaceAll(Utils::color);

                List<String> lores = new ArrayList<>();
                String displayname = "";
                if (mEgg.hasItemMeta()) {
                    if (mEgg.getItemMeta().hasLore())
                        lores = mEgg.getItemMeta().getLore();
                    if (mEgg.getItemMeta().hasDisplayName())
                        displayname = mEgg.getItemMeta().getDisplayName();
                }

                if (displayname.equalsIgnoreCase(spawnEggName) && lores.equals(item_lore)) {
                    Location spawnLoc = event.getClickedBlock().getLocation();

                    if(compareDistances(spawnLoc)) {
                        if (spawnLoc.getWorld().getDifficulty() != Difficulty.PEACEFUL) {
                            if (getRegionFlag(spawnLoc)) {
                                if (fileManager.getConfig("Boss.yml").contains("Bosses." + key + ".Boss")) {
                                    ConfigurationSection sBoss = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses." + key + ".Boss");
                                    plugin.getBossesManager().spawnBoss(player, spawnLoc, sBoss, key);
                                } else {
                                    bossesSpawnEgg.remove(key);

                                    int randomNum = Utils.randInt(0, bossesSpawnEgg.size() - 1);
                                    String strBoss = (String) bossesSpawnEgg.keySet().toArray()[randomNum];

                                    ConfigurationSection sBoss = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses." + strBoss + ".Boss");
                                    plugin.getBossesManager().spawnBoss(player, spawnLoc, sBoss, strBoss);
                                }
                                int amount = player.getInventory().getItemInHand().getAmount();
                                if (player.getGameMode() != GameMode.CREATIVE) {
                                    if (amount > 1) {
                                        player.getInventory().getItemInHand().setAmount(amount - 1);
                                    } else {
                                        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage(fileManager.getMessage("notspawn_chunk"));
                    }
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    protected boolean compareDistances(Location spawnLoc) {
        Collection<LivingEntity> bossesVivos = plugin.getBossesManager().getBossesVivos().values();

        for (LivingEntity e : bossesVivos) {
            if(distance(spawnLoc, e.getLocation()) <= 64) {
                return false;
            }
        }
        return true;
    }

    protected double distance(Location a, Location b){
        return Math.sqrt(square(a.getX() - b.getX()) + square(a.getY() - b.getY()) + square(a.getZ() - b.getZ()));
    }

    protected double square(double x){
        return x * x;
    }

    protected Plugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        } else {
            return plugin;
        }
    }

    protected boolean getRegionFlag(Location location) {
        Plugin plugin = getWorldGuard();

        boolean flag = true;
        if (plugin != null) {
            WorldGuardPlugin worldGuardPlugin = (WorldGuardPlugin) plugin;
            RegionManager regionManager = worldGuardPlugin.getRegionManager(location.getWorld());
            ApplicableRegionSet regionSet = regionManager.getApplicableRegions(location);

            if (regionSet.queryState(null, DefaultFlag.MOB_SPAWNING) != StateFlag.State.ALLOW) {
                flag = false;
            }
        }
        return flag;
    }
}
