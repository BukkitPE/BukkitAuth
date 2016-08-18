package net.bukkitpe;

import java.io.File;
import net.BukkitPE.Player;
import net.BukkitPE.event.Listener;
import net.BukkitPE.plugin.PluginBase;
import net.BukkitPE.command.Command;
import net.BukkitPE.command.CommandSender;
import net.BukkitPE.utils.Config;
import net.bukkitpe.commands.LoginCommand;
import net.bukkitpe.commands.RegisterCommand;

public class BukkitPELogin extends PluginBase implements Listener{
    public Config authenticated_players;
    public LoginCommand logincmd;
    public RegisterCommand registercmd;
    @Override
    public void onEnable(){
        events();
        getLogger().info("BukkitPELogin has been enabled");
    }
    public void events(){
        saveDefaultConfig();
        logincmd = new LoginCommand(this);
        registercmd = new RegisterCommand(this);
        authenticated_players = new Config(this.getDataFolder() + "/authenticated.txt", Config.ENUM);
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        if(!new File(this.getDataFolder() + "").exists()){
            new File(this.getDataFolder() + "").mkdir();
        }
        if(!new File(this.getDataFolder() + "/players").exists()){
            new File(this.getDataFolder() + "/players").mkdir();
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String labels, String[] args){
        if(command.getName().equalsIgnoreCase("login")){
            try{
                return logincmd.onCommand(sender, command, labels, args);
            } catch (SecurityException | IllegalArgumentException e) {
                e.printStackTrace();
            }

        }
        if(command.getName().equalsIgnoreCase("register")){
            try{
                return registercmd.onCommand(sender, command, labels, args);
            } catch (SecurityException | IllegalArgumentException e) {
                e.printStackTrace();
            }

        }
        return true;
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
