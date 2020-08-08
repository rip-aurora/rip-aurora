package me.memeszz.aurora.mixin.mixins;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import me.memeszz.aurora.mixin.accessor.IItemRenderer;

@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer implements IItemRenderer
{
    @Accessor
    @Override
    public abstract float getPrevEquippedProgressMainHand();
    
    @Accessor
    @Override
    public abstract void setEquippedProgressMainHand(final float p0);
    
    @Accessor
    @Override
    public abstract float getPrevEquippedProgressOffHand();
    
    @Accessor
    @Override
    public abstract void setEquippedProgressOffHand(final float p0);
    
    @Accessor
    @Override
    public abstract void setItemStackMainHand(final ItemStack p0);
    
    @Accessor
    @Override
    public abstract void setItemStackOffHand(final ItemStack p0);
}
