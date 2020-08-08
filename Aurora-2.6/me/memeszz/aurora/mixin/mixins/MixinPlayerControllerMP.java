package me.memeszz.aurora.mixin.mixins;

import me.memeszz.aurora.event.events.EventPlayerDamageBlock;
import me.memeszz.aurora.event.events.EventPlayerClickBlock;
import net.minecraft.util.EnumFacing;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.event.events.EventPlayerResetBlockRemoving;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.memeszz.aurora.event.events.DestroyBlockEvent;
import me.memeszz.aurora.Aurora;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import me.memeszz.aurora.mixin.accessor.IPlayerControllerMP;

@Mixin({ PlayerControllerMP.class })
public abstract class MixinPlayerControllerMP implements IPlayerControllerMP
{
    @Accessor
    @Override
    public abstract void setBlockHitDelay(final int p0);
    
    @Accessor
    @Override
    public abstract void setIsHittingBlock(final boolean p0);
    
    @Accessor
    @Override
    public abstract float getCurBlockDamageMP();
    
    @Accessor
    @Override
    public abstract void setCurBlockDamageMP(final float p0);
    
    @Inject(method = { "onPlayerDestroyBlock" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V") }, cancellable = true)
    private void onPlayerDestroyBlock(final BlockPos pos, final CallbackInfoReturnable<Boolean> info) {
        Aurora.getInstance().getEventManager().dispatchEvent(new DestroyBlockEvent(pos));
    }
    
    @Inject(method = { "resetBlockRemoving" }, at = { @At("HEAD") }, cancellable = true)
    public void resetBlockRemoving(final CallbackInfo p_Info) {
        final EventPlayerResetBlockRemoving l_Event = new EventPlayerResetBlockRemoving();
        Aurora.getInstance().getEventManager().dispatchEvent(l_Event);
        if (l_Event.isCanceled() || ModuleManager.isModuleEnabled("MultiTask")) {
            p_Info.cancel();
        }
    }
    
    @Inject(method = { "clickBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void clickBlock(final BlockPos loc, final EnumFacing face, final CallbackInfoReturnable<Boolean> callback) {
        final EventPlayerClickBlock l_Event = new EventPlayerClickBlock(loc, face);
        Aurora.getInstance().getEventManager().dispatchEvent(l_Event);
        if (l_Event.isCanceled()) {
            callback.setReturnValue(false);
            callback.cancel();
        }
    }
    
    @Inject(method = { "onPlayerDamageBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void onPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing, final CallbackInfoReturnable<Boolean> p_Info) {
        final EventPlayerDamageBlock l_Event = new EventPlayerDamageBlock(posBlock, directionFacing);
        Aurora.getInstance().getEventManager().dispatchEvent(l_Event);
        if (l_Event.isCanceled()) {
            p_Info.setReturnValue(false);
            p_Info.cancel();
        }
    }
}
