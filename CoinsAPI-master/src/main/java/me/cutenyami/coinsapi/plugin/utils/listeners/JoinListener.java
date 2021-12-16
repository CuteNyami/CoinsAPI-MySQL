package me.cutenyami.coinsapi.plugin.utils.listeners;

import me.cutenyami.coinsapi.plugin.CoinsAPIMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final CoinsAPIMain plugin;

    public JoinListener(CoinsAPIMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getCoinsAPI().existsUser(player.getUniqueId())) {
            plugin.getCoinsAPI().addUser(player.getUniqueId(), player.getName());
        }
    }

}
