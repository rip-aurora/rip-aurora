
package me.memeszz.aurora.event.events;

import net.minecraft.client.gui.ScaledResolution;
import me.memeszz.aurora.event.AuroraEvent;

public class EventRender2D extends AuroraEvent
{
    private float partialTicks;
    private ScaledResolution scaledResolution;
    
    public EventRender2D(final float partialTicks, final ScaledResolution scaledResolution) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
    }
    
    @Override
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }
    
    public void setScaledResolution(final ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}
