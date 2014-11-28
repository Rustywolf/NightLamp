package codes.rusty.nightlamp.tasks;

import codes.rusty.nightlamp.util.WorldUtil;
import org.bukkit.block.Block;

public class LampSetStateTask implements Runnable {
    
    private Block block;
    private boolean on;
    
    public LampSetStateTask(Block block, Boolean on) {
        this.block = block;
        this.on = on;
    }

    @Override
    public void run() {
        WorldUtil.setLampState(block, on);
    }
    
}
