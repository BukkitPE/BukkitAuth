package net.bukkitpe;
 
import net.BukkitPE.Player;
import net.BukkitPE.event.Listener;
import net.BukkitPE.event.player.PlayerMoveEvent;
import net.BukkitPE.event.player.PlayerQuitEvent;

public class EventListener implements Listener{

    public BukkitPELogin plugin;
    
    EventListener(BukkitPELogin plugin){
        this.plugin = plugin;
    }
    public BukkitPELogin getPlugin(){
        return plugin;
    }
    public void onMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(!this.plugin.isAuthenticated(player)){
            e.setCancelled();
            player.onGround = true;
        }
    }
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        this.plugin.authenticated_players.remove(player.getName().toLowerCase());
        this.plugin.authenticated_players.save();
    }
}
