package me.marlon.customboss.managers;

import me.marlon.customboss.CustomBoss;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class GUIManager {

    private final CustomBoss plugin = CustomBoss.getInstance();

    public Inventory mCreateMainGui(Player player) {
        String inventoryName = plugin.getConfigurationManager().getInventoryNameMainGUI();
        String inventoryBossName = plugin.getConfigurationManager().getBookNameBoss();
        String inventoryEspadasName = plugin.getConfigurationManager().getBookNameEspadas();
        List<String> inventoryBookLoreBoss = plugin.getConfigurationManager().getBookLoreBoss();
        List<String> inventoryBookLoreEspadas = plugin.getConfigurationManager().getBookLoreEspadas();

        int slots = plugin.getConfigurationManager().getSlotsMainGUI();
        Inventory gui = Bukkit.createInventory(player, slots, inventoryName);

        ItemStack bookBoss = new ItemStack(Material.BOOK);
        ItemStack bookEspadas = new ItemStack(Material.BOOK);

        ItemMeta bookBoss_meta = bookBoss.getItemMeta();
        bookBoss_meta.setDisplayName(inventoryBossName);
        bookBoss_meta.setLore(inventoryBookLoreBoss);
        bookBoss.setItemMeta(bookBoss_meta);

        ItemMeta bookEspadas_meta = bookEspadas.getItemMeta();
        bookEspadas_meta.setDisplayName(inventoryEspadasName);
        bookEspadas_meta.setLore(inventoryBookLoreEspadas);
        bookEspadas.setItemMeta(bookEspadas_meta);
        
        int cont = 0;
        if (slots >= 27) {
            for (int i = 0; i < 9; i++) {
                ItemStack painel_vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
                ItemMeta painel_vidro_meta = painel_vidro.getItemMeta();
                painel_vidro_meta.setDisplayName("§0");
                painel_vidro.setItemMeta(painel_vidro_meta);

                gui.setItem(i, painel_vidro);
                cont = i;
            }

            for (int i = slots - 9; i < slots; i++) {
                ItemStack painel_vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
                ItemMeta painel_vidro_meta = painel_vidro.getItemMeta();
                painel_vidro_meta.setDisplayName("§0");
                painel_vidro.setItemMeta(painel_vidro_meta);

                gui.setItem(i, painel_vidro);
            }

            if (cont != 0) {
                cont++;
            }
            gui.setItem(cont+3, bookBoss);
            cont += 1;
            gui.setItem(cont+4, bookEspadas);
        } else {
            gui.addItem(bookBoss);
            gui.addItem(bookEspadas);
        }
        return gui;
    }

    public Inventory mCreateBossGui(Player player) {
        String inventoryName = plugin.getConfigurationManager().getInventoryNameBossGUI();
        int slots = plugin.getConfigurationManager().getSlotsBossGUI();

        Inventory gui = Bukkit.createInventory(player, slots, inventoryName);
        HashMap<String, HashMap<String, Object>> bossesGui = plugin.getBossesManager().getBossesGUI();

        for (String key : bossesGui.keySet()) {
            HashMap<String, Object> bossGui = bossesGui.get(key);

            String[] itemID = (String[]) bossGui.get("ItemID");
            int slot = (int) bossGui.get("Slot");
            String name = (String) bossGui.get("Name");
            List<String> item_lore = (List<String>) bossGui.get("ItemLore");

            ItemStack item;
            Material material = Material.matchMaterial(itemID[0]);
            if (itemID.length == 2)
                item = new ItemStack(material, 1, (byte) Integer.parseInt(itemID[1]));
            else
                item = new ItemStack(material, 1);

            ItemMeta item_meta = item.getItemMeta();
            item_meta.setDisplayName(name);
            item_meta.setLore(item_lore);
            item.setItemMeta(item_meta);

            gui.setItem(slot, item);
        }

        return gui;
    }

    public Inventory mCreateEspadasGUI(Player player) {
        String inventoryName = plugin.getConfigurationManager().getInventoryNameEspadasGUI();
        int slots = plugin.getConfigurationManager().getSlotsEspadasGUI();

        Inventory gui = Bukkit.createInventory(player, slots, inventoryName);
        HashMap<String, HashMap<String, Object>> espadasGUI = plugin.getEspadasManager().getEspadasGUI();

        for (String key : espadasGUI.keySet()) {
            HashMap<String, Object> espadaGUI = espadasGUI.get(key);

            String[] itemID = (String[]) espadaGUI.get("ItemID");
            int slot = (int) espadaGUI.get("Slot");
            String name = (String) espadaGUI.get("Name");
            List<String> item_lore = (List<String>) espadaGUI.get("ItemLore");

            ItemStack item;
            Material material = Material.matchMaterial(itemID[0]);
            if (itemID.length == 2)
                item = new ItemStack(material, 1, (byte) Integer.parseInt(itemID[1]));
            else
                item = new ItemStack(material, 1);

            item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            ItemMeta item_meta = item.getItemMeta();
            item_meta.setDisplayName(name);
            item_meta.setLore(item_lore);
            item_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(item_meta);

            gui.setItem(slot, item);
        }
        return gui;
    }
}
