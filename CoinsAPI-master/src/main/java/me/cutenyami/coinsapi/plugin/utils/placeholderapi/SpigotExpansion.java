package me.cutenyami.coinsapi.plugin.utils.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.cutenyami.coinsapi.api.ICoinsUser;
import me.cutenyami.coinsapi.plugin.CoinsAPIMain;
import org.bukkit.entity.Player;

public class SpigotExpansion extends PlaceholderExpansion {

    private final CoinsAPIMain plugin;

    public SpigotExpansion(CoinsAPIMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "coinsapi";
    }

    @Override
    public String getAuthor() {
        return "CuteNyami";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (p == null) {
            return "";
        }
        if (params.equals("coins")) {
            ICoinsUser user = plugin.getCoinsAPI().getUser(p.getUniqueId());
            return "" + user.getCoins();
        }

        return null;
    }
}
