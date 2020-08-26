package me.marlon.customboss.events;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;
import me.marlon.customboss.managers.GUIManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryClick implements Listener {

    private final CustomBoss plugin = CustomBoss.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void inventoryClickEvent(InventoryClickEvent event) {
        String inventoryMain = plugin.getConfigurationManager().getInventoryNameMainGUI();
        String inventoryBoss = plugin.getConfigurationManager().getInventoryNameBossGUI();
        String inventoryEspadas = plugin.getConfigurationManager().getInventoryNameEspadasGUI();

        String inventoryBookBossName = plugin.getConfigurationManager().getBookNameBoss();
        String inventoryBookEspadasName = plugin.getConfigurationManager().getBookNameEspadas();

        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();

            if(event.getClickedInventory() != null) {
                if (inventorysNames().contains(event.getClickedInventory().getTitle()) && event.getClick() != ClickType.LEFT) {
                    event.setCancelled(true);
                } else if (inventorysNames().contains(event.getClickedInventory().getTitle()) && player.getItemOnCursor().getType() != Material.AIR) {
                    event.setCancelled(true);
                } else if (event.getClickedInventory().getTitle().equalsIgnoreCase(inventoryMain) && event.getClick() == ClickType.LEFT && event.getCurrentItem() != null) {
                    GUIManager guiManager = new GUIManager();
                    String itemClickedName = "";

                    if (event.getCurrentItem().hasItemMeta())
                        if (event.getCurrentItem().getItemMeta().hasDisplayName())
                            itemClickedName = event.getCurrentItem().getItemMeta().getDisplayName();

                    if (itemClickedName.equalsIgnoreCase(inventoryBookBossName)) {
                        player.closeInventory();
                        Inventory gui = guiManager.mCreateBossGui(player);
                        player.openInventory(gui);
                    } else if (itemClickedName.equalsIgnoreCase(inventoryBookEspadasName)) {
                        player.closeInventory();
                        Inventory gui = guiManager.mCreateEspadasGUI(player);
                        player.openInventory(gui);
                    }
                    event.setCancelled(true);
                } else if (event.getClickedInventory().getTitle().equalsIgnoreCase(inventoryBoss) && event.getClick() == ClickType.LEFT && event.getCurrentItem() != null) {
                    HashMap<String, HashMap<String, Object>> bossesGUI = plugin.getBossesManager().getBossesGUI();
                    String itemClickedName = "";
                    List<String> itemClickedLore = new ArrayList<>();

                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().hasDisplayName())
                            itemClickedName = event.getCurrentItem().getItemMeta().getDisplayName();
                        if (event.getCurrentItem().getItemMeta().hasLore())
                            itemClickedLore = event.getCurrentItem().getItemMeta().getLore();
                    }

                    for (String key : bossesGUI.keySet()) {
                        HashMap<String, Object> bossGUI = bossesGUI.get(key);

                        List<String> item_lore_gui = (List<String>) bossGUI.get("ItemLore");
                        String item_name_gui = Utils.color((String) bossGUI.get("Name"));

                        if (itemClickedName.equalsIgnoreCase(item_name_gui)) {
                            if (itemClickedLore.equals(item_lore_gui)) {
                                HashMap<String, Object> bossSpawnEgg = plugin.getBossesManager().getBossesSpawnEgg().get(key);
                                player.closeInventory();

                                String[] itemID = (String[]) bossSpawnEgg.get("ItemID");
                                String name = (String) bossSpawnEgg.get("Name");
                                List<String> item_lore = (List<String>) bossSpawnEgg.get("ItemLore");
                                ItemStack item;

                                Material material = Material.matchMaterial(itemID[0]);
                                if (itemID.length == 2) {
                                    item = new ItemStack(material, 1, (byte) Integer.parseInt(itemID[1]));
                                } else {
                                    item = new ItemStack(material, 1);
                                }

                                ItemMeta item_meta = item.getItemMeta();
                                item_meta.setDisplayName(name);
                                item_meta.setLore(item_lore);
                                item.setItemMeta(item_meta);
                                player.getInventory().addItem(item);
                                break;
                            }
                        }
                    }
                    event.setCancelled(true);
                } else if (event.getClickedInventory().getTitle().equalsIgnoreCase(inventoryEspadas) && event.getClick() == ClickType.LEFT && event.getCurrentItem() != null) {
                    HashMap<String, HashMap<String, Object>> espadasGUI = plugin.getEspadasManager().getEspadasGUI();

                    String itemClickedName = "";
                    List<String> itemClickedLore = new ArrayList<>();

                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().hasDisplayName())
                            itemClickedName = event.getCurrentItem().getItemMeta().getDisplayName();
                        if (event.getCurrentItem().getItemMeta().hasLore())
                            itemClickedLore = event.getCurrentItem().getItemMeta().getLore();
                    }

                    for (String key : espadasGUI.keySet()) {
                        HashMap<String, Object> espadaGUI = espadasGUI.get(key);

                        String item_name_gui = Utils.color((String) espadaGUI.get("Name"));
                        List<String> item_lore_gui = (List<String>) espadaGUI.get("ItemLore");

                        if (itemClickedName.equalsIgnoreCase(item_name_gui)) {
                            if (itemClickedLore.equals(item_lore_gui)) {
                                HashMap<String, Object> espada = plugin.getEspadasManager().getEspadas().get(key);
                                player.closeInventory();

                                String[] itemID = (String[]) espada.get("ItemID");
                                String name = (String) espada.get("Name");
                                List<String> item_lore = (List<String>) espada.get("ItemLore");
                                ItemStack item;

                                Material material = Material.matchMaterial(itemID[0]);
                                if (itemID.length == 2) {
                                    item = new ItemStack(material, 1, (byte) Integer.parseInt(itemID[1]));
                                } else {
                                    item = new ItemStack(material, 1);
                                }

                                ItemMeta item_meta = item.getItemMeta();
                                item_meta.setDisplayName(name);
                                item_meta.setLore(item_lore);
                                item_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                                item.setItemMeta(item_meta);
                                item.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);

                                player.getInventory().addItem(item);
                                break;
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    protected List<String> inventorysNames() {
        List<String> inventorys = new ArrayList<>();

        inventorys.add(plugin.getConfigurationManager().getInventoryNameMainGUI());
        inventorys.add(plugin.getConfigurationManager().getInventoryNameBossGUI());
        inventorys.add(plugin.getConfigurationManager().getInventoryNameEspadasGUI());
        return inventorys;
    }
}