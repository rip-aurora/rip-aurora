
package me.memeszz.aurora.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import me.memeszz.aurora.event.AuroraEvent;

public class EventPlayerClickBlock extends AuroraEvent
{
    public BlockPos Location;
    public EnumFacing Facing;
    
    public EventPlayerClickBlock(final BlockPos loc, final EnumFacing face) {
        this.Location = loc;
        this.Facing = face;
    }
}
