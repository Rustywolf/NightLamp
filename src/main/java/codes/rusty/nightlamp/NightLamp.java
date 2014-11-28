package codes.rusty.nightlamp;

import codes.rusty.nightlamp.commands.AddLamp;
import codes.rusty.nightlamp.listeners.NightLampListener;
import codes.rusty.nightlamp.misc.BlockPos;
import codes.rusty.nightlamp.tasks.LampSaveTask;
import codes.rusty.nightlamp.tasks.LampSetStateTask;
import codes.rusty.nightlamp.tasks.TimeCheckTask;
import codes.rusty.nightlamp.util.ConfigUtil;
import codes.rusty.nightlamp.util.WorldUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import com.google.common.collect.HashMultimap;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public class NightLamp extends JavaPlugin {
    
    private static NightLamp plugin;
    
    private File configFile;
    
    private NightLampListener listener;
    private TimeCheckTask task;
    private LampSaveTask saveTask;
    
    private HashMultimap<UUID, BlockPos> lampLocations = HashMultimap.create();
    private HashMultimap<World, Block> loadedLamps = HashMultimap.create();
    public HashMap<World, Boolean> isNight = new HashMap<>();
    public ArrayList<String> waitingForClick = new ArrayList<>();
    
    @Override
    public void onEnable() {
        NightLamp.plugin = this;
        
        plugin.saveDefaultConfig();
        configFile = new File(plugin.getDataFolder(), "lamps.data");
        lampLocations = ConfigUtil.getLamps(configFile);
        
        this.getCommand("addlamp").setExecutor(new AddLamp(this));
        
        listener = new NightLampListener(this);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        
        task = new TimeCheckTask(this);
        task.runTaskTimer(this, 1, 5*20);
        
        saveTask = new LampSaveTask(this);
        saveTask.runTaskTimer(plugin, 1, 5*60*20);
        
        for (World world : Bukkit.getWorlds()) {
            loadWorld(world);
        }
        
        log(Level.INFO, "Loaded NightLamp plugin!");
    }
    
    @Override
    public void onDisable() {
        if (task != null) {
            task.cancel();
        }
    }
    
    public void setLampState(World world, boolean on) {
        WorldUtil.setLampState(world, loadedLamps.get(world), on);
    }
    
    public void addBlock(Block block) {
        System.out.println("Placed blocks registered!");
        lampLocations.put(block.getWorld().getUID(), new BlockPos(block));
        loadedLamps.put(block.getWorld(), block);
        
        Bukkit.getScheduler().runTaskLater(plugin, new LampSetStateTask(block, isNight.get(block.getWorld())), 1);
    }
    
    public boolean containsBlock(Block block) {
        return loadedLamps.get(block.getWorld()).contains(block);
    }
    
    public void removeBlock(Block block) {
        BlockPos pos = new BlockPos(block);
        Iterator<BlockPos> it = lampLocations.get(block.getWorld().getUID()).iterator();
        while (it.hasNext()) {
            if (it.next().equals(pos)) {
                it.remove();
            }
        }
        
        Iterator<Block> itt = loadedLamps.get(block.getWorld()).iterator();
        while (itt.hasNext()) {
            if (itt.next().equals(block)) {
                itt.remove();
            }
        }
    }
    
    public void loadWorld(World world) {
        List<Block> blocks = new ArrayList<>();
        for (BlockPos pos : lampLocations.get(world.getUID())) {
            blocks.add(pos.getBlock(world));
        }
        
        loadedLamps.putAll(world, blocks);
        isNight.put(world, false);
        
        WorldUtil.setLampState(world, blocks, false);
    }
    
    public void unloadWorld(World world) {
        loadedLamps.removeAll(world);
        isNight.remove(world);
    }
    
    public boolean saveLamps() {
        return ConfigUtil.saveLamps(configFile, lampLocations);
    }
    
    public static boolean log(Level level, String message) {
        if (NightLamp.plugin == null) {
            return false;
        } else {
            NightLamp.plugin.getLogger().log(level, message);
            return true;
        }
    }
    
}
