package codes.rusty.nightlamp.tasks;

import codes.rusty.nightlamp.NightLamp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RemoveWaitingClickTask implements Runnable {
    
    private NightLamp plugin;
    private String name;
    
    public RemoveWaitingClickTask(NightLamp plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }
    
    @Override
    public void run() {
        if (plugin.waitingForClick.contains(name)) {
            Player player = Bukkit.getPlayerExact(name);
            player.sendMessage(ChatColor.RED + "Took too long to click a block!");
            plugin.waitingForClick.remove(name);
        }
    }
    
}
