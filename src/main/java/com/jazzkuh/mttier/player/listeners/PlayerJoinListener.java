package com.jazzkuh.mttier.player.listeners;

import com.jazzkuh.mttier.data.StormHikari;
import com.jazzkuh.mttier.data.configuration.DefaultConfig;
import com.jazzkuh.mttier.player.PlayerWrapper;
import com.jazzkuh.mttier.player.framework.TierPlayer;
import com.jazzkuh.mttier.utils.Utils;
import nl.minetopiasdb.api.playerdata.PlayerManager;
import nl.minetopiasdb.api.playerdata.objects.SDBPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    // We need it, idk why but it works so we'll leave it here.
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        StormHikari.getInstance().loadPlayerModel(event.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        StormHikari.getInstance().loadPlayerModel(event.getPlayer().getUniqueId());

        TierPlayer tierPlayer = PlayerWrapper.getTierPlayer(event.getPlayer());
        if (tierPlayer != null && DefaultConfig.AUTO_UPDATE_TIER.asBoolean()) {
            SDBPlayer sdbPlayer = PlayerManager.getOfflinePlayer(tierPlayer.getUniqueId());
            Utils.checkPlayerTier(tierPlayer, sdbPlayer.getLevel());
        }
    }
}
