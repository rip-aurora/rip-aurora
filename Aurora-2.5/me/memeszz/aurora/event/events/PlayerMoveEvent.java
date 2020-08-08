
package me.memeszz.aurora.event.events;

import net.minecraft.entity.MoverType;
import me.memeszz.aurora.event.AuroraEvent;

public class PlayerMoveEvent extends AuroraEvent
{
    MoverType type;
    public double x;
    public double y;
    public double z;
    
    public PlayerMoveEvent(final MoverType moverType, final double xx, final double yy, final double zz) {
        this.type = moverType;
        this.x = xx;
        this.y = yy;
        this.z = zz;
    }
    
    public MoverType getType() {
        return this.type;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setX(final double xx) {
        this.x = xx;
    }
    
    public void setY(final double yy) {
        this.y = yy;
    }
    
    public void setZ(final double zz) {
        this.z = zz;
    }
}
