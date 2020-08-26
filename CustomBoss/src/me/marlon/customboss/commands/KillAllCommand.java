package me.marlon.customboss.commands;

import me.marlon.customboss.CustomBoss;
import me.marlon.customboss.Utils.Utils;
import me.marlon.customboss.managers.FileManager;
import me.marlon.customboss.model.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class KillAllCommand implements Command {

    private final CustomBoss plugin = CustomBoss.getInstance();
    private final FileManager fileManager = plugin.getFileManager();

    @Override
    public String getPermission() {
        return "boss.use";
    }

    @Override
    public void onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String... arguments) {
        HashMap<UUID, LivingEntity> bossesVivos = plugin.getBossesManager().getBossesVivos();
        HashMap<UUID, List<LivingEntity>> servos = plugin.getBossesManager().getMapServos();
        bossesVivos.values().forEach(i -> i.setHealth(0));
        Utils.killAllServos(servos);

        int qtdBosses = bossesVivos.size();
        commandSender.sendMessage(fileManager.getMessage("killall").replace("{bosses}", String.valueOf(qtdBosses)));
        plugin.getBossesManager().cancelTasks(bossesVivos.keySet());
        plugin.getBossesManager().setServosTasksId(new HashMap<>());
        plugin.getBossesManager().setBossesTasksId(new HashMap<>());
        plugin.getBossesManager().setMapServos(new HashMap<>());
        plugin.getBossesManager().setBossesVivos(new HashMap<>());
    }

    @Override
    public String getDescription() {
        return fileManager.getMessage("cmd_killall");
    }
}
