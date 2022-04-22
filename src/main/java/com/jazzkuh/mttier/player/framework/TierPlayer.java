package com.jazzkuh.mttier.player.framework;

import com.jazzkuh.mttier.data.models.PlayerModel;
import com.jazzkuh.mttier.utils.Tier;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TierPlayer {

    private final @Getter Player player;
    private final @Getter
    PlayerModel playerModel;
    private final @Getter UUID uniqueId;
    private final @Getter String name;

    public TierPlayer(@NotNull Player player, @NotNull PlayerModel playerModel) {
        this.player = player;
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();
        this.playerModel = playerModel;
    }

    public Tier getTier() {
        return playerModel.getTier();
    }
}
