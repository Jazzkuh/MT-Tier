package com.jazzkuh.mttier.data;

import com.craftmend.storm.parser.types.TypeRegistry;
import com.jazzkuh.mttier.Main;
import com.jazzkuh.mttier.data.adapters.TierAdapter;
import com.jazzkuh.mttier.utils.Tier;

import java.sql.SQLException;

public class DataManager {
    public void initialize() {
        StormHikari stormHikari = new StormHikari();
        TypeRegistry.registerAdapter(Tier.class, new TierAdapter());

        try {
            stormHikari.init();
        } catch (SQLException exception) {
            Main.getInstance().getLogger().severe("Failed to connect to the database.");
        }
    }
}
