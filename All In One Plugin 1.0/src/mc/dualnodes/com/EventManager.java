package mc.dualnodes.com;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

public class EventManager implements Listener{
	
	public EventManager(MZNPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void clickItem(PlayerInteractEvent evnt) {
		if(evnt.getMaterial().compareTo(Material.BEDROCK) == 0) {
			evnt.setCancelled(true);
		}
	}
	
	@EventHandler
	public void teleport(PlayerTeleportEvent evnt) {
		Player player = evnt.getPlayer();
		if(evnt.getCause() != TeleportCause.SPECTATE && evnt.getCause() != TeleportCause.UNKNOWN) {
			MZNPlugin.backs.put(player.getName(), evnt.getFrom());
		} else if (evnt.getCause() != TeleportCause.UNKNOWN) {
			MZNPlugin.backs.remove(player.getName());
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent evnt) {
		Player player = evnt.getPlayer();
		
		//Sends player welcome message
		player.sendMessage(ChatColor.GOLD + "Welcome to The Server!");
		player.sendMessage(ChatColor.AQUA + "Do /help for help with commands!");
	}
	
	@EventHandler
	public void grab(PlayerDropItemEvent evnt) {
		if(evnt.getItemDrop().getItemStack().getType().compareTo(Material.BEDROCK) == 0) {
			evnt.getItemDrop().setItemStack(new ItemStack(Material.AIR, 0));
		}
	}
}
