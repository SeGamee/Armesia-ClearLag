package fr.segame.armesiaClearLag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ClearTask {

    private final ArmesiaClearLag plugin;
    private BukkitRunnable task;
    private int timer;

    public ClearTask(ArmesiaClearLag plugin) {
        this.plugin = plugin;
        this.timer = plugin.getConfigManager().getInterval();
    }

    /**
     * Effectue un clear lag instantané et envoie le message de broadcast.
     */
    public void clearNow() {
        ConfigManager cfg = plugin.getConfigManager();
        String prefix = cfg.format(cfg.getPrefix());
        ClearResult result = clearEntities();
        String msg = cfg.getMessage("cleared")
                .replace("{items}", String.valueOf(result.items()))
                .replace("{entities}", String.valueOf(result.entities()));
        Bukkit.broadcastMessage(prefix + cfg.format(msg));
    }

    public void start() {
        task = new BukkitRunnable() {
            @Override
            public void run() {

                ConfigManager cfg = plugin.getConfigManager();
                String prefix = cfg.format(cfg.getPrefix());

                if (timer <= 0) {
                    ClearResult result = clearEntities();

                    String msg = cfg.getMessage("cleared")
                            .replace("{items}", String.valueOf(result.items()))
                            .replace("{entities}", String.valueOf(result.entities()));

                    Bukkit.broadcastMessage(prefix + cfg.format(msg));

                    timer = cfg.getInterval();
                    return;
                }

                List<Integer> warnings = cfg.getWarnings();

                if (warnings.contains(timer)) {
                    String msg = cfg.getMessage("warning")
                            .replace("{time}", TimeUtil.formatTimeFull(timer));

                    Bukkit.broadcastMessage(prefix + cfg.format(msg));
                }

                timer--;
            }
        };

        task.runTaskTimer(plugin, 20L, 20L);
    }

    public void stop() {
        if (task != null) task.cancel();
    }

    public void restart() {
        stop();
        timer = plugin.getConfigManager().getInterval();
        start();
    }

    public int getTimeLeft() {
        return timer;
    }

    private ClearResult clearEntities() {
        int itemsCount = 0;
        int entityCount = 0;

        var cfg = plugin.getConfigManager();
        var blacklist = cfg.getItemBlacklist();

        for (var world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {

                if (entity instanceof Item item) {

                    String material = item.getItemStack().getType().name();

                    if (blacklist.contains(material)) continue;

                    int amount = item.getItemStack().getAmount();

                    item.remove();

                    itemsCount += amount;
                    entityCount++;
                    continue;
                }

                if (entity instanceof org.bukkit.entity.ExperienceOrb orb) {
                    orb.remove();
                }
            }
        }

        // 🔥 WEBHOOK
        if (cfg.isWebhookEnabled()) {

            String rawJson = cfg.getWebhookJson();

            if (rawJson != null) {

                String finalJson = rawJson
                        .replace("{items}", String.valueOf(itemsCount))
                        .replace("{entities}", String.valueOf(entityCount));

                String url = cfg.getWebhookUrl();

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    DiscordWebhook.sendRaw(url, finalJson);
                });
            }
        }

        return new ClearResult(itemsCount, entityCount);
    }
}