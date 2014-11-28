package codes.rusty.nightlamp.tasks;

import codes.rusty.nightlamp.NightLamp;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeCheckTask extends BukkitRunnable {
    
    private NightLamp plugin;
    
    public TimeCheckTask(NightLamp plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            long dayTime = (world.getTime() % 24000);
            boolean night = dayTime > 13000;
                
            if (!plugin.isNight.containsKey(world)) {
                plugin.isNight.put(world, night);
                plugin.setLampState(world, night);
            } else {
                boolean lastUpdate = plugin.isNight.get(world);
                if (lastUpdate != night) {
                    plugin.isNight.put(world, night);
                    plugin.setLampState(world, night);
                }
            }
        }
    }
    
}
