package codes.rusty.nightlamp.listeners;

import codes.rusty.nightlamp.NightLamp;
import codes.rusty.nightlamp.tasks.LampSetStateTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class NightLampListener implements Listener {
    
    private NightLamp plugin;
    
    public NightLampListener(NightLamp plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent event) {
        plugin.loadWorld(event.getWorld());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        plugin.unloadWorld(event.getWorld());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent event) {
        plugin.saveLamps();
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (b.getType() == Material.REDSTONE_LAMP_OFF || b.getType() == Material.REDSTONE_LAMP_ON) {
            if (plugin.containsBlock(b)) {
                plugin.removeBlock(b);
                event.getPlayer().sendMessage(ChatColor.GREEN + "NightLamp removed!");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = event.getClickedBlock();
            if (b.getType() == Material.REDSTONE_LAMP_OFF || b.getType() == Material.REDSTONE_LAMP_ON) {
                if (plugin.waitingForClick.contains(event.getPlayer().getName())) {
                    event.setCancelled(true);
                    plugin.addBlock(b);
                    plugin.waitingForClick.remove(event.getPlayer().getName());
                    event.getPlayer().sendMessage(ChatColor.GREEN + "NightLamp added!");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.REDSTONE_LAMP_OFF || block.getType() == Material.REDSTONE_LAMP_ON) {
            if (plugin.containsBlock(block)) {
                Bukkit.getScheduler().runTaskLater(plugin, new LampSetStateTask(block, plugin.isNight.get(block.getWorld())), 1);
                Bukkit.getScheduler().runTaskLater(plugin, new LampSetStateTask(block, plugin.isNight.get(block.getWorld())), 5);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRedstone(BlockRedstoneEvent event) {
        if (plugin.containsBlock(event.getBlock())) {
            event.setNewCurrent(5);
        }
    }
}
