package me.memeszz.aurora.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import me.memeszz.aurora.event.EventCancellable;

public class EventPlayerClickBlock extends EventCancellable
{
    public BlockPos Location;
    public EnumFacing Facing;
    
    public EventPlayerClickBlock(final BlockPos loc, final EnumFacing face) {
        this.Location = loc;
        this.Facing = face;
    }
}
