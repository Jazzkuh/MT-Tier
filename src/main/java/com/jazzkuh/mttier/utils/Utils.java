package com.jazzkuh.mttier.utils;

import com.jazzkuh.mttier.Main;
import com.jazzkuh.mttier.data.StormHikari;
import com.jazzkuh.mttier.data.models.PlayerModel;
import com.jazzkuh.mttier.player.framework.TierPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {

    Main plugin;

    public Utils(Main plugin) {
        this.plugin = plugin;
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
    public static void sendMessage(Player player, String input) {
        player.sendMessage(Utils.color(input));
    }
    public static void sendMessage(CommandSender sender, String input) {
        sender.sendMessage(Utils.color(input));
    }

    public static void checkPlayerTier(TierPlayer tierPlayer, Integer playerLevel) {
        if (tierPlayer.getTier() == Tier.STAFF || tierPlayer.getTier() == Tier.PROJECTLEIDER) return;

        for (Tier tier : Tier.values()) {
            if (tier.getLevelRequired() == null && tier.getMaxLevel() == null) continue;

            if (tier.getMaxLevel() == null && playerLevel == tier.getLevelRequired()) {
                PlayerModel playerModel = tierPlayer.getPlayerModel();
                playerModel.setTier(tier);

                StormHikari.getInstance().saveStormModel(playerModel).thenAccept(integer -> StormHikari.getInstance().loadPlayerModel(tierPlayer.getUniqueId()));
                return;
            }

            if (tier.getLevelRequired() != null && tier.getMaxLevel() != null) {
                if (playerLevel >= tier.getLevelRequired() && playerLevel <= tier.getMaxLevel()) {
                    PlayerModel playerModel = tierPlayer.getPlayerModel();
                    playerModel.setTier(tier);

                    StormHikari.getInstance().saveStormModel(playerModel).thenAccept(integer -> StormHikari.getInstance().loadPlayerModel(tierPlayer.getUniqueId()));
                    return;
                }
            }
        }
    }
}

