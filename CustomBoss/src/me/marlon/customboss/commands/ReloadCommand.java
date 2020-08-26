package me.marlon.customboss.commands;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.managers.ConfigurationManager;
import me.marlon.customboss.managers.FileManager;
import me.marlon.customboss.model.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements Command {

    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();

    @Override
    public String getPermission() {
        return "boss.use";
    }

    @Override
    public void onCommand(CommandSender player, org.bukkit.command.Command command, String label, String... arguments) {
        try {
            plugin.setConfigurationManager(new ConfigurationManager());
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (plugin.getConfigurationManager().inventorysNames().contains(p.getOpenInventory().getTitle())) {
                    p.getOpenInventory().close();
                }
            }
            fileManager.reloadConfig("Config.yml", "Espadas.yml", "Boss.yml");
            player.sendMessage(fileManager.getMessage("reload"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return fileManager.getMessage("cmd_reload");
    }
}
