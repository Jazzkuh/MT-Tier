package com.jazzkuh.mttier.data;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.connection.hikaricp.HikariDriver;
import com.craftmend.storm.connection.sqlite.SqliteFileDriver;
import com.jazzkuh.mttier.Main;
import com.jazzkuh.mttier.data.configuration.DefaultConfig;
import com.jazzkuh.mttier.data.models.PlayerModel;
import com.jazzkuh.mttier.player.PlayerWrapper;
import com.jazzkuh.mttier.utils.Tier;
import com.zaxxer.hikari.HikariConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StormHikari {
    private static @Getter @Setter(AccessLevel.PRIVATE) StormHikari instance;
    private @Getter Storm storm;
    private static @Getter ExecutorService executorService;

    public StormHikari() {
        setInstance(this);

        executorService = Executors.newFixedThreadPool(10);
    }

    public void init() throws SQLException {
        if (DefaultConfig.DATABASE_USE_SQLITE.asBoolean()) {
            storm = new Storm(new SqliteFileDriver(new File(Main.getInstance().getDataFolder(), "database.db")));
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + DefaultConfig.DATABASE_IP.asString() + ":" + DefaultConfig.DATABASE_PORT.asString() + "/" + DefaultConfig.DATABASE_NAME.asString());
            config.setUsername(DefaultConfig.DATABASE_USERNAME.asString());
            config.setPassword(DefaultConfig.DATABASE_PASSWORD.asString());
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("jdbcCompliantTruncation", "false");
            config.setPoolName("MTTierPool");

            storm = new Storm(new HikariDriver(config));
        }

        storm.registerModel(new PlayerModel());
        storm.runMigrations();

        Main.getInstance().getLogger().info("Succesfully connected to the database.");
    }

    public CompletableFuture<Optional<PlayerModel>> findPlayerModel(@NotNull UUID uuid) {
        CompletableFuture<Optional<PlayerModel>> completableFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                Collection<PlayerModel> playerModel;
                playerModel = storm.buildQuery(PlayerModel.class)
                        .where("unique_id", Where.EQUAL, uuid.toString())
                        .limit(1)
                        .execute()
                        .join();

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> completableFuture.complete(playerModel.stream().findFirst()));
            } catch (Exception exception) {
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }

    @SuppressWarnings("all")
    public CompletableFuture<PlayerModel> loadPlayerModel(UUID uuid) {
        CompletableFuture<PlayerModel> completableFuture = new CompletableFuture<>();
        StormHikari.getInstance().findPlayerModel(uuid).thenAccept(playerModel -> {
            PlayerWrapper.playerModels.remove(uuid);

            if (playerModel.isEmpty()) {
                PlayerModel createdModel = new PlayerModel();
                createdModel.setUniqueId(uuid);
                createdModel.setTier(Tier.GRAY);
                PlayerWrapper.playerModels.put(uuid, createdModel);
                completableFuture.complete(createdModel);

                StormHikari.getInstance().saveStormModel(createdModel);
                return;
            }

            PlayerWrapper.playerModels.put(uuid, playerModel.get());
            completableFuture.complete(playerModel.get());
        });

        return completableFuture;
    }

    public CompletableFuture<Integer> saveStormModel(StormModel stormModel) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                completableFuture.complete(storm.save(stormModel));
            } catch (SQLException exception) {
                completableFuture.completeExceptionally(exception);
            }
        });

        return completableFuture;
    }
}
