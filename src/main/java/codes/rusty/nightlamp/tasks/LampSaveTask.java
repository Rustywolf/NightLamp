package codes.rusty.nightlamp.tasks;

import codes.rusty.nightlamp.NightLamp;
import org.bukkit.scheduler.BukkitRunnable;

public class LampSaveTask extends BukkitRunnable {
    
    private NightLamp plugin;
    
    public LampSaveTask(NightLamp plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        plugin.saveLamps();
    }
    
}
