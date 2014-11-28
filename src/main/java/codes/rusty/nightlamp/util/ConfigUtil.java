package codes.rusty.nightlamp.util;

import codes.rusty.nightlamp.NightLamp;
import codes.rusty.nightlamp.misc.BlockPos;
import com.google.common.collect.HashMultimap;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;
import java.util.logging.Level;

public class ConfigUtil {
    
    public static HashMultimap<UUID, BlockPos> getLamps(File file) {
        HashMultimap<UUID, BlockPos> blocks = HashMultimap.create();
        
        try {
            if (!file.exists()) {
                NightLamp.log(Level.INFO, "Unable to find block database file. Creating new...");
                file.createNewFile();
                return HashMultimap.create();
            }
            
            UUID currentWorld = null;
            
            if (file.isFile() && file.canRead()) {
                DataInputStream dis = new DataInputStream(new FileInputStream(file));
                
                while (true) {
                    int marker = dis.read();
                    
                    //End of File marker
                    if (marker == 2 || marker == -1) {
                        break;
                    }
                    
                    //World def marker
                    if (marker == 0) {
                        long mostSigBytes = dis.readLong();
                        long leastSigBytes = dis.readLong();
                        currentWorld = new UUID(mostSigBytes, leastSigBytes);
                    }
                    
                    //Block def marker
                    if (marker == 1) {
                        int x = dis.readInt();
                        int y = dis.readInt();
                        int z = dis.readInt();
                        BlockPos b = new BlockPos(x, y, z);
                        blocks.put(currentWorld, b);
                    }
                }
                
                dis.close();
                
                NightLamp.log(Level.INFO, "Block database loaded succesfully!");
                return blocks;
            } else {
                NightLamp.log(Level.INFO, "Unable to load block database file. (Check permissions)");
                return HashMultimap.create();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        NightLamp.log(Level.INFO, "The block database appears to be corrupted. Loaded as much as possible.");
        return HashMultimap.create();
    }
    
    public static boolean saveLamps(File file, HashMultimap<UUID, BlockPos> blocks) {
        
        try {
            if (!file.exists()) {
                if(!file.createNewFile()) {
                    NightLamp.log(Level.INFO, "The block database could not be saved (Cannot create file).");
                    return false;
                }
            }
            
            if (file.isFile() && file.canWrite()) {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
                
                for (UUID world : blocks.keySet()) {
                    dos.write(0);
                    dos.writeLong(world.getMostSignificantBits());
                    dos.writeLong(world.getLeastSignificantBits());
                    
                    for (BlockPos b : blocks.get(world)) {
                        dos.write(1);
                        dos.writeInt(b.getX());
                        dos.writeInt(b.getY());
                        dos.writeInt(b.getZ());
                    }
                }
                
                dos.write(2);
                dos.flush();
                dos.close();
                
                NightLamp.log(Level.INFO, "The block database has been saved succesfully.");
                return true;
            } else {
                NightLamp.log(Level.INFO, "The block database could not be written to (Check permissions).");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        NightLamp.log(Level.INFO, "There was an error saving the block database. Attempt to save again.");
        return false;
    }
    
}
