package codes.rusty.nightlamp.commands;

import codes.rusty.nightlamp.NightLamp;
import codes.rusty.nightlamp.tasks.RemoveWaitingClickTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddLamp implements CommandExecutor {
    
    private final NightLamp plugin;
    
    public AddLamp(NightLamp plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.waitingForClick.put(player.getName(), false);
            Bukkit.getScheduler().runTaskLater(plugin, new RemoveWaitingClickTask(plugin, player.getName()), 5*20);
            sender.sendMessage(ChatColor.GREEN + "Click the block you wish to add within the next 5 seconds.");
            return true;
        }
        
        sender.sendMessage(ChatColor.DARK_RED + "Only players can use this command!");
        return false;
    }
    
}
