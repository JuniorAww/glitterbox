package com.junioraww.glitterbox;

import com.junioraww.glitterbox.utils.Animation;
import com.junioraww.glitterbox.utils.Glitterbox;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.CachedServerIcon;

import java.io.File;
import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }
    public CachedServerIcon serverIcon = null;

    public static Config config = null;
    public static Config tab = null;

    public void loadConfig() {
        File dir = this.getDataFolder();
        if (!dir.exists()) dir.mkdir();

        File fileTab = new File(this.getDataFolder(), "tab-format.yml");
        tab = new Config(fileTab);
        File file = new File(this.getDataFolder(), "config.yml");
        config = new Config(file);
        if (!config.load() || !tab.load()) {
            this.getServer().getPluginManager().disablePlugin(this);
            throw new IllegalStateException("The config-file was not loaded correctly!");
        }

        displayPing = config.getConfig().getBoolean("display-ping");

        var config_ = config.getConfig();

        // Custom server icon
        if(config_.getBoolean("custom-motd.icon")) {
            this.saveResource("icon.png", false);
            File serverIconFile = new File(this.getDataFolder() + "/icon.png");
            try {
                serverIcon = Bukkit.loadServerIcon(serverIconFile);
            } catch (Exception ignored) {}
        }

        // Loads tab-format
        var tabConf = tab.getConfig();
        headerTab = String.join("\n", tabConf.getStringList("header"));
        footerTab = String.join("\n", tabConf.getStringList("footer"));
    }

    int tickSchedule;
    int pingSchedule;
    public static Objective playersPing;
    public static Scoreboard ms;
    public Boolean UsePAPI = false; // Placeholder API
    public String headerTab;
    public String footerTab;
    public Boolean displayPing;

    // This thing loads everything and starts per-tick schedules
    public void enable() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        /* Starts ping schedule, etc */
        if(displayPing) {
            if (ms.getObjective("ping") == null) ms.registerNewObjective("ping", Criteria.DUMMY, "players_ping");
            playersPing = ms.getObjective("ping");
            playersPing.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            pingSchedule = scheduler.scheduleSyncRepeatingTask(this, () -> {
                for(var p : Bukkit.getOnlinePlayers()) {
                    playersPing.getScore(p.getName()).setScore(p.getPing());
                }
            }, 0L, 60L);
        }

        /* Starts tab update */
        tickSchedule = scheduler.scheduleSyncRepeatingTask(this, () -> {
            for (var p : Bukkit.getOnlinePlayers()) {
                var header = parse(headerTab, p);
                var footer = parse(footerTab, p);
                p.setPlayerListHeaderFooter(header, footer);
            }
        }, 0L, 1L);
    }

    private final Boolean motdRegistered = false;

    private String parse(String input, Player player) {
        if(instance.UsePAPI) {
            input = PlaceholderAPI.setPlaceholders(player, input);
        }
        if(input.contains("!")) {
            input = input.replace("!ping", Integer.toString(player.getPing())).replace("!online", getOnline()).replace("!player", player.getName());
        }

        var result = new ArrayList<String>();
        for(var n : input.split("\n")) {
            if(n.matches("^(&[a-z0-9])>(&[a-z0-9])\\|(.*)")) result.add(Animation.transit(n));
            else if(n.matches("^[a-fRGB0-9]{6}\\|(.*)")) result.add(Animation.glowing(n));
            else result.add(n);
        }

        return String.join("\n", result).replaceAll("&","§");
    }

    private String getOnline() {
        return Integer.toString(Bukkit.getOnlinePlayers().size());
    }


    @Override
    public void onEnable() {
        instance = this;
        ms = Bukkit.getScoreboardManager().getMainScoreboard();
        //for(var p : Bukkit.getOnlinePlayers()) { p.setScoreboard(ms); };

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            UsePAPI = true;
            Bukkit.getLogger().info("PlaceholderAPI is being used");
        }

        loadConfig();
        enable();

        this.getCommand("glitterbox").setExecutor(new Glitterbox());
        Bukkit.getPluginManager().registerEvents(new EventHandlers(config.getConfig()), this);
    }

    @Override
    public void onDisable() {
        if(displayPing && ms.getObjective("ping") != null) ms.getObjective("ping").setDisplaySlot(DisplaySlot.PLAYER_LIST);
        instance = null;
    }
}
