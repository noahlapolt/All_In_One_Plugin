package mc.dualnodes.com;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class DNPlugin extends JavaPlugin {

	public static Map<String, String> tpRequests = new HashMap<String, String>();
	public static Map<String, Location> backs = new HashMap<String, Location>();
	
	@Override
	public void onEnable() {
		getLogger().info("MZN Enabled.");
		new EventManager(this);
	}
	
	@Override
	public void onDisable() {
		getLogger().info("MZN Disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		
		if(sender.isOp()) {
			//Utilities 
			if(cmd.getName().equalsIgnoreCase("fix") && sender instanceof Player) {
				Player player = (Player) sender;
				ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
				
				if(meta instanceof Damageable) {
					((Damageable) meta).setDamage(0);
					player.getInventory().getItemInMainHand().setItemMeta(meta);
					player.sendMessage(ChatColor.BLUE + "Fixed item.");
				} else {
					player.sendMessage(ChatColor.RED + "Cannot fix item!");
				}
				
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("heal") && sender instanceof Player) {
				Player player = (Player) sender;
				int length = args.length;
				
				//Determines if healing yourself or someone else.
				if(length == 0) {
					//Heals yourself.
					player.setHealth(20.0);
					player.setFoodLevel(40);
					player.sendMessage(ChatColor.GREEN + "Healed!");
				} 
				else if(length == 1) {
					Player player2 = Bukkit.getServer().getPlayer(args[0]); //Gets the player from the server.
					//Determines if the player exists.
					if(player2 != null) {
						//Heals target.
						player2.setHealth(20.0);
						player2.setFoodLevel(40);
						player.sendMessage(ChatColor.GREEN + "Healed " + player2.getName() + "!");
						player2.sendMessage(ChatColor.GREEN + "Healed by " + player.getName() + "!");
					} else {
						player.sendMessage(ChatColor.RED + "Player not found.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Incorrect ussage: Do /heal (name)");
				}
				
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("ride") && sender instanceof Player) {
				Player player = (Player) sender;
				
				//Determines if there is a correct number of args.
				if(args.length == 1) {
					Player player2 = Bukkit.getServer().getPlayer(args[0]); //Gets the player from the server.
					
					//Determines if the player exists.
					if(player2 != null) {
						//Rides player and informs them.
						player2.addPassenger(player);
						player2.sendMessage(ChatColor.GOLD + player.getName() + " is riding you.");
						player.sendMessage(ChatColor.GOLD + "You are riding " + player2.getName() + ".");
					} else {
						player.sendMessage(ChatColor.RED + "Player not found.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Incorrect ussage: Do /ride [name]");
				}
				
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("fly") && sender instanceof Player) {
				Player player = (Player) sender;
				if(player.getAllowFlight() == true) {
					player.setAllowFlight(false);
					player.setFlying(false);
					player.sendMessage(ChatColor.GRAY + "You can no longer fly.");
				} else {
					player.setAllowFlight(true);
					player.sendMessage(ChatColor.GRAY + "You can now fly.");
				}
				
				return true;
			}
			
			//Gamemode Commands
			if(cmd.getName().equalsIgnoreCase("gmc") && sender instanceof Player) {
				Player player = (Player) sender;
				player.setGameMode(GameMode.CREATIVE);
				player.sendMessage(ChatColor.GRAY + "You are now in Creative Mode.");
				
				return true;
			}
			if(cmd.getName().equalsIgnoreCase("gms") && sender instanceof Player) {
				Player player = (Player) sender;
				player.setGameMode(GameMode.SURVIVAL);
				player.sendMessage(ChatColor.GRAY + "You are now in Survival Mode.");
				
				return true;
			}
			if(cmd.getName().equalsIgnoreCase("gma") && sender instanceof Player) {
				Player player = (Player) sender;
				player.setGameMode(GameMode.ADVENTURE);
				player.sendMessage(ChatColor.GRAY + "You are now in Adventure Mode.");
				
				return true;
			}
			if(cmd.getName().equalsIgnoreCase("gmsp") && sender instanceof Player) {
				Player player = (Player) sender;
				player.setGameMode(GameMode.SPECTATOR);
				player.sendMessage(ChatColor.GRAY + "You are now a Spectator.");
				
				return true;
			}
			
			//Player Interaction
			if(cmd.getName().equalsIgnoreCase("nickname") && sender instanceof Player) {
				Player player = (Player) sender;
				
				if(args.length == 1) {
					player.setCustomName(args[0]);
					player.setDisplayName(args[0]);
					player.setPlayerListName(args[0]);
					player.setCustomNameVisible(true);
					player.sendMessage(ChatColor.GREEN + "Your name is now " + args[0] + ".");
				} else {
					player.sendMessage(ChatColor.RED + "Not the correct number of variables.");
					player.sendMessage(ChatColor.GRAY + "Ussage /nickname [name]");
				}
				
				return true;
			}
			
			//Goes to last tp location.
			if(cmd.getName().equalsIgnoreCase("back") && sender instanceof Player) {
				Player player = (Player) sender;
				Location loc = backs.get(player.getName());
				if(loc != null) {
					player.teleport(loc, TeleportCause.SPECTATE);
				} else {
					player.sendMessage(ChatColor.BLUE + "No where to go back to.");
				}
				
				return true;
			}
			
			//Looks into player inventory
			if(cmd.getName().equalsIgnoreCase("invsee") && sender instanceof Player) {
				Player player = (Player) sender;
				
				//Determines if there is a correct number of args.
				if(args.length == 1) {
					Player player2 = Bukkit.getServer().getPlayer(args[0]); //Gets the player from the server.
					
					//Determines if the player exists.
					if(player2 != null) {
						player.openInventory(player2.getInventory()); 		//Opens that players inventory.
					} else {
						player.sendMessage(ChatColor.RED + "Could not find " + args[0] + ".");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Incorrect ussage: Do /invsee [name]");
				}
				
				return true;
			}
			
			//Looks into player end inventory
			if(cmd.getName().equalsIgnoreCase("endinvsee") && sender instanceof Player) {
				Player player = (Player) sender;
				
				//Determines if there is a correct number of args.
				if(args.length == 1) {
					Player player2 = Bukkit.getServer().getPlayer(args[0]); //Gets the player from the server.
					
					//Determines if the player exists.
					if(player2 != null) {
						player.openInventory(player2.getEnderChest()); 		//Opens that players inventory.
					} else {
						player.sendMessage(ChatColor.RED + "Could not find " + args[0] + ".");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Incorrect ussage: Do /endinvsee [name]");
				}
				
				return true;
			}
		} 
		
		//Moving in one world
		if(cmd.getName().equalsIgnoreCase("home") && sender instanceof Player) {
			Player player = (Player) sender;
			
			if(player.getBedSpawnLocation() != null && player.getBedSpawnLocation().getWorld().getName().contains(player.getWorld().getName().split("_")[0])) {
				player.teleport(player.getBedSpawnLocation(), TeleportCause.PLUGIN);
				player.sendMessage(ChatColor.GOLD + "Teleported you to your bed.");
			} else {
				player.sendMessage(ChatColor.GRAY + "You don't have a bed.");
			}
			
			return true;
		}
		
		
		//Teleport to other players if they accept
		if(cmd.getName().equalsIgnoreCase("tpa") && sender instanceof Player) {
			Player player = (Player) sender;
			
			if(args.length == 1) {
				Player player2 = Bukkit.getServer().getPlayer(args[0]); //Gets the player from the server.
				
				if(player2 != null) {
					if(player2.getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
						if(!tpRequests.containsKey(player2.getName())) {
							player.sendMessage(ChatColor.DARK_AQUA + "TP request sent to " + args[0] + ". They have 20 seconds to accept.");
							player2.sendMessage(ChatColor.DARK_AQUA + player.getName() + " has requested to tp to you use /tpaccept or /tpdeny to accept or deny this tp request. You have 20 seconds to accept or deny.");
							tpRequests.put(player2.getName(), player.getName());
							
							//Schedules the teleport
							BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				            scheduler.scheduleSyncDelayedTask(this, new Runnable() {
				                @Override
				                public void run() {
				                	if(tpRequests.containsKey(player2.getName())) {
				                		tpRequests.remove(player2.getName());
				                		player.sendMessage(ChatColor.DARK_AQUA + "Teleport request has expired.");
				                		player2.sendMessage(ChatColor.DARK_AQUA + "Teleport request has expired.");
				                	}
				                }
				            }, 400);
						} else {
							player.sendMessage(ChatColor.DARK_AQUA + "Someone has already requested to tp to this player. Try again later.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Player is in another world.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Player not found.");
				}
			} else {
				player.sendMessage(ChatColor.DARK_RED + "Incorrect ussage: Do /tpa [name]");
			}
			
			return true;
		}
		
		//Accept teleport request.
		if(cmd.getName().equalsIgnoreCase("tpaccept") && sender instanceof Player) {
			Player player = (Player) sender;
			Set<Entry<String, String>> entries = tpRequests.entrySet();
			Iterator<Entry<String, String>> itr = entries.iterator();
			Entry<String, String> entry = null;
			
			//Finds the tpa request.
			if(itr.hasNext()) {
				do {
					entry = itr.next();
				} while(itr.hasNext() && !entry.getKey().contentEquals(player.getName()));
			}
				
			
			
			if(entry != null && entry.getKey().contentEquals(player.getName())) {
				Player player2 = Bukkit.getServer().getPlayer(entry.getValue()); //Gets the player from the server.;
				
				if(player2 != null) {
					player2.sendMessage(ChatColor.DARK_AQUA + "TP accepted. Teleporting...");
					player2.sendMessage(ChatColor.DARK_AQUA + "3");
					player.sendMessage(ChatColor.DARK_AQUA + "3");
					
					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(this, new Runnable() {
		                @Override
		                public void run() {
		                	player2.sendMessage(ChatColor.DARK_AQUA + "2");
							player.sendMessage(ChatColor.DARK_AQUA + "2");
		                }
		            }, 20);
					
					scheduler.scheduleSyncDelayedTask(this, new Runnable() {
		                @Override
		                public void run() {
		                	player2.sendMessage(ChatColor.DARK_AQUA + "1");
							player.sendMessage(ChatColor.DARK_AQUA + "1");
		                }
		            }, 40);
					
		            scheduler.scheduleSyncDelayedTask(this, new Runnable() {
		                @Override
		                public void run() {
		                	player2.teleport(player.getLocation(), TeleportCause.PLUGIN);
		                }
		            }, 60);
					
					tpRequests.remove(player.getName());
				} else {
					player.sendMessage(ChatColor.RED + "Player not found.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "TPA request not found.");
			}
			
			return true;
		}
		
		//Deney teleport request.
		if(cmd.getName().equalsIgnoreCase("tpdeny") && sender instanceof Player) {
			Player player = (Player) sender;
			Set<Entry<String, String>> entries = tpRequests.entrySet();
			Iterator<Entry<String, String>> itr = entries.iterator();
			Entry<String, String> entry = null;
			
			//Finds the tpa request.
			if(itr.hasNext()) {
				do {
					entry = itr.next();
				} while(itr.hasNext() && !entry.getKey().contentEquals(player.getName()));
			}
			
			if(entry != null && entry.getKey().contentEquals(player.getName())) {
				Player player2 = Bukkit.getServer().getPlayer(entry.getValue()); //Gets the player from the server.;
				
				if(player2 != null) {
					player2.sendMessage(ChatColor.DARK_AQUA + "TP denied.");
					player.sendMessage(ChatColor.DARK_AQUA + "TP denied.");
					tpRequests.remove(player.getName());
				} else {
					player.sendMessage(ChatColor.RED + "Player not found.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "TPA request not found.");
			}
			
			return true;
		}
		
		return false;
	}
}
