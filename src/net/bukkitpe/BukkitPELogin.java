package net.bukkitpe;

import java.io.File;
import java.io.PrintWriter;
import net.BukkitPE.Player;
import net.BukkitPE.event.Listener;
import net.BukkitPE.plugin.PluginBase;
import net.BukkitPE.command.Command;
import net.BukkitPE.command.CommandSender;
import net.BukkitPE.event.player.PlayerMoveEvent;
import net.BukkitPE.event.player.PlayerQuitEvent;
import net.BukkitPE.utils.Config;

public class BukkitPELogin extends PluginBase implements Listener{
    public Config authenticated_players;
    @Override
    public void onEnable(){
        saveDefaultConfig();
        authenticated_players = new Config(this.getDataFolder() + "/authenticated.txt", Config.ENUM);
        if(!new File(this.getDataFolder() + "").exists()){
            new File(this.getDataFolder() + "").mkdir();
        }
        if(!new File(this.getDataFolder() + "/players").exists()){
            new File(this.getDataFolder() + "/players").mkdir();
        }
        getLogger().info("BukkitPELogin has been enabled");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String labels, String[] args){
        if(command.getName().equalsIgnoreCase("login")){
            if(!(sender instanceof Player)){
                sender.sendMessage("§cRun this command in game");
                return false;
            }
            if(args.length == 0){
                sender.sendMessage(this.getConfig().getString("login.usage"));
                return false;
            } else {
                if(isAuthenticated(getServer().getPlayer(sender.getName()))){
                    sender.sendMessage(getConfig().getString("login.authenticated"));
                    return false;
                }else{
                    if(!new File(this.getDataFolder() + "/players/" + sender.getName() + ".yml").exists()){
                        sender.sendMessage(this.getConfig().getString("login.not-registered"));
                        return false;
                    }
                    String password = Encrypt.encryptData(args[0]);
                    Config config_user = new Config(this.getDataFolder() + "/players/" + sender.getName() + ".yml" , Config.YAML);
                    String config_password = config_user.getString("password");
                    if(password.equalsIgnoreCase(config_password)){
                        authenticated_players.set(sender.getName().toLowerCase(), true);
                        authenticated_players.save();
                        sender.sendMessage(getConfig().getString("login.success"));
                        return true;
                    } else {
                        sender.sendMessage(this.getConfig().getString("login.incorrect"));
                        return false;
                    }
                }
            }
        }
        if(command.getName().equalsIgnoreCase("register")){
            if(!(sender instanceof Player)){
                sender.sendMessage("§cRun this command in game");
                return false;
            }
            if(args.length == 0){
                sender.sendMessage(this.getConfig().getString("register.usage"));
                return false;
            }else {
                if(isAuthenticated(getServer().getPlayer(sender.getName()))){
                    sender.sendMessage(getConfig().getString("register.authenticated"));
                    return false;
                } else {
                    String password = Encrypt.encryptData(args[0]);
                    if(args[0].length() < 6){
                        sender.sendMessage("§cYou need more than 6 letters");
                        return false;
                    }
                    if(!new File(this.getDataFolder() + "/players/" + sender.getName() + ".yml").exists()){
                        Config config_user = new Config(this.getDataFolder() + "/players/" + sender.getName() + ".yml" , Config.YAML);
                        config_user.save();
                        config_user.set("password", password);
                        config_user.save();
                        sender.sendMessage(this.getConfig().getString("register.success"));
                    } else {
                        sender.sendMessage(this.getConfig().getString("register.registered"));
                        return false;
                    }
                }
            }
        }
        return false;
    }
    public void onMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(isAuthenticated(player)){
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
        }
    }
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        authenticated_players.remove(player.getName().toLowerCase());
        authenticated_players.save();
    }
    public boolean isAuthenticated(Player player){
        return authenticated_players.exists(player.getName());
    }
    @Override
    public void onDisable(){
        saveDefaultConfig();
        getLogger().info("BukkitLogin has been disabled");
    }
    
}
