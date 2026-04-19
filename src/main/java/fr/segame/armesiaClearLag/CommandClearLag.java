package fr.segame.armesiaClearLag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandClearLag implements CommandExecutor {

    private final ArmesiaClearLag plugin;

    public CommandClearLag(ArmesiaClearLag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        ConfigManager cfg = plugin.getConfigManager();
        String prefix = cfg.format(cfg.getPrefix());

        boolean canReload = sender.hasPermission("armesia.clearlag.reload");
        boolean canTime = sender.hasPermission("armesia.clearlag.time");

        // /clearlag
        if (args.length == 0) {

            if (!canReload && canTime) {
                int time = plugin.getClearTask().getTimeLeft();

                String msg = cfg.getMessage("time")
                        .replace("{time}", TimeUtil.formatTimeFull(time));

                sender.sendMessage(prefix + cfg.format(msg));
                return true;
            }

            if (canReload) {
                sender.sendMessage(prefix + cfg.format(cfg.getMessage("usage")));
                return true;
            }

            sender.sendMessage(prefix + cfg.format(cfg.getMessage("no-permission")));
            return true;
        }

        // /clearlag reload
        if (args[0].equalsIgnoreCase("reload")) {

            if (!canReload) {
                sender.sendMessage(prefix + cfg.format(cfg.getMessage("no-permission")));
                return true;
            }

            plugin.getConfigManager().reload();
            plugin.getClearTask().restart();

            sender.sendMessage(prefix + cfg.format(cfg.getMessage("reload")));
            return true;
        }

        // /clearlag time
        if (args[0].equalsIgnoreCase("time")) {

            if (!canTime) {
                sender.sendMessage(prefix + cfg.format(cfg.getMessage("no-permission")));
                return true;
            }

            int time = plugin.getClearTask().getTimeLeft();

            String msg = cfg.getMessage("time")
                    .replace("{time}", TimeUtil.formatTimeFull(time));

            sender.sendMessage(prefix + cfg.format(msg));
            return true;
        }

        if (canReload) {
            sender.sendMessage(prefix + cfg.format(cfg.getMessage("usage")));
        } else {
            sender.sendMessage(prefix + cfg.format(cfg.getMessage("no-permission")));
        }

        return true;
    }
}