
package me.memeszz.aurora.event.events;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.Tessellator;
import me.memeszz.aurora.event.AuroraEvent;

public class RenderEvent extends AuroraEvent
{
    private final Tessellator tessellator;
    private final Vec3d renderPos;
    private final float partialTicks;
    
    public RenderEvent(final Tessellator tessellator, final Vec3d renderPos, final float ticks) {
        this.tessellator = tessellator;
        this.renderPos = renderPos;
        this.partialTicks = ticks;
    }
    
    public Tessellator getTessellator() {
        return this.tessellator;
    }
    
    public BufferBuilder getBuffer() {
        return this.tessellator.getBuffer();
    }
    
    public Vec3d getRenderPos() {
        return this.renderPos;
    }
    
    public void setTranslation(final Vec3d translation) {
        this.getBuffer().setTranslation(-translation.x, -translation.y, -translation.z);
    }
    
    public void resetTranslation() {
        this.setTranslation(this.renderPos);
    }
    
    @Override
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
