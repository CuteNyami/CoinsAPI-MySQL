package me.cutenyami.coinsapi.plugin.commands;

import me.cutenyami.coinsapi.api.ICoinsUser;
import me.cutenyami.coinsapi.plugin.CoinsAPIMain;
import me.cutenyami.coinsapi.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCMD implements CommandExecutor {

    private final CoinsAPIMain plugin;

    public CoinsCMD(CoinsAPIMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!plugin.getCoinsAPI().isConnected()) {
            sender.sendMessage(Utils.ERROR_PREFIX + "Missing Connection to the Database! §8[Change the database in the mysql.json]");
            return false;
        }



        if (args.length == 0) {
            sender.sendMessage(Utils.ERROR_PREFIX + "/coins <player> §8[Shows Player Info]");
            sender.sendMessage(Utils.ERROR_PREFIX + "/coins <player> <set, add, remove> <coins>");
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        ICoinsUser user = plugin.getCoinsAPI().getUser(player.getUniqueId());


        if (!plugin.getCoinsAPI().existsUser(player.getUniqueId())) {
            sender.sendMessage(Utils.ERROR_PREFIX + "Can't found the Player " + args[0]);
            return false;
        }

        if (args.length == 1) {
            sender.sendMessage("§7User Info of " + user.getName());
            sender.sendMessage(Utils.PREFIX + "Coins: §a" + user.getCoins());
            sender.sendMessage(Utils.PREFIX + "UUID: §a" + user.getUniqueId());
            return false;
        }

        if (args[1].equalsIgnoreCase("set") ||
                args[1].equalsIgnoreCase("add") ||
                args[1].equalsIgnoreCase("remove"))
        {
            if (args.length == 2) {
                sender.sendMessage(Utils.ERROR_PREFIX + "You have to set a number!");
                return false;
            }

            if (!isNumber(args[2])) {
                sender.sendMessage(Utils.ERROR_PREFIX + "This is not a number!");
                return false;
            }

            int coins = Integer.parseInt(args[2]);

            if (args[1].equalsIgnoreCase("set")) {

                user.setCoins(coins);
                user.update(true);
                sender.sendMessage(Utils.PREFIX + "Player §a" + args[0] + " §7set §a" + args[2] + "§7coins!");

            }else if (args[1].equalsIgnoreCase("add")) {

                user.addCoins(coins);
                user.update(true);
                sender.sendMessage(Utils.PREFIX + "Player §a" + args[0] + " §7added §a" + args[2] + " §7coins!");

            }else if (args[1].equalsIgnoreCase("remove")) {

                user.removeCoins(coins);
                user.update(true);
                sender.sendMessage(Utils.PREFIX + "Player §a" + args[0] + " removed §a" + args[2] + " §7coins!");

            }
            return false;
        }
        sender.sendMessage(Utils.ERROR_PREFIX + "/coins <player> <set, add, remove> <coins> §8[Remove, set or add Coins]");
        return false;
    }

    public boolean isNumber(String string) {
        int i = Integer.parseInt(string);
        System.out.println(i);
        return true;
    }
}
