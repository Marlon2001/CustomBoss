package me.marlon.customboss.managers;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EspadasManager {

    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();

    public HashMap<String, HashMap<String, Object>> getEspadasGUI() {
        Set<String> espadasNames = fileManager.getConfig("Espadas.yml").getConfigurationSection("Espadas").getKeys(true);
        espadasNames.removeIf(i -> i.contains("."));

        HashMap<String, HashMap<String, Object>> espadasGUI = new HashMap<>();

        for (String espada : espadasNames) {
            ConfigurationSection sGui = fileManager.getConfig("Espadas.yml").getConfigurationSection("Espadas." + espada + ".GUI");

            HashMap<String, Object> espadaGUI = new HashMap<>();
            espadaGUI.put("ItemID", sGui.getString("ItemID").split(":"));

            espadaGUI.put("Slot", sGui.getInt("Slot"));
            espadaGUI.put("Name", Utils.color(sGui.getString("Name")));
            List<String> item_lore = sGui.getStringList("Lore");
            item_lore.replaceAll(Utils::color);
            espadaGUI.put("ItemLore", item_lore);

            espadasGUI.put(espada, espadaGUI);
        }

        return espadasGUI;
    }

    public HashMap<String, HashMap<String, Object>> getEspadas() {
        Set<String> espadasNames = fileManager.getConfig("Espadas.yml").getConfigurationSection("Espadas").getKeys(true);
        espadasNames.removeIf(i -> i.contains("."));

        HashMap<String, HashMap<String, Object>> espadasGUI = new HashMap<>();

        for (String espada : espadasNames) {
            ConfigurationSection sGui = fileManager.getConfig("Espadas.yml").getConfigurationSection("Espadas." + espada + ".Espada");

            HashMap<String, Object> espadaGUI = new HashMap<>();
            espadaGUI.put("ItemID", sGui.getString("ItemID").split(":"));
            espadaGUI.put("Name", Utils.color(sGui.getString("Name")));
            espadaGUI.put("Dano", sGui.getInt("Dano"));
            List<String> item_lore = sGui.getStringList("Lore");
            item_lore.replaceAll(Utils::color);
            espadaGUI.put("ItemLore", item_lore);

            espadasGUI.put(espada, espadaGUI);
        }

        return espadasGUI;
    }
}
