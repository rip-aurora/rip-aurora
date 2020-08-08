package me.memeszz.aurora.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import me.memeszz.aurora.event.EventCancellable;

public class EventPlayerDamageBlock extends EventCancellable
{
    private final BlockPos BlockPos;
    private EnumFacing Direction;
    
    public EventPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing) {
        this.BlockPos = posBlock;
        this.setDirection(directionFacing);
    }
    
    public BlockPos getPos() {
        return this.BlockPos;
    }
    
    public EnumFacing getDirection() {
        return this.Direction;
    }
    
    public void setDirection(final EnumFacing direction) {
        this.Direction = direction;
    }
}
