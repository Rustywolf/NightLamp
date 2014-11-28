package codes.rusty.nightlamp.misc;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockPos {

    private final int x;
    private final int y;
    private final int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(Block block) {
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Block getBlock(World world) {
        return world.getBlockAt(x, y, z);
    }

    public Location getLocation(World world) {
        return new Location(world, x, y, z);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        hash = 97 * hash + this.z;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final BlockPos other = (BlockPos) obj;
        if (this.x != other.x) {
            return false;
        }
        
        if (this.y != other.y) {
            return false;
        }
        
        if (this.z != other.z) {
            return false;
        }
        
        return true;
    }

}
