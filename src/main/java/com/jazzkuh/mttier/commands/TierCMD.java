package com.jazzkuh.mttier.commands;

import com.jazzkuh.mttier.data.StormHikari;
import com.jazzkuh.mttier.data.models.PlayerModel;
import com.jazzkuh.mttier.player.PlayerWrapper;
import com.jazzkuh.mttier.player.framework.TierPlayer;
import com.jazzkuh.mttier.utils.Tier;
import com.jazzkuh.mttier.utils.Utils;
import com.jazzkuh.mttier.utils.command.AbstractCommand;
import com.jazzkuh.mttier.utils.command.Argument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TierCMD extends AbstractCommand {

    public TierCMD() {
        super("tier",
                new Argument("info", null),
                new Argument("info <player>", null, "mttier.command.tier.info"),
                new Argument("set <player> <tier>", null, "mttier.command.tier.set"));
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (!senderIsPlayer()) return;

        Player player = (Player) sender;
        List<String> availableTiers = Arrays.stream(Tier.values()).map(Tier::name).collect(Collectors.toList());

        if (args.length < 1) {
            this.sendNotEnoughArguments(this);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "info": {
                if (args.length > 1) {
                    if (!hasPermission(getBasePermission() + ".info", false)) return;

                    if (Bukkit.getPlayer(args[1]) == null) {
                        Utils.sendMessage(sender, "&cJe hebt geen geldige speler opgegeven.");
                        return;
                    }

                    Player target = Bukkit.getPlayer(args[1]);
                    TierPlayer tierPlayer = PlayerWrapper.getTierPlayer(target);

                    Utils.sendMessage(sender, "&6De huidige tier van &c" + target.getName() + " &6is &c" + tierPlayer.getTier().getTierIcon() + "&6.");
                } else {
                    TierPlayer tierPlayer = PlayerWrapper.getTierPlayer(player);
                    Utils.sendMessage(sender, "&6Jouw huidige tier is &c" + tierPlayer.getTier().getTierName() + "&6.");
                }
                break;
            }
            case "set": {
                if (!hasPermission(getBasePermission() + ".set", false)) return;
                if (args.length < 3) {
                    this.sendNotEnoughArguments(this);
                    return;
                }

                if (Bukkit.getPlayer(args[1]) == null) {
                    Utils.sendMessage(sender, "&cJe hebt geen geldige speler opgegeven.");
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (!availableTiers.contains(args[2].toUpperCase())) {
                    Utils.sendMessage(sender, "&cDe tier (&4" + args[2] + "&c) kon niet gevonden worden. Kies uit: "
                            + StringUtils.join(availableTiers, ", "));
                    return;
                }

                Tier tier = Tier.valueOf(args[2].toUpperCase());
                TierPlayer tierPlayer = PlayerWrapper.getTierPlayer(target);
                PlayerModel playerModel = tierPlayer.getPlayerModel();
                playerModel.setTier(tier);

                StormHikari.getInstance().saveStormModel(playerModel).thenAccept(integer -> StormHikari.getInstance().loadPlayerModel(tierPlayer.getUniqueId()));

                Utils.sendMessage(player, "&6Je hebt de tier van &c" + target.getName() + " &6verzet naar &c" + tier.getTierName() + "&6.");
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            Set<String> options = new HashSet<>(Collections.singletonList("info"));
            addIfPermission(sender, options, getBasePermission() + ".set", "set");
            return getApplicableTabCompleters(args[0], options);
        }

        if (args.length == 2) {
            return getApplicableTabCompleters(args[1], Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        }

        if (args.length == 3 && !args[0].equalsIgnoreCase("info")
                && sender.hasPermission(getBasePermission() + ".set")) {
            return getApplicableTabCompleters(args[2],
                    Arrays.stream(Tier.values()).map(tier -> tier.name().toLowerCase()).collect(Collectors.toList()));
        }

        return Collections.emptyList();
    }
}