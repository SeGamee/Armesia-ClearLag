package fr.segame.armesiaClearLag;

import org.bukkit.plugin.java.JavaPlugin;

public class ArmesiaClearLag extends JavaPlugin {

    private static ArmesiaClearLag instance;
    private ConfigManager configManager;
    private ClearTask clearTask;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("webhook.json", false); // 🔥 important

        configManager = new ConfigManager(this);

        clearTask = new ClearTask(this);
        clearTask.start();

        getCommand("clearlag").setExecutor(new CommandClearLag(this));
    }

    public static ArmesiaClearLag getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ClearTask getClearTask() {
        return clearTask;
    }
}