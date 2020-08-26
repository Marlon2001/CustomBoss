package me.marlon.customboss.commands;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.managers.FileManager;
import me.marlon.customboss.managers.GUIManager;
import me.marlon.customboss.model.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BossCommand implements Command {

    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();

    @Override
    public String getPermission() {
        return "boss.use";
    }

    @Override
    public void onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String... arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            GUIManager guiManager = new GUIManager();
            Inventory gui = guiManager.mCreateMainGui(player);
            player.openInventory(gui);
        }
    }

    @Override
    public String getDescription() {
        return fileManager.getMessage("cmd_boss");
    }
}
