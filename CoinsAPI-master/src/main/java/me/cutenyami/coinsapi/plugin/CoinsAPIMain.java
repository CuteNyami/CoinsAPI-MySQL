package me.cutenyami.coinsapi.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.byncing.scheduler.Scheduler;
import me.cutenyami.coinsapi.api.CoinsAPI;
import me.cutenyami.coinsapi.api.ICoinsUser;
import me.cutenyami.coinsapi.plugin.commands.CoinsCMD;
import me.cutenyami.coinsapi.plugin.utils.configs.CoinsAPIConfig;
import me.cutenyami.coinsapi.plugin.utils.listeners.JoinListener;
import me.cutenyami.coinsapi.plugin.utils.placeholderapi.SpigotExpansion;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CoinsAPIMain extends JavaPlugin implements Listener {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static CoinsAPIMain instance;
    private CoinsAPI coinsAPI;

    @Override
    public void onEnable() {
        instance = this;

        CoinsAPIConfig config = new CoinsAPIConfig();

        coinsAPI = CoinsAPI.getInstance();
        coinsAPI.connect(config.getPools(), config.getProfile());

        getCommand("coins").setExecutor(new CoinsCMD(this));
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        new SpigotExpansion(this).register();

        Scheduler scheduler = coinsAPI.getScheduler();
        scheduler.runTimer(() -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                ICoinsUser user = coinsAPI.getUser(player.getUniqueId());
                if (user != null) user.update(true);
            });
        }, 1000, 1000);
    }

    @Override
    public void onDisable() {
        coinsAPI.disconnect();
    }

    public static CoinsAPIMain getInstance() {
        return instance;
    }

    public CoinsAPI getCoinsAPI() {
        return coinsAPI;
    }
}