package me.memeszz.aurora.mixin.accessor;

import net.minecraft.item.ItemStack;

public interface IItemRenderer
{
    float getPrevEquippedProgressMainHand();
    
    void setEquippedProgressMainHand(final float p0);
    
    float getPrevEquippedProgressOffHand();
    
    void setEquippedProgressOffHand(final float p0);
    
    void setItemStackMainHand(final ItemStack p0);
    
    void setItemStackOffHand(final ItemStack p0);
}
