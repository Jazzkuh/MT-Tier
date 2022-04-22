package com.jazzkuh.mttier.player;

import com.jazzkuh.mttier.data.models.PlayerModel;
import com.jazzkuh.mttier.player.framework.TierPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerWrapper {

    public static @Getter HashMap<UUID, PlayerModel> playerModels = new HashMap<>();

    public static TierPlayer getTierPlayer(Player player) {
        if (!playerModels.containsKey(player.getUniqueId())) {
            return null;
        };

        return new TierPlayer(player, playerModels.get(player.getUniqueId()));
    }
}
