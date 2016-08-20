package net.BukkitAuth;

import net.BukkitAuth.manage.Manage;
import net.BukkitPE.Player;
import net.BukkitPE.command.Command;
import net.BukkitPE.command.CommandSender;
import net.BukkitPE.event.EventHandler;
import net.BukkitPE.event.Listener;
import net.BukkitPE.event.block.BlockBreakEvent;
import net.BukkitPE.event.block.BlockPlaceEvent;
import net.BukkitPE.event.block.DoorToggleEvent;
import net.BukkitPE.event.entity.EntityArmorChangeEvent;
import net.BukkitPE.event.inventory.CraftItemEvent;
import net.BukkitPE.event.player.PlayerChatEvent;
import net.BukkitPE.event.player.PlayerCommandPreprocessEvent;
import net.BukkitPE.event.player.PlayerDropItemEvent;
import net.BukkitPE.event.player.PlayerInteractEvent;
import net.BukkitPE.event.player.PlayerItemConsumeEvent;
import net.BukkitPE.event.player.PlayerItemHeldEvent;
import net.BukkitPE.event.player.PlayerJoinEvent;
import net.BukkitPE.event.player.PlayerKickEvent;
import net.BukkitPE.event.player.PlayerLoginEvent;
import net.BukkitPE.event.player.PlayerMoveEvent;
import net.BukkitPE.event.player.PlayerQuitEvent;
import net.BukkitPE.event.plugin.PluginDisableEvent;
import net.BukkitPE.event.server.DataPacketReceiveEvent;
import net.BukkitPE.network.protocol.DataPacket;
import net.BukkitPE.network.protocol.LoginPacket;

class EventListener implements Listener{
	private Main plugin;
	
	EventListener(Main plugin) {
		this.plugin = plugin;
	}
	public Main getPlugin() {
		return plugin;
	}
	public DataBase getDataBase() {
		return plugin.getDataBase();
	}
	public Manage getManage() {
		return plugin.getManage();
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().toLowerCase() == getDataBase().get("command-login")) {
			if (!(sender instanceof Player)) {
				getDataBase().alert(sender, getDataBase().get("must-in-game"));
				return true;
			}
			if(getManage().isLogin(sender)) {
				getDataBase().alert(sender, getDataBase().get("already-login"));
				return true;
			}
			if(!getPlugin().getManage().isRegister(sender)) {
				return true;
			}
			if (args.length == 0) {
				getDataBase().alert(sender, getDataBase().get("command-login-usage"));
				return true;
			}
			if (!getManage().checkPassword(sender, args[0])) {
				getDataBase().alert(sender, getDataBase().get("not-correct-password"));
				return true;
			}
			getManage().setLogin(sender, true);
			return true;
		}
		if (command.getName().toLowerCase() == getDataBase().get("command-register")) {
			if (!(sender instanceof Player)) {
				getDataBase().alert(sender, getDataBase().get("must-in-game"));
				return true;
			}
			if(getManage().isRegister(sender)) {
				getDataBase().alert(sender, getDataBase().get("already-register"));
				return true;
			}
			if (args.length == 0) {
				getDataBase().alert(sender, getDataBase().get("command-register-usage"));
				return true;
			}
			getManage().setRegister(sender, args[0]);
			getDataBase().message(sender, getDataBase().get("register-success"));
			return true;
		}
		if (command.getName().toLowerCase().equals(getDataBase().get("command-manage"))) {
			if (args.length == 0) {
				getDataBase().alert(sender, getDataBase().get("command-manage-usage"));
				return true;
			}
			if (args[0].toLowerCase().equals(getDataBase().get("command-manage-change"))) {
				if (args.length < 3) {
					getDataBase().alert(sender, getDataBase().get("command-manage-change-usage"));
					return true;
				}
				if (!getManage().changePassword(args[1], args[2])) {
					getDataBase().alert(sender, getDataBase().get("not-register"));
					return true;
				}
				getDataBase().message(sender, getDataBase().get("change-password").replaceAll("%player%", args[1]).replaceAll("%password%", args[2]));
				return true;
			}
			if (args[0].toLowerCase().equals(getDataBase().get("command-manage-unregister"))) {
				if (args.length < 2) {
					getDataBase().alert(sender, getDataBase().get("command-manage-unregister-usage"));
					return true;
				}
				if (!getManage().unRegister(args[1])) {
					getDataBase().alert(sender, getDataBase().get("not-register"));
					return true;
				}
				getDataBase().message(sender, getDataBase().get("unregister-player").replaceAll("%player%", args[1]));
				return true;
			}
			if (getDataBase().get("command-manage-subaccount").equals(args[0].toLowerCase())) {
				getManage().setAllowSubAccount(!getManage().isAllowSubAccount());
				getDataBase().alert(sender, getDataBase().get("set-allow-subaccount").replaceAll("%bool%", (getManage().isAllowSubAccount()) ? (getDataBase().messages.get("default-language") == "kor")?"허용":"allow" : (getDataBase().messages.get("default-language") == "kor")?"비허용":"disallow"));
				return true;
			}
			getDataBase().alert(sender, getDataBase().get("command-manage-usage"));
			return true;
		}
		return true;
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!getManage().isLogin(event.getPlayer())) {
			event.setCancelled();
		}
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!getManage().isLogin(event.getPlayer())) {
			event.setCancelled();
		}
	}
	@EventHandler
	public void onDoorToggle(DoorToggleEvent event) {
		if (!getManage().isLogin(event.getPlayer())) {
			event.setCancelled();
		}
	}
	@EventHandler
	public void onArmorChange(EntityArmorChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			if (!getManage().isLogin((Player)event.getEntity())) {
				event.setCancelled();
			}
		}
	}
	@EventHandler
	public void onCraft(CraftItemEvent event) {
		if (!getManage().isLogin(event.getPlayer())) {
			event.setCancelled();
		}
	}
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (getManage().isLogin(event.getPlayer())) {
			return;
		}
		String message = event.getMessage();
		if(message.startsWith("/" + getDataBase().get("command-login")) || message.startsWith("/" + getDataBase().get("command-register"))) {
			return;
		}
		event.setCancelled();
	}
	@EventHandler
	public void onChat(PlayerChatEvent event) {
		if (!getManage().isLogin(event.getPlayer())) {
			event.setCancelled();
		}
	}
	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		if (!getManage().isLogin(event.getPlayer())) {
			event.setCancelled();
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!getManage().isLogin(event.getPlayer())) {
			event.setCancelled();
		}
	}
	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		if (!getManage().isLogin(event.getPlayer())) {
			event.setCancelled();
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (getManage().isRegister(player)) {
			if (getDataBase().getLastLoginIp(player).equals(player.getAddress())) {
				getManage().setLogin(player, true);
				return;
			}
			getDataBase().alert(player, getDataBase().get("command-login-usage"));
			getDataBase().alert(player, getDataBase().get("to-login"));
		}else {
			getDataBase().alert(player, getDataBase().get("command-register-usage"));
			getDataBase().alert(player, getDataBase().get("to-login"));
		}
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		getManage().setLogin(event.getPlayer(), false);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!getManage().isRegister(player)) {
			getDataBase().alert(player, getDataBase().get("command-register-usage"));
			getDataBase().alert(player, getDataBase().get("to-login"));
			event.setCancelled();
			return;
		}
		if (!getManage().isLogin(player)) {
			if (getDataBase().getLastLoginIp(player).equals(player.getAddress())) {
				getManage().setLogin(player, true);
				return;
			}
			getDataBase().alert(player, getDataBase().get("command-login-usage"));
			getDataBase().alert(player, getDataBase().get("to-login"));
			event.setCancelled();
		}
	}
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		getManage().setLogin(event.getPlayer(), false);
		if(getManage().isAllowSubAccount()) {
			return;
		}
		String subAccount = getManage().getSubAccount(event.getPlayer());
		if (subAccount != null) {
			event.setKickMessage(getDataBase().get("kick-subaccount").replaceAll("%name%", subAccount));
			event.setCancelled();
		}
	}
	@EventHandler
	public void loginKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		if (event.getReason().equals("logged in from another location")) {
			if (getManage().isLogin(player)) {
				event.setCancelled();
			}
		}
	}
}