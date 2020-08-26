package me.marlon.customboss.commands;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.managers.FileManager;
import me.marlon.customboss.model.Command;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GiveCommand implements Command {

    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();

    @Override
    public String getPermission() {
        return "boss.use";
    }

    @Override
    public void onCommand(CommandSender player, org.bukkit.command.Command command, String label, String... args) {
        List<String> arguments = new LinkedList<>(Arrays.asList(args));
        arguments.remove(0);

        if (arguments.size() == 2 || arguments.size() == 3) {
            Player p = plugin.getServer().getPlayerExact(arguments.get(0));
            String itemGive = arguments.get(1);
            int amount = 1;

            try {
                if (arguments.size() == 3)
                    amount = Integer.parseInt(arguments.get(2));
            } catch (NumberFormatException ignored) {
            }

            if (p != null) {
                HashMap<String, HashMap<String, Object>> bossesSpawnEgg = plugin.getBossesManager().getBossesSpawnEgg();
                HashMap<String, HashMap<String, Object>> espadas = plugin.getEspadasManager().getEspadas();

                if (bossesSpawnEgg.containsKey(itemGive)) {
                    HashMap<String, Object> bossSpawnEgg = bossesSpawnEgg.get(itemGive);

                    String[] itemID = (String[]) bossSpawnEgg.get("ItemID");
                    String name = (String) bossSpawnEgg.get("Name");
                    List<String> item_lore = (List<String>) bossSpawnEgg.get("ItemLore");
                    ItemStack item;

                    Material material = Material.matchMaterial(itemID[0]);
                    if (itemID.length == 2) {
                        item = new ItemStack(material, amount, (byte) Integer.parseInt(itemID[1]));
                    } else {
                        item = new ItemStack(material, amount);
                    }

                    ItemMeta item_meta = item.getItemMeta();
                    item_meta.setDisplayName(name);
                    item_meta.setLore(item_lore);
                    item.setItemMeta(item_meta);

                    p.getInventory().addItem(item);
                } else if (espadas.containsKey(itemGive)) {
                    HashMap<String, Object> espada = espadas.get(itemGive);

                    String[] itemID = (String[]) espada.get("ItemID");
                    String name = (String) espada.get("Name");
                    List<String> item_lore = (List<String>) espada.get("ItemLore");
                    ItemStack item;

                    Material material = Material.matchMaterial(itemID[0]);
                    if (itemID.length == 2) {
                        item = new ItemStack(material, amount, (byte) Integer.parseInt(itemID[1]));
                    } else {
                        item = new ItemStack(material, amount);
                    }

                    ItemMeta item_meta = item.getItemMeta();
                    item_meta.setDisplayName(name);
                    item_meta.setLore(item_lore);
                    item_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                    item.setItemMeta(item_meta);
                    item.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);

                    p.getInventory().addItem(item);
                } else {
                    player.sendMessage(fileManager.getMessage("item_inexistente").replace("{item}", arguments.get(1)));
                }
            } else {
                player.sendMessage(fileManager.getMessage("jogador_inexistente").replace("{player}", arguments.get(0)));
            }
        } else {
            player.sendMessage(fileManager.getMessage("cmd_give"));
        }
    }

    @Override
    public String getDescription() {
        return fileManager.getMessage("cmd_give");
    }
}
