package me.marlon.customboss.managers;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BossesManager {

    private final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();
    private HashMap<UUID, Integer> servosTasksId = new HashMap<>();
    private HashMap<UUID, Integer> bossesTasksId = new HashMap<>();
    private HashMap<UUID, LivingEntity> bossesVivos;
    private HashMap<UUID, List<LivingEntity>> mapServos = new HashMap<>();

    public BossesManager() {
        this.bossesVivos = Utils.getBossesInWorlds(plugin);
        this.bossesVivos.values().forEach(i -> initSpawnServos(null, i, null));
        this.bossesVivos.values().forEach(i -> initBossDelay(i, null));
    }

    public HashMap<String, HashMap<String, Object>> getBossesGUI() {
        Set<String> bossNames = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses").getKeys(true);
        bossNames.removeIf(i -> i.contains("."));

        HashMap<String, HashMap<String, Object>> bossesGUI = new HashMap<>();

        for (String boss : bossNames) {
            ConfigurationSection sGui = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses." + boss + ".GUI");

            HashMap<String, Object> bossGui = new HashMap<>();
            bossGui.put("ItemID", sGui.getString("ItemID").split(":"));

            bossGui.put("Slot", sGui.getInt("Slot"));
            bossGui.put("Name", Utils.color(sGui.getString("Name")));
            List<String> item_lore = sGui.getStringList("Lore");
            item_lore.replaceAll(Utils::color);
            bossGui.put("ItemLore", item_lore);

            bossesGUI.put(boss, bossGui);
        }

        return bossesGUI;
    }

    public HashMap<String, HashMap<String, Object>> getBossesSpawnEgg() {
        Set<String> bossNames = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses").getKeys(true);
        bossNames.removeIf(i -> i.contains("."));

        HashMap<String, HashMap<String, Object>> bossesSpawnEgg = new HashMap<>();

        for (String boss : bossNames) {
            ConfigurationSection sSpawnEgg = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses." + boss + ".SpawnEgg");

            HashMap<String, Object> bossGui = new HashMap<>();
            bossGui.put("ItemID", sSpawnEgg.getString("ItemID").split(":"));
            bossGui.put("Name", Utils.color(sSpawnEgg.getString("Name")));
            List<String> item_lore = sSpawnEgg.getStringList("Lore");
            item_lore.replaceAll(Utils::color);
            bossGui.put("ItemLore", item_lore);

            bossesSpawnEgg.put(boss, bossGui);
        }

        return bossesSpawnEgg;
    }

    public HashMap<String, HashMap<String, Object>> getBosses() {
        Set<String> bossNames = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses").getKeys(true);
        bossNames.removeIf(i -> i.contains("."));

        HashMap<String, HashMap<String, Object>> bossesBoss = new HashMap<>();

        for (String boss : bossNames) {
            if (fileManager.getConfig("Boss.yml").contains("Bosses." + boss + ".Boss")) {
                ConfigurationSection sBoss = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses." + boss + ".Boss");

                HashMap<String, Object> boss1 = new HashMap<>();
                boss1.put("Name", Utils.color(sBoss.getString("Name")));
                boss1.put("MaxHealth", sBoss.getDouble("Health"));
                boss1.put("NaturalDrops", sBoss.getBoolean("NaturalDrops"));
                boss1.put("DropXP", sBoss.getInt("DropXP"));
                boss1.put("CustomDrops", sBoss.getStringList("CustomDrops"));
                boss1.put("MessageToKiller", sBoss.getStringList("MessageToKiller"));
                boss1.put("Commands", sBoss.getStringList("Commands"));

                bossesBoss.put(boss, boss1);
            }
        }

        return bossesBoss;
    }

    public void initBossDelay(LivingEntity boss, ConfigurationSection sBoss) {
        if (sBoss == null) {
            HashMap<String, HashMap<String, Object>> bosses = getBosses();

            List<String> metadataValues = new ArrayList<>();
            if (boss.getMetadata("BossTypeConfig") != null) {
                List<MetadataValue> entityMetaData = boss.getMetadata("BossTypeConfig");
                for (MetadataValue m : entityMetaData) {
                    metadataValues.add(String.valueOf(m.value()));
                }
            }

            for (String b1 : bosses.keySet()) {
                if (metadataValues.contains(b1)) {
                    if (fileManager.getConfig("Boss.yml").contains("Bosses." + b1 + ".Boss")) {
                        sBoss = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses." + b1 + ".Boss");
                        break;
                    }
                }
            }
        }

        if (sBoss != null) {
            final int[] seconds = {-1};

            if (sBoss.get("Tempo") != null) {
                seconds[0] = sBoss.getInt("Tempo");

                if (seconds[0] != -1 && seconds[0] > 0) {
                    bossesTasksId.put(boss.getUniqueId(), bukkitScheduler.scheduleSyncRepeatingTask(plugin, () -> {
                        if (seconds[0] > 0) {
                            long minutos = TimeUnit.SECONDS.toMinutes(seconds[0]);
                            long segundos = seconds[0] % 60;
                            String formato = addZero(minutos) + ":" + addZero(segundos);
                            String mensagem = ChatColor.GREEN + formato;

                            if (boss.isDead()) {
                                if (boss.getKiller() == null) {
                                    seconds[0] = 0;
                                } else if (boss.getLocation().getY() < 0) {
                                    boss.setHealth(0);
                                }
                            } else {
                                if (seconds[0] % 10 == 0) {
                                    if (!boss.getEyeLocation().getBlock().getType().equals(Material.AIR)) {
                                        EntityDamageEvent event = boss.getLastDamageCause();
                                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                                            if (event instanceof EntityDamageByEntityEvent) {
                                                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                                                if (e.getDamager() instanceof Player) {
                                                    boss.teleport(e.getDamager().getLocation());
                                                }
                                            }
                                        }
                                    }
                                } else if (seconds[0] % 15 == 0) {
                                    if (mapServos.get(boss.getUniqueId()) != null) {
                                        List<LivingEntity> servos = mapServos.get(boss.getUniqueId());
                                        for (LivingEntity ent : servos) {
                                            if (!ent.getEyeLocation().getBlock().getType().equals(Material.AIR)) {
                                                ent.teleport(boss);
                                            } else if (distance(boss.getLocation(), ent.getLocation()) > 10) {
                                                ent.teleport(boss);
                                            }
                                        }
                                    }
                                }

                                seconds[0]--;
                            }
                        } else {
                            UUID uuid = boss.getUniqueId();
                            boss.setHealth(0);
                            if (mapServos.get(uuid) != null) {
                                mapServos.get(uuid).forEach(i -> i.setHealth(0));
                            }

                            if (bossesTasksId.get(uuid) != null) {
                                int id = bossesTasksId.get(uuid);
                                bukkitScheduler.cancelTask(id);
                            }

                            if (servosTasksId.get(uuid) != null) {
                                int id = servosTasksId.get(uuid);
                                bukkitScheduler.cancelTask(id);
                            }
                            bossesTasksId.remove(uuid);
                            mapServos.remove(uuid);
                            bossesVivos.remove(uuid);

                            Collection<Entity> entities = boss.getLocation().getWorld().getNearbyEntities(boss.getLocation(), 30, 20, 30);
                            for (Entity e : entities) {
                                if (e instanceof Player) {
                                    Player p = (Player) e;
                                    p.sendMessage(fileManager.getMessage("boss_distancia_morreu"));
                                }
                            }
                        }
                    }, 0L, 20L));
                }
            }
        }
    }

    private void initSpawnServos(Player player, LivingEntity boss, ConfigurationSection sBoss) {
        ConfigurationSection sServos = null;

        if (sBoss != null) {
            sServos = sBoss.getConfigurationSection("Servos");
        } else {
            HashMap<String, HashMap<String, Object>> bosses = getBosses();

            List<String> metadataValues = new ArrayList<>();
            if (boss.getMetadata("BossTypeConfig") != null) {
                List<MetadataValue> entityMetaData = boss.getMetadata("BossTypeConfig");
                for (MetadataValue m : entityMetaData) {
                    metadataValues.add(String.valueOf(m.value()));
                }
            }

            for (String b1 : bosses.keySet()) {
                if (metadataValues.contains(b1)) {
                    if (fileManager.getConfig("Boss.yml").contains("Bosses." + b1 + ".Boss.Servos")) {
                        sServos = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses." + b1 + ".Boss.Servos");
                        break;
                    }
                }
            }
        }

        if (sServos != null) {
            int delay = sServos.getInt("Delay");

            final ConfigurationSection finalSServos = sServos;
            int taskid = bukkitScheduler.scheduleSyncRepeatingTask(plugin, () -> {
                if (!boss.isDead()) {
                    List<LivingEntity> servos = spawnServos(player, boss, finalSServos);
                    mapServos.put(boss.getUniqueId(), servos);
                }
            }, delay * 20, delay * 20);
            servosTasksId.put(boss.getUniqueId(), taskid);
        }
    }

    public void spawnBoss(Player player, Location spawnLoc, ConfigurationSection sBoss, String key) {
        LivingEntity boss;
        spawnLoc = spawnLoc.add(0.5D, 1D, 0.5D);

        String bosstype = sBoss.getString("BossType");
        boss = Utils.getBossType(player, spawnLoc, bosstype);

        if (boss != null) {
            String name = Utils.color(sBoss.getString("Name"));
            double maxHealth = sBoss.getDouble("Health");

            boss.setCustomName(name);
            boss.setMaxHealth(maxHealth);
            boss.setHealth(maxHealth);

            HashMap<Enchantment, Integer> armorEnchantments = new HashMap<>();

            if (sBoss.getStringList("ArmorEnchantments") != null) {
                for (String en : sBoss.getStringList("ArmorEnchantments")) {
                    String[] breakdown = en.split(":");
                    String enchantment = breakdown[0];
                    int lvl = Integer.parseInt(breakdown[1]);
                    armorEnchantments.put(Enchantment.getByName(enchantment), lvl);
                }
            }

            HashMap<Enchantment, Integer> weaponEnchantments = new HashMap<>();

            if (sBoss.getStringList("WeaponEnchantments") != null) {
                for (String en : sBoss.getStringList("WeaponEnchantments")) {
                    String[] breakdown = en.split(":");
                    String enchantment = breakdown[0];
                    int lvl = Integer.parseInt(breakdown[1]);
                    weaponEnchantments.put(Enchantment.getByName(enchantment), lvl);
                }
            }

            String armorType = sBoss.getString("ArmorType");
            String weaponType = sBoss.getString("WeaponType");

            if (armorType != null && weaponType != null) {
                ItemStack itemHand = Utils.makeItem(Material.matchMaterial(weaponType), name, 1, 0, new ArrayList<>(), weaponEnchantments);
                boss.getEquipment().setItemInHand(itemHand);
                boss.getEquipment().setArmorContents(Utils.makeArmor(armorType, armorEnchantments));
            }

            Collection<Entity> entities = boss.getLocation().getWorld().getNearbyEntities(boss.getLocation(), 30, 10, 30);
            for (Entity e : entities) {
                if (e instanceof Player) {
                    Player p = (Player) e;
                    p.sendMessage(fileManager.getMessage("boss_distancia_nasceu"));
                }
            }

            boss.setMetadata("BossName", new FixedMetadataValue(plugin, name));
            boss.setMetadata("BossTypeConfig", new FixedMetadataValue(plugin, key));
            boss.setCanPickupItems(false);
            initSpawnServos(player, boss, sBoss);
            initBossDelay(boss, sBoss);
            bossesVivos.put(boss.getUniqueId(), boss);
            plugin.getConfigurationManager().saveBosses(bossesVivos.keySet());
        }
    }

    private List<LivingEntity> spawnServos(Player player, LivingEntity boss, ConfigurationSection sServos) {
        List<LivingEntity> servos;
        if (mapServos.get(boss.getUniqueId()) != null)
            servos = mapServos.get(boss.getUniqueId());
        else
            servos = new ArrayList<>();
        servos.removeIf(Entity::isDead);

        int servosPorDelay = sServos.getInt("ServosPorDelay");
        int maxServos = sServos.getInt("MaxServos");

        List<String> entities = sServos.getStringList("Types");
        int entitiesSize = sServos.getStringList("Types").size();

        if (servos.size() < maxServos) {
            for (int i = 0; i < servosPorDelay; i++) {
                if (servos.size() == maxServos)
                    break;
                Location location = boss.getLocation();
                double x = Utils.randInt(-3, 3);
                double z = Utils.randInt(-3, 3);
                location = location.add(x, 1D, z);

                String e = entities.get(Utils.randInt(0, entitiesSize - 1));

                LivingEntity entity = Utils.getBossType(player, location, e);
                if (entity != null) {
                    List<String> metadataValues = new ArrayList<>();
                    if (boss.getMetadata("BossName") != null) {
                        List<MetadataValue> entityMetaData = boss.getMetadata("BossName");
                        for (MetadataValue m : entityMetaData) {
                            metadataValues.add(String.valueOf(m.value()));
                        }
                    }
                    String name = Utils.color(sServos.getString("Name"));
                    name = name.replace("{boss}", metadataValues.get(0));
                    double maxHealth = sServos.getDouble("Health");

                    entity.setCustomName(name);
                    entity.setMaxHealth(maxHealth);
                    entity.setHealth(maxHealth);

                    HashMap<Enchantment, Integer> armorEnchantments = new HashMap<>();

                    for (String en : sServos.getStringList("ArmorEnchantments")) {
                        String[] breakdown = en.split(":");
                        String enchantment = breakdown[0];
                        int lvl = Integer.parseInt(breakdown[1]);
                        armorEnchantments.put(Enchantment.getByName(enchantment), lvl);
                    }

                    HashMap<Enchantment, Integer> weaponEnchantments = new HashMap<>();

                    for (String en : sServos.getStringList("WeaponEnchantments")) {
                        String[] breakdown = en.split(":");
                        String enchantment = breakdown[0];
                        int lvl = Integer.parseInt(breakdown[1]);
                        weaponEnchantments.put(Enchantment.getByName(enchantment), lvl);
                    }

                    String armorType = sServos.getString("ArmorType");
                    String weaponType = sServos.getString("WeaponType");

                    ItemStack itemHand = Utils.makeItem(Material.matchMaterial(weaponType), name, 1, 0, new ArrayList<>(), weaponEnchantments);
                    entity.getEquipment().setItemInHand(itemHand);
                    entity.getEquipment().setArmorContents(Utils.makeArmor(armorType, armorEnchantments));
                    entity.setCanPickupItems(false);

                    servos.add(entity);
                }
            }
        }
        return servos;
    }

    public HashMap<UUID, LivingEntity> getBossesVivos() {
        return bossesVivos;
    }

    public void setBossesVivos(HashMap<UUID, LivingEntity> bossesVivos) {
        this.bossesVivos = bossesVivos;
    }

    public HashMap<UUID, List<LivingEntity>> getMapServos() {
        return mapServos;
    }

    public void setMapServos(HashMap<UUID, List<LivingEntity>> mapServos) {
        this.mapServos = mapServos;
    }

    public void removeMapServos(UUID uuidBoss) {
        if (this.mapServos.containsKey(uuidBoss)) {
            List<LivingEntity> entities = this.mapServos.get(uuidBoss);
            for (LivingEntity e : entities)
                e.setHealth(0);
            this.mapServos.remove(uuidBoss);
        }
    }

    public HashMap<UUID, Integer> getServosTasksId() {
        return servosTasksId;
    }

    public void setServosTasksId(HashMap<UUID, Integer> servosTasksId) {
        this.servosTasksId = servosTasksId;
    }

    public void cancelTasks(Set<UUID> uuidsBoss) {
        for (UUID uuidBoss : uuidsBoss) {
            if (this.servosTasksId.containsKey(uuidBoss)) {
                int taskid = this.servosTasksId.get(uuidBoss);
                Bukkit.getScheduler().cancelTask(taskid);
                this.servosTasksId.remove(uuidBoss);
            }
            if (this.bossesTasksId.containsKey(uuidBoss)) {
                int taskid = this.bossesTasksId.get(uuidBoss);
                Bukkit.getScheduler().cancelTask(taskid);
                this.bossesTasksId.remove(uuidBoss);
            }
        }
    }

    public HashMap<UUID, Integer> getBossesTasksId() {
        return bossesTasksId;
    }

    public void setBossesTasksId(HashMap<UUID, Integer> bossesTasksId) {
        this.bossesTasksId = bossesTasksId;
    }

    protected double distance(Location a, Location b) {
        return Math.sqrt(square(a.getX() - b.getX()) + square(a.getY() - b.getY()) + square(a.getZ() - b.getZ()));
    }

    protected double square(double x) {
        return x * x;
    }

    protected String addZero(long num) {
        if (num < 10)
            return "0" + num;
        else
            return String.valueOf(num);
    }
}

