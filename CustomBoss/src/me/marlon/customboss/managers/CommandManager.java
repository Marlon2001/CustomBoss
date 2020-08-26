package me.marlon.customboss.managers;

import me.marlon.customboss.CustomBoss;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final Map<String, me.marlon.customboss.model.Command> commands = new LinkedHashMap<>();
    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length <= 0) {
                this.commands.get("boss").onCommand(player, command, s, strings);
            } else {
                String commandName = strings[0];
                if (this.commands.containsKey(commandName)) {
                    me.marlon.customboss.model.Command commandBoss = this.commands.get(commandName);
                    if (commandBoss.getPermission() == null || player.hasPermission(commandBoss.getPermission())) {
                        commandBoss.onCommand(player, command, s, strings);
                    } else {
                        player.sendMessage(fileManager.getMessage("sem_permissao"));
                    }
                } else {
                    player.sendMessage(fileManager.getMessage("comando_desconhecido"));
                }
            }
        } else {
            String commandName;
            if(strings.length > 0) {
                commandName = strings[0];
                if (this.commands.containsKey(commandName)) {
                    me.marlon.customboss.model.Command commandBoss = this.commands.get(commandName);
                    commandBoss.onCommand(commandSender, command, s, strings);
                } else {
                    commandSender.sendMessage(fileManager.getMessage("comando_desconhecido"));
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> stringList = new ArrayList<>();

        if (strings.length == 1) {
            for (String string : this.commands.keySet()) {
                if (string.equals("boss"))
                    continue;
                me.marlon.customboss.model.Command commandBoss = this.commands.get(string);
                if (commandBoss.getPermission() == null || commandSender.hasPermission(commandBoss.getPermission())) {
                    if (string.startsWith(strings[0])) {
                        stringList.add(string);
                    }
                }
            }
            return stringList;
        }

        if (strings[0].toLowerCase().equals("give")) {
            if (strings.length == 2) {
                List<Player> players = (List<Player>) plugin.getServer().getOnlinePlayers();

                for (Player p : players) {
                    if (commandSender.getName().equals(p.getName()))
                        continue;
                    stringList.add(p.getName());
                }
                return null;
            } else if (strings.length == 3) {
                Set<String> bossNames = fileManager.getConfig("Boss.yml").getConfigurationSection("Bosses").getKeys(true);
                bossNames.removeIf(i -> i.contains("."));
                stringList.addAll(bossNames);

                Set<String> espadasNames = fileManager.getConfig("Espadas.yml").getConfigurationSection("Espadas").getKeys(true);
                espadasNames.removeIf(i -> i.contains("."));
                stringList.addAll(espadasNames);

                return stringList;
            } else {
                return stringList;
            }
        }
        return null;
    }

    public Map<String, me.marlon.customboss.model.Command> getCommands() {
        return commands;
    }

    public void registerCommand(String name, me.marlon.customboss.model.Command command) {
        this.commands.put(name, command);
    }

    public void unregisterCommand(String name) {
        this.commands.remove(name);
    }
}
