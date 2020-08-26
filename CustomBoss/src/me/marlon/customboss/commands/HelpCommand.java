package me.marlon.customboss.commands;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.managers.FileManager;
import me.marlon.customboss.model.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements Command {

    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();

    @Override
    public String getPermission() {
        return "boss.use";
    }

    @Override
    public void onCommand(CommandSender player, org.bukkit.command.Command command, String label, String... arguments) {
        for (Command comando : CustomBoss.getInstance().getCommandManager().getCommands().values()) {
            if (comando.getPermission() == null || player.hasPermission(comando.getPermission())) {
                player.sendMessage(comando.getDescription());
            }
        }
    }

    @Override
    public String getDescription() {
        return fileManager.getMessage("cmd_help");
    }
}
