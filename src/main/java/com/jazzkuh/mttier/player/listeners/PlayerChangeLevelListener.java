package com.jazzkuh.mttier.player.listeners;

import com.jazzkuh.mttier.player.PlayerWrapper;
import com.jazzkuh.mttier.player.framework.TierPlayer;
import com.jazzkuh.mttier.utils.Utils;
import nl.minetopiasdb.api.events.player.PlayerChangeLevelEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChangeLevelListener implements Listener {
    @EventHandler
    public void onPlayerChangeLevel(PlayerChangeLevelEvent event) {
        if (Bukkit.getPlayer(event.getPlayer().getUUID()) == null) return;
        Player player = Bukkit.getPlayer(event.getPlayer().getUUID());
        TierPlayer tierPlayer = PlayerWrapper.getTierPlayer(player);
        Utils.checkPlayerTier(tierPlayer, event.getNewLevel());
    }
}
