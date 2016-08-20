package net.BukkitAuth;

import net.BukkitAuth.manage.Manage;
import net.BukkitPE.command.Command;
import net.BukkitPE.command.CommandSender;
import net.BukkitPE.plugin.PluginBase;

public class Main extends PluginBase{
	private EventListener listener;
	private DataBase dataBase;
	private Manage manage;
	
	public void onEnable() {
		listener = new EventListener(this);
		dataBase = new DataBase(this);
		manage = new Manage(this);
		
		this.getServer().getPluginManager().registerEvents(listener, this);
	}
	public void onDisable() {
		dataBase.save();
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			return listener.onCommand(sender, command, label, args);
		} catch (SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return true;
	}
	public DataBase getDataBase() {
		return dataBase;
	}
	public Manage getManage() {
		return manage;
	}
}