package fr.segame.armesiaClearLag;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class ConfigManager {

    private final ArmesiaClearLag plugin;
    private FileConfiguration config;

    public ConfigManager(ArmesiaClearLag plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public int getInterval() {
        return config.getInt("interval");
    }

    public List<Integer> getWarnings() {
        return config.getIntegerList("warnings");
    }

    public String getPrefix() {
        return config.getString("messages.prefix");
    }

    public String getMessage(String path) {
        return config.getString("messages." + path);
    }

    public List<String> getItemBlacklist() {
        return config.getStringList("item-blacklist");
    }

    public boolean isWebhookEnabled() {
        return config.getBoolean("webhook.enabled");
    }

    public String getWebhookUrl() {
        return config.getString("webhook.url");
    }

    public String getWebhookJson() {
        try {
            File file = new File(plugin.getDataFolder(), "webhook.json");
            return Files.readString(file.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String format(String msg) {
        return msg.replace("&", "§");
    }
}