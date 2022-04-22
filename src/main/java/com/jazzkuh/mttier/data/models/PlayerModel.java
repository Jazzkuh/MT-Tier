package com.jazzkuh.mttier.data.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import com.jazzkuh.mttier.utils.Tier;
import lombok.Data;

import java.util.UUID;

@Data
@Table(name = "players")
public class PlayerModel extends StormModel {
    private @Column UUID uniqueId;
    private @Column(defaultValue = "GRAY") Tier tier;
}
