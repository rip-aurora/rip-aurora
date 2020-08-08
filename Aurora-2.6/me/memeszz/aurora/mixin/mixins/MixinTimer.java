package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import me.memeszz.aurora.mixin.accessor.ITimer;

@Mixin({ Timer.class })
public abstract class MixinTimer implements ITimer
{
    @Accessor
    @Override
    public abstract void setTickLength(final float p0);
    
    @Accessor
    @Override
    public abstract float getTickLength();
}
