package net.bukkitpe.commands;

import java.io.File;
import net.BukkitPE.Player;
import net.BukkitPE.command.Command;
import net.BukkitPE.command.CommandSender;
import net.BukkitPE.event.Listener;
import net.BukkitPE.utils.Config;
import net.bukkitpe.Encrypt;
import net.bukkitpe.BukkitPELogin;

public class LoginCommand implements Listener{
    public BukkitPELogin plugin;
    
    public LoginCommand(BukkitPELogin plugin){
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command command, String labels, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("§cRun this command in game");
            return false;
        }
        if(args.length == 0){
            sender.sendMessage(this.plugin.getConfig().getString("login.usage"));
            return false;
        } else {
            if(this.plugin.isAuthenticated(this.plugin.getServer().getPlayer(sender.getName()))){
                sender.sendMessage(this.plugin.getConfig().getString("login.authenticated"));
                return false;
            }else{
                if(!new File(this.plugin.getDataFolder() + "/players/" + sender.getName() + ".yml").exists()){
                    sender.sendMessage(this.plugin.getConfig().getString("login.not-registered"));
                    return false;
                }
                String password = Encrypt.encryptData(args[0]);
                Config config_user = new Config(this.plugin.getDataFolder() + "/players/" + sender.getName() + ".yml" , Config.YAML);
                String config_password = config_user.getString("password");
                if(password.equalsIgnoreCase(config_password)){
                    this.plugin.authenticated_players.set(sender.getName().toLowerCase(), true);
                    this.plugin.authenticated_players.save();
                    sender.sendMessage(this.plugin.getConfig().getString("login.success"));
                    return true;
                } else {
                    sender.sendMessage(this.plugin.getConfig().getString("login.incorrect"));
                    return false;
                }
            }
        }
    }
}
