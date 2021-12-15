package me.cutenyami.coinsapi.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.byncing.scheduler.Scheduler;
import me.cutenyami.coinsapi.api.CoinsAPI;
import me.cutenyami.coinsapi.api.ICoinsAPIUser;
import me.cutenyami.coinsapi.plugin.commands.CoinsCommand;
import me.cutenyami.coinsapi.plugin.utils.CoinsAPIConfig;
import me.cutenyami.coinsapi.plugin.utils.placeholder.SpigotExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CoinsAPIMain extends JavaPlugin implements Listener {

    public static final String PREFIX = "§9» §7";
    public static final String ERROR_PREFIX = "§c» §7";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static CoinsAPIMain instance;
    private CoinsAPI coinsAPI;
    @Override
    public void onEnable() {
        instance = this;

        CoinsAPIConfig config = new CoinsAPIConfig();

        coinsAPI = CoinsAPI.getInstance();
        coinsAPI.connect(config.getPools(), config.getProfile());

        getCommand("coins").setExecutor(new CoinsCommand(this));
        getServer().getPluginManager().registerEvents(this, this);

        new SpigotExpansion(this).register();

        Scheduler scheduler = coinsAPI.getScheduler();
        scheduler.runTimer(() -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                ICoinsAPIUser user = coinsAPI.getUser(player.getUniqueId());
                if (user != null) user.update(true);
            });
        }, 1000, 1000);
    }

    @Override
    public void onDisable() {
        coinsAPI.disconnect();
    }

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!coinsAPI.existsUser(player.getUniqueId())) {
            coinsAPI.addUser(player.getUniqueId(), player.getName());
        }
    }

    public static CoinsAPIMain getInstance() {
        return instance;
    }

    public CoinsAPI getBitsAPI() {
        return coinsAPI;
    }
}