package com.jazzkuh.mttier.utils.command;

import com.jazzkuh.mttier.Main;
import com.jazzkuh.mttier.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCommand implements TabExecutor {
    public CommandSender sender;
    public Command command;
    public String commandName;
    public Argument[] arguments;

    public AbstractCommand(String commandName) {
        this.commandName = commandName;
        this.arguments = new Argument[]{};
    }

    public AbstractCommand(String commandName, Argument... arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public void register(Main plugin) {
        this.register(plugin, false);
    }

    public void register(Main plugin, Boolean needsPermission) {
        PluginCommand cmd = plugin.getCommand(commandName);
        if (cmd != null) {
            if (needsPermission) {
                cmd.setPermission(getBasePermission());
                cmd.setPermissionMessage(Utils.color("&cTo use this command you need permission " + getBasePermission() + "."));
            }
            cmd.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.command = command;
        this.sender = sender;
        this.execute(sender, command, label, args);
        return true;
    }

    public abstract void execute(CommandSender sender, Command command, String label, String[] args);

    protected boolean senderIsPlayer() {
        return sender instanceof Player;
    }

    protected boolean hasPermission(String permission, boolean silent) {
        if (sender.hasPermission(permission)) {
            return true;
        }

        if (!silent) {
            sender.sendMessage(Utils.color("&cTo use this command you need permission " + permission + "."));
        }
        return false;
    }

    protected void addIfPermission(CommandSender sender, Collection<String> options, String permission, String option) {
        if (sender.hasPermission(permission)) {
            options.add(option);
        }
    }

    protected String getBasePermission() {
        return "mttier.command." + commandName;
    }

    protected void sendNotEnoughArguments(AbstractCommand command) {
        sender.sendMessage(Utils.color("&cNo subcommand specified."));
        sender.sendMessage(Utils.color("&6/" + command.command.getName() + " <subcommand> <arg>..."));
        for (Argument argument : this.arguments) {
            if (argument.getPermission() == null || sender.hasPermission(argument.getPermission())) {
                String description = argument.getDescription() == null ? "No description" : argument.getDescription();
                sender.sendMessage(Utils.color("&a/" + command.command.getName() + " &2" + argument.getArguments() + "&f - &a" + description));
            }
        }
    }

    protected List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
        return StringUtil.copyPartialMatches(arg, completions, new ArrayList<>(completions.size()));
    }
}
