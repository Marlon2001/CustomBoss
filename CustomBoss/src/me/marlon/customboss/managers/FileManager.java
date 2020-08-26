package me.marlon.customboss.managers;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginAwareness;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Level;

public class FileManager {

    private final CustomBoss plugin = CustomBoss.getInstance();
    private HashMap<String, FileConfiguration> newConfigs = new HashMap<>();

    public static void saveDefaultConfig(File dataFolder, String... names) {
        boolean folderExists = false;
        if (!dataFolder.exists()) {
            if (dataFolder.mkdir()) {
                folderExists = true;
            }
        } else {
            folderExists = true;
        }

        if (folderExists) {
            for (String name : names) {
                File actual = new File(dataFolder, name);

                if (!actual.exists()) {
                    InputStream input = CustomBoss.class.getResourceAsStream("/resources/" + name);

                    if (input != null) {
                        FileOutputStream output = null;

                        try {
                            if (actual.createNewFile()) {
                                output = new FileOutputStream(actual);
                                byte[] buf = new byte[8192];

                                int length;
                                while ((length = input.read(buf)) > 0) {
                                    output.write(buf, 0, length);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                input.close();
                            } catch (Exception ignored) {
                            }

                            try {
                                if (output != null) {
                                    output.close();
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
        }
    }

    public String getMessage(String key) {
        ConfigurationSection section = this.getConfig("Config.yml").getConfigurationSection("Mensagens");

        if (plugin.getConfigurationManager().isPrefixMessage())
            return Utils.color(section.getString("prefix")) + Utils.color(section.getString(key));
        else
            return Utils.color(section.getString(key));
    }

    public String getMessageNoPrefix(String key) {
        ConfigurationSection section = this.getConfig("Config.yml").getConfigurationSection("Mensagens");

        return Utils.color(section.getString(key));
    }

    public FileConfiguration getConfig(String config) {
        if (newConfigs.get(config) == null) {
            this.reloadConfig(config);
        }
        return newConfigs.get(config);
    }

    public void saveConfig(String config) {
        File configFile = new File(plugin.getDataFolder(), config);

        try {
            this.getConfig(config).save(configFile);
            reloadConfig(config);
        } catch (IOException var2) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, var2);
        }
    }

    public void reloadConfig(String... configs) {
        HashMap<String, FileConfiguration> newConfigs = new HashMap<>();

        for (String config : configs) {
            File configFile = new File(plugin.getDataFolder(), config);
            newConfigs.put(config, YamlConfiguration.loadConfiguration(configFile));
            InputStream defConfigStream = plugin.getResource(config);

            if (defConfigStream != null) {
                YamlConfiguration defConfig;
                if (!this.isStrictlyUTF8() && !FileConfiguration.UTF8_OVERRIDE) {
                    defConfig = new YamlConfiguration();

                    byte[] contents;
                    try {
                        contents = ByteStreams.toByteArray(defConfigStream);
                    } catch (IOException var7) {
                        plugin.getLogger().log(Level.SEVERE, "Unexpected failure reading " + config, var7);
                        return;
                    }

                    String text = new String(contents, Charset.defaultCharset());
                    if (!text.equals(new String(contents, Charsets.UTF_8))) {
                        plugin.getLogger().warning("Default system encoding may have misread " + config + " from plugin jar");
                    }

                    try {
                        defConfig.loadFromString(text);
                    } catch (InvalidConfigurationException var6) {
                        plugin.getLogger().log(Level.SEVERE, "Cannot load configuration from jar", var6);
                    }
                } else {
                    defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
                }
                newConfigs.put(config, defConfig);
            }
        }

        this.newConfigs = newConfigs;
    }

    private boolean isStrictlyUTF8() {
        return plugin.getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8);
    }
}
