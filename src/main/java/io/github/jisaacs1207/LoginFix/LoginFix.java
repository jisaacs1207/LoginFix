package io.github.jisaacs1207.LoginFix;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class LoginFix extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getLogger().info("Login fixedly fixed.");
		getServer().getPluginManager().registerEvents(this, this);
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {   // Every 30 seconds, checks and documents location for crash redundancy
            	for(Player player : Bukkit.getOnlinePlayers()) {
            		String pName = player.getName().toString();
            		String pWorld = player.getWorld().getName().toString();
            		int x = player.getLocation().getBlock().getX();
            	    int y = player.getLocation().getBlock().getY();
            	    int z = player.getLocation().getBlock().getZ();
            	    if((!pWorld.equalsIgnoreCase("world"))&&(!pWorld.equalsIgnoreCase("world_nether"))&&(!pWorld.equalsIgnoreCase("world_end"))){
        				x = getConfig().getInt("aaaamrdefault.x");
        				y = getConfig().getInt("aaaamrdefault.y");
        				z = getConfig().getInt("aaaamrdefault.z");
        				pWorld= getConfig().getString("aaaamrdefault.world");
        			}
            	    getConfig().set(pName+".world", pWorld);
            	    getConfig().set(pName+".x", x);
            	    getConfig().set(pName+".y", y);
            	    getConfig().set(pName+".z", z);
            	    saveConfig();
            	}
            }
        }, 0L, 600L);
	}
 
	@Override
	public void onDisable() {
		saveConfig();
		getLogger().info("Login unfixedly unfixed.");
	}
	
	@EventHandler (priority=EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e) // On join, checks if their node exists. If it exists, checks if they are in a main world. Teleports if ok.
	{
		reloadConfig();
		String pName = e.getPlayer().getName().toString();
		if(nodeExists(getConfig(),pName)){
			int x = this.getConfig().getInt(pName+".x");
			int y = this.getConfig().getInt(pName+".y");
			int z = this.getConfig().getInt(pName+".z");
			String worldString = this.getConfig().getString(pName+".world");
			if((!worldString.equalsIgnoreCase("world"))&&(!worldString.equalsIgnoreCase("world_nether"))&&(!worldString.equalsIgnoreCase("world_end"))){
				x = this.getConfig().getInt("aaaamrdefault.x");
				y = this.getConfig().getInt("aaaamrdefault.y");
				z = this.getConfig().getInt("aaaamrdefault.z");
				worldString= this.getConfig().getString("aaaamrdefault.world");
			}
			World world = getServer().getWorld(worldString);
			Location loc = new Location(world, x, y, z);
			e.getPlayer().teleport(loc);
		}
		
		
	} 
	@EventHandler (priority=EventPriority.LOWEST) // If in a weird myst, sends them to a main spawn
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		Player player = e.getPlayer();
	    String worldString = player.getLocation().getWorld().getName().toString();
	    if((!worldString.equalsIgnoreCase("world"))&&(!worldString.equalsIgnoreCase("world_nether"))&&(!worldString.equalsIgnoreCase("world_end"))){
	    	int sx = this.getConfig().getInt("aaaamrdefault.x");
			int sy = this.getConfig().getInt("aaaamrdefault.y");
			int sz = this.getConfig().getInt("aaaamrdefault.z");
			String sworldString= this.getConfig().getString("aaaamrdefault.world");
			World sWorld = getServer().getWorld(sworldString);
			Location spawn = new Location(sWorld,sx,sy,sz);
			player.teleport(spawn);
	    }
	    else{
	    	String pName = player.getName().toString();
    		String pWorld = player.getWorld().getName().toString();
    		int x = player.getLocation().getBlock().getX();
    	    int y = player.getLocation().getBlock().getY();
    	    int z = player.getLocation().getBlock().getZ();
    	    getConfig().set(pName+".world", pWorld);
    	    getConfig().set(pName+".x", x);
    	    getConfig().set(pName+".y", y);
    	    getConfig().set(pName+".z", z);
    	    saveConfig();
	    }
	}
	public boolean nodeExists(Configuration config, String nodePath){  // Checks if node exists
	    String node = config.getString(nodePath);
	    return ( node != null );
	}
}

