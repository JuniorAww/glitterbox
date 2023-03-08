package com.junioraww.glitterbox.utils;

import com.junioraww.glitterbox.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Glitterbox implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0) {
            if(args[0].matches("reload") && sender.isOp()) {
                Main.getInstance().reloadConfig();
                Main.getInstance().reloadConfig();
                sender.sendMessage("§6(Glitter§bbox) §fReloaded configs!");
            }
            else sender.sendMessage("§6(Glitter§bbox) §fWhat? You can only: §c/gb reload");
        } else sender.sendMessage("§6(Glitter§bbox) §fPlugin is active!");

        return true;
    }

}