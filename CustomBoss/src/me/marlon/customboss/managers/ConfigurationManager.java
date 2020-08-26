package me.marlon.customboss.managers;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;

import java.util.*;

public class ConfigurationManager {

    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();

    private final boolean isPrefixMessage;
    private final String inventoryNameMainGUI;
    private final String inventoryNameBossGUI;
    private final String inventoryNameEspadasGUI;
    private final int slotsMainGUI;
    private final int slotsBossGUI;
    private final int slotsEspadasGUI;

    private final int mobBarStyle;

    private final String bookNameBoss;
    private final List<String> bookLoreBoss;
    private final String bookNameEspadas;
    private final List<String> bookLoreEspadas;
    private final int danoPadrao;
    private final boolean killall;

    public ConfigurationManager() {
        this.isPrefixMessage = fileManager.getConfig("Config.yml").getBoolean("PrefixMessage");
        this.inventoryNameMainGUI = Utils.color(fileManager.getConfig("Config.yml").getString("Settings.InventoryName"));
        this.inventoryNameBossGUI = Utils.color(fileManager.getConfig("Boss.yml").getString("Settings.InventoryName"));
        this.inventoryNameEspadasGUI = Utils.color(fileManager.getConfig("Espadas.yml").getString("Settings.InventoryName"));
        this.slotsMainGUI = fileManager.getConfig("Config.yml").getInt("Settings.Slots");
        this.slotsBossGUI = fileManager.getConfig("Boss.yml").getInt("Settings.Slots");
        this.slotsEspadasGUI = fileManager.getConfig("Espadas.yml").getInt("Settings.Slots");

        this.mobBarStyle = fileManager.getConfig("Boss.yml").getInt("Settings.MobBarStyle");

        this.bookNameBoss = Utils.color(fileManager.getConfig("Boss.yml").getString("Settings.BookName"));
        this.bookLoreBoss = new ArrayList<>();
        List<String> bookLoresB = fileManager.getConfig("Boss.yml").getStringList("Settings.BookLore");
        for(String b : bookLoresB)
            this.bookLoreBoss.add(Utils.color(b));
        this.bookNameEspadas = Utils.color(fileManager.getConfig("Espadas.yml").getString("Settings.BookName"));
        this.bookLoreEspadas = new ArrayList<>();
        List<String> bookLoresE = fileManager.getConfig("Espadas.yml").getStringList("Settings.BookLore");
        for(String e : bookLoresE)
            this.bookLoreEspadas.add(Utils.color(e));
        this.danoPadrao = fileManager.getConfig("Espadas.yml").getInt("Settings.DanoPadrao");

        this.killall = fileManager.getConfig("Config.yml").getBoolean("Settings.KillAll");
    }

    public void saveBosses(Set<UUID> uuids) {
        try {
            fileManager.getConfig("Boss.yml").set("BossesVivos", Arrays.toString(uuids.toArray()));
            fileManager.saveConfig("Boss.yml");
        } catch (Exception ignored) {
        }
    }

    public ArrayList<UUID> getBosses() {
        ArrayList<UUID> uuidsList = new ArrayList<>();

        if (fileManager.getConfig("Boss.yml").get("BossesVivos") != null) {
            String uuids1 = (String) fileManager.getConfig("Boss.yml").get("BossesVivos");

            if(uuids1.equals("[]"))
                return uuidsList;

            uuids1 = uuids1.replace("[", "");
            uuids1 = uuids1.replace("]", "");

            if(uuids1.contains(",")){
                String[] uuids2 = uuids1.split(",");
                for (String u : uuids2) {
                    if (!u.trim().isEmpty()) {
                        uuidsList.add(UUID.fromString(u.trim()));
                    }
                }
            } else {
                if (!uuids1.trim().isEmpty()) {
                    uuidsList.add(UUID.fromString(uuids1.trim()));
                }
            }
        }

        return uuidsList;
    }

    public boolean isPrefixMessage() {
        return isPrefixMessage;
    }

    public String getInventoryNameMainGUI() {
        return inventoryNameMainGUI;
    }

    public String getInventoryNameBossGUI() {
        return inventoryNameBossGUI;
    }

    public String getInventoryNameEspadasGUI() {
        return inventoryNameEspadasGUI;
    }

    public int getSlotsMainGUI() {
        return slotsMainGUI;
    }

    public int getSlotsBossGUI() {
        return slotsBossGUI;
    }

    public int getSlotsEspadasGUI() {
        return slotsEspadasGUI;
    }

    public int getMobBarStyle() {
        return mobBarStyle;
    }

    public String getBookNameBoss() {
        return bookNameBoss;
    }

    public List<String> getBookLoreBoss() {
        bookLoreBoss.replaceAll(Utils::color);
        return bookLoreBoss;
    }

    public String getBookNameEspadas() {
        return bookNameEspadas;
    }

    public List<String> getBookLoreEspadas() {
        return bookLoreEspadas;
    }

    public int getDanoPadrao() {
        return danoPadrao;
    }

    public boolean isKillall() {
        return killall;
    }

    public List<String> inventorysNames() {
        List<String> inventorys = new ArrayList<>();

        inventorys.add(getInventoryNameMainGUI());
        inventorys.add(getInventoryNameBossGUI());
        inventorys.add(getInventoryNameEspadasGUI());
        return inventorys;
    }
}
