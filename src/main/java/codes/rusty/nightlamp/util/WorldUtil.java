package codes.rusty.nightlamp.util;

import com.google.common.collect.HashMultimap;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WorldUtil {
    
    private final static String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
    
    public static boolean setLampState(Block block, boolean on) {
        Material to = (on) ? Material.REDSTONE_LAMP_ON : Material.REDSTONE_LAMP_OFF;
        
        if (block.getType() == Material.REDSTONE_LAMP_ON || block.getType() == Material.REDSTONE_LAMP_OFF) {
            Object worldServer = WorldUtil.getWorldServer(block.getWorld());
            boolean isStatic = getStatic(worldServer);
            
            if (!isStatic) {
                setStatic(worldServer, true);
            }
            
            block.setType(to);
            
            if (!isStatic) {
                setStatic(worldServer, false);
            }
        }
        
        return true;
    }
    
    public static boolean setLampState(World world, Collection<Block> blocks, boolean on) {
        Material to = (on) ? Material.REDSTONE_LAMP_ON : Material.REDSTONE_LAMP_OFF;
        
        Object worldServer = WorldUtil.getWorldServer(world);
        boolean isStatic = getStatic(worldServer);

        if (!isStatic) {
            setStatic(worldServer, true);
        }

        for (Block b : blocks) {
            if (b.getType() == Material.REDSTONE_LAMP_ON || b.getType() == Material.REDSTONE_LAMP_OFF) {
                b.setType(to);
            }
        }

        if (!isStatic) {
            setStatic(worldServer, false);
        }
        
        return true;
    }
    
    public static boolean setLampState(HashMultimap<World, Block> blocks, boolean on) {
        Material to = (on) ? Material.REDSTONE_LAMP_ON : Material.REDSTONE_LAMP_OFF;
        
        for (World world : blocks.keySet()) {
            Object worldServer = WorldUtil.getWorldServer(world);
            boolean isStatic = getStatic(worldServer);
            
            if (!isStatic) {
                setStatic(worldServer, true);
            }
            
            for (Block b : blocks.get(world)) {
                if (b.getType() == Material.REDSTONE_LAMP_ON || b.getType() == Material.REDSTONE_LAMP_OFF) {
                    b.setType(to);
                }
            }
            
            if (!isStatic) {
                setStatic(worldServer, false);
            }
        }
        
        return true;
    }
    
    public static Object getWorldServer(World world) {
        try {
            Class craftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
            Class worldServer = Class.forName("net.minecraft.server." + version + ".WorldServer");
            
            Object craftWorldInstance = craftWorld.cast(world);
            Object worldServerInstance = worldServer.cast(craftWorld.getMethod("getHandle").invoke(craftWorldInstance));
            
            return worldServerInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private static boolean getStatic(Object worldServer) {
        try {
            return worldServer.getClass().getField("isStatic").getBoolean(worldServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private static void setStatic(Object worldServer, boolean statix) {
        try {
           worldServer.getClass().getField("isStatic").setBoolean(worldServer, statix);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
