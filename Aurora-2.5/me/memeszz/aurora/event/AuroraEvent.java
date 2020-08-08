
package me.memeszz.aurora.event;

import me.memeszz.aurora.util.Wrapper;
import me.zero.alpine.type.Cancellable;

public class AuroraEvent extends Cancellable
{
    private final Era era;
    private final float partialTicks;
    
    public AuroraEvent() {
        this.era = Era.PRE;
        this.partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }
    
    public Era getEra() {
        return this.era;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public enum Era
    {
        PRE, 
        PERI, 
        POST;
    }
}
