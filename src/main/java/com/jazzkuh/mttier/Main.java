package com.jazzkuh.mttier;

import com.jazzkuh.mttier.commands.TierCMD;
import com.jazzkuh.mttier.data.DataManager;
import com.jazzkuh.mttier.data.StormHikari;
import com.jazzkuh.mttier.data.configuration.DefaultConfig;
import com.jazzkuh.mttier.player.listeners.PlayerChangeLevelListener;
import com.jazzkuh.mttier.player.listeners.PlayerJoinListener;
import com.jazzkuh.mttier.utils.PlaceholderUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static @Getter @Setter(AccessLevel.PRIVATE) Main instance;

    @Override
    public void onEnable() {
        setInstance(this);
        DefaultConfig.init();

        this.saveConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            new PlaceholderUtil().register();
        }

        new DataManager().initialize();

        new TierCMD().register(this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        if (DefaultConfig.AUTO_UPDATE_TIER.asBoolean()) {
            Bukkit.getPluginManager().registerEvents(new PlayerChangeLevelListener(), this);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            StormHikari.getInstance().loadPlayerModel(player.getUniqueId());
        }
    }
}