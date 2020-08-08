
package me.memeszz.aurora.event.events;

import net.minecraft.util.math.BlockPos;
import me.memeszz.aurora.event.AuroraEvent;

public class DestroyBlockEvent extends AuroraEvent
{
    BlockPos pos;
    
    public DestroyBlockEvent(final BlockPos blockPos) {
        this.pos = blockPos;
    }
    
    public BlockPos getBlockPos() {
        return this.pos;
    }
}
