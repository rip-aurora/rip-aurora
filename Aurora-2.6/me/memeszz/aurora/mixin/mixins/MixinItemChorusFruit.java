package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.event.events.EventChorusTeleport;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemChorusFruit;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { ItemChorusFruit.class }, priority = 29999)
public abstract class MixinItemChorusFruit
{
    @Inject(method = { "onItemUseFinish" }, at = { @At("HEAD") }, cancellable = true)
    public void onUpdate(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving, final CallbackInfoReturnable<ItemStack> cir) {
        final EventChorusTeleport event = new EventChorusTeleport();
        Aurora.getInstance().getEventManager().dispatchEvent(event);
    }
}
