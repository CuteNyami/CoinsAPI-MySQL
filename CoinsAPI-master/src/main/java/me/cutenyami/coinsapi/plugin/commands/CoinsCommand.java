package me.cutenyami.coinsapi.plugin.commands;

import me.cutenyami.coinsapi.api.ICoinsAPIUser;
import me.cutenyami.coinsapi.plugin.CoinsAPIMain;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CoinsCommand implements CommandExecutor {

    private final CoinsAPIMain plugin;

    public CoinsCommand(CoinsAPIMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.getBitsAPI().isConnected()) {
            sender.sendMessage(CoinsAPIMain.PREFIX + "§cNo connection to database!");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(CoinsAPIMain.ERROR_PREFIX + "/coins <player>");
            sender.sendMessage(CoinsAPIMain.ERROR_PREFIX + "/coins <player> <set, add, remove> <value>");
            return false;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        if (!plugin.getBitsAPI().existsUser(player.getUniqueId())) {
            sender.sendMessage(CoinsAPIMain.ERROR_PREFIX + "The Player " + args[0] + " was not found!");
            return false;
        }
        ICoinsAPIUser user = plugin.getBitsAPI().getUser(player.getUniqueId());

        if (args.length == 1) {
            sender.sendMessage(CoinsAPIMain.PREFIX + "User: §a" + user.getName());
            sender.sendMessage(CoinsAPIMain.PREFIX + "Value: §a" + user.getCoins());
            return false;
        }
        if (isArg(args[1], "set", "add", "remove")) {

            if (args.length == 2) {
                sender.sendMessage(CoinsAPIMain.ERROR_PREFIX + "Please specify a number!");
                return false;
            }

            if (!isNumber(args[2])) {
                sender.sendMessage(CoinsAPIMain.ERROR_PREFIX + "This is not a number!");
                return false;
            }

            int bits = Integer.parseInt(args[2]);

            if (isArg(args[1], "set")) {
                user.setCoins(bits);
                user.update(true);
                sender.sendMessage(CoinsAPIMain.PREFIX + "Player §a" + args[0] + " §7set §a" + args[2] + " §7coins.");
            }
            if (isArg(args[1], "add")) {
                user.addCoins(bits);
                user.update(true);
                sender.sendMessage(CoinsAPIMain.PREFIX + "Player §a" + args[0] + " §7added §a" + args[2] + " §7coins.");
            }
            if (isArg(args[1], "remove")) {
                user.removeCoins(bits);
                user.update(true);
                sender.sendMessage(CoinsAPIMain.PREFIX + "Player §a" + args[0] + " §7removed §a" + args[2] + " §7coins.");
            }
            return false;
        }
        sender.sendMessage(CoinsAPIMain.ERROR_PREFIX + "/coins <player> <set, add, remove> <value>");
        return false;
    }

    public boolean isArg(String target, String... strings) {
        return Arrays.stream(strings).anyMatch(s -> s.equalsIgnoreCase(target));
    }

    public boolean isNumber(String string) {
        try {
            int i = Integer.parseInt(string);
            System.out.println(i);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

}