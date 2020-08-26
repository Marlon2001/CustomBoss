package me.marlon.customboss.model;

import org.bukkit.command.CommandSender;

public interface Command {
    String getPermission();

    void onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String... arguments);

    String getDescription();
}
