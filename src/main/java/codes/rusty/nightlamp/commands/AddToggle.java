package codes.rusty.nightlamp.commands;

import codes.rusty.nightlamp.NightLamp;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddToggle implements CommandExecutor {
    
    private final NightLamp plugin;
    
    public AddToggle(NightLamp plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be used by players!");
        } else {
            Player player = (Player) sender;
            if (plugin.waitingForClick.containsKey(player.getName()) && plugin.waitingForClick.get(player.getName())) {
                plugin.waitingForClick.remove(player.getName());
                sender.sendMessage(ChatColor.GREEN + "Your ability to add lamps has been " + ChatColor.DARK_RED + "disabled");
            } else {
                plugin.waitingForClick.put(player.getName(), Boolean.TRUE);
                sender.sendMessage(ChatColor.GREEN + "Your ability to add lamps has been " + ChatColor.DARK_GREEN + "enabled");
            }
        }
        
        return true;
    }
    
}
