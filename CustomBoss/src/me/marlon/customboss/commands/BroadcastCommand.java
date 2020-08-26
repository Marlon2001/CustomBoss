package me.marlon.customboss.commands;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;
import me.marlon.customboss.managers.FileManager;
import me.marlon.customboss.model.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BroadcastCommand implements Command {

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
        StringBuilder message;

        if (arguments.size() > 0) {
            message = new StringBuilder(fileManager.getMessageNoPrefix("prefix") + Utils.color("&a"));

            for (String arg : arguments) {
                message.append(Utils.color(arg)).append(" ");
            }

            plugin.getServer().broadcastMessage(message.toString());
        } else {
            player.sendMessage(fileManager.getMessage("cmd_broadcast"));
        }
    }

    @Override
    public String getDescription() {
        return fileManager.getMessage("cmd_broadcast");
    }
}
