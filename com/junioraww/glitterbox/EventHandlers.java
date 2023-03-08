package com.junioraww.glitterbox;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventHandlers implements Listener {
    private static List<String> lines;
    private static final Config config = Main.config;

    public EventHandlers(YamlConfiguration config) {
        lines = config.getStringList("custom-motd.lines");
    }

    @EventHandler
    public void serverMOTD(ServerListPingEvent e) {
        if(!config.getConfig().getBoolean("custom-motd.enable")) return;

        var motd = new ArrayList<>(lines);
        if(motd.get(0).contains("||")) motd.set(0, nextInt(motd.get(0).split("[|][|]")));
        if(motd.get(1).contains("||")) motd.set(1, nextInt(motd.get(1).split("[|][|]")));
        e.setMotd(String.join("\n", motd).replaceAll("&","§"));

        e.setMaxPlayers(config.getConfig().getInt("custom-motd.slots"));

        var serverIcon = Main.getInstance().serverIcon;
        if(serverIcon != null) e.setServerIcon(serverIcon);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        //e.getPlayer().setScoreboard(Main.ms);
        e.setJoinMessage("§7" + e.getPlayer().getName() + " зашёл");
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        e.setQuitMessage("§7" + e.getPlayer().getName() + " вышел");
    }

    private String nextInt(String[] input) {
        return input[new Random().nextInt(input.length)];
    }
}
