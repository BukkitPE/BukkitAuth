package net.bukkitpe.commands;

import java.io.File;
import net.BukkitPE.Player;
import net.BukkitPE.command.Command;
import net.BukkitPE.command.CommandSender;
import net.BukkitPE.event.Listener;
import net.BukkitPE.utils.Config;
import net.bukkitpe.BukkitPELogin;
import net.bukkitpe.Encrypt;

public class RegisterCommand implements Listener{
    public BukkitPELogin plugin;
    public RegisterCommand(BukkitPELogin plugin){
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command command, String labels, String[] args){
        if(!(sender instanceof Player)){sender.sendMessage("§cRun this command in game");
            return false;
        }
        if(args.length == 0){
            sender.sendMessage(this.plugin.getConfig().getString("register.usage"));
            return false;
        }else {
            if(this.plugin.isAuthenticated(this.plugin.getServer().getPlayer(sender.getName()))){
                sender.sendMessage(this.plugin.getConfig().getString("register.authenticated"));
                return false;
            } else {
                String password = Encrypt.encryptData(args[0]);
                if(args[0].length() < 6){
                    sender.sendMessage("§cYou need more than 6 letters!");
                    return false;
                }
                if(!new File(this.plugin.getDataFolder() + "/players/" + sender.getName() + ".yml").exists()){
                    Config config_user = new Config(this.plugin.getDataFolder() + "/players/" + sender.getName() + ".yml" , Config.YAML);
                    config_user.save();
                    config_user.set("password", password);
                    config_user.save();
                    sender.sendMessage(this.plugin.getConfig().getString("register.success"));
                } else {
                    sender.sendMessage(this.plugin.getConfig().getString("register.registered"));
                    return false;
                }
            }
        }
        return false;
    }
}
