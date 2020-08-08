package me.memeszz.aurora.event.events;

import net.minecraft.util.math.BlockPos;

public class DestroyBlockEvent
{
    BlockPos pos;
    
    public DestroyBlockEvent(final BlockPos blockPos) {
        this.pos = blockPos;
    }
    
    public BlockPos getBlockPos() {
        return this.pos;
    }
}
