package me.marlon.customboss;

import me.marlon.customboss.Utils.Utils;
import me.marlon.customboss.commands.*;
import me.marlon.customboss.events.*;
import me.marlon.customboss.managers.*;
import net.minecraft.server.v1_8_R3.EntityFlying;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public class CustomBoss extends JavaPlugin {

    private static CustomBoss instance;
    private ConfigurationManager configurationManager;
    private CommandManager commandManager;
    private BossesManager bossesManager;
    private EspadasManager espadasManager;
    private FileManager fileManager;
    private MobHealthBar mobHealthBar;

    public static CustomBoss getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        FileManager.saveDefaultConfig(getDataFolder(), "Config.yml", "Boss.yml", "Espadas.yml");
        this.fileManager = new FileManager();
        this.configurationManager = new ConfigurationManager();
        this.bossesManager = new BossesManager();
        this.commandManager = new CommandManager();
        this.espadasManager = new EspadasManager();
        this.mobHealthBar = new MobHealthBar();

        final PluginManager pluginManager = getServer().getPluginManager();
        this.commandManager.registerCommand("boss", new BossCommand());
        this.commandManager.registerCommand("killall", new KillAllCommand());
        this.commandManager.registerCommand("give", new GiveCommand());
        this.commandManager.registerCommand("bc", new BroadcastCommand());
        this.commandManager.registerCommand("help", new HelpCommand());
        this.commandManager.registerCommand("reload", new ReloadCommand());

        this.getCommand("boss").setExecutor(this.commandManager);
        this.getCommand("boss").setTabCompleter(this.commandManager);

        pluginManager.registerEvents(new InventoryClick(), instance);
        pluginManager.registerEvents(new PlayerInteract(), instance);
        pluginManager.registerEvents(new PlayerInteractEntity(), instance);
        pluginManager.registerEvents(new ChunkUnload(), instance);

        pluginManager.registerEvents(new EntityTeleport(), instance);
        pluginManager.registerEvents(new EntityRegainHealth(), instance);
        pluginManager.registerEvents(new EntityTarget(), instance);
        pluginManager.registerEvents(new EntityDamage(), instance);
        pluginManager.registerEvents(new EntityDamageByEntity(), instance);
        pluginManager.registerEvents(new EntityDeath(), instance);
        pluginManager.registerEvents(new WitherEvents(), instance);

        this.getLogger().info("Habilitando " + this.getDescription().getName() + " carregado na versão v" + this.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
        for (Player p : this.getServer().getOnlinePlayers()) {
            if (this.getConfigurationManager().inventorysNames().contains(p.getOpenInventory().getTitle())) {
                p.getOpenInventory().close();
            }
        }

        if (configurationManager.isKillall()) {
            bossesManager.getBossesVivos().values().forEach(i -> i.setHealth(0));
            configurationManager.saveBosses(new HashSet<>());
        } else {
            configurationManager.saveBosses(bossesManager.getBossesVivos().keySet());
        }
        Utils.killAllServos(bossesManager.getMapServos());
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public BossesManager getBossesManager() {
        return bossesManager;
    }

    public EspadasManager getEspadasManager() {
        return espadasManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public MobHealthBar getMobHealthBar() {
        return mobHealthBar;
    }
}
