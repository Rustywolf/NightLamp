package codes.rusty.nightlamp.commands;

import codes.rusty.nightlamp.NightLamp;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveLamps implements CommandExecutor {
    
    private final NightLamp lamp;
    
    public SaveLamps(NightLamp lamp) {
        this.lamp = lamp;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (lamp.saveLamps()) {
            sender.sendMessage(ChatColor.GREEN + "Lamps saved succesfully.");
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "There was an error saving the lamps.");
            sender.sendMessage(ChatColor.DARK_RED + "Please check the console for more info.");
        }
        return true;
    }
    
}
