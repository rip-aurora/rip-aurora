
package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import me.memeszz.aurora.event.events.EventPlayerUpdate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.event.events.PlayerMoveEvent;
import net.minecraft.entity.MoverType;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.entity.AbstractClientPlayer;

@Mixin(value = { EntityPlayerSP.class }, priority = 998)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    public MixinEntityPlayerSP() {
        super((World)null, (GameProfile)null);
    }
    
    @Redirect(method = { "move" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(final AbstractClientPlayer player, final MoverType type, final double x, final double y, final double z) {
        final PlayerMoveEvent moveEvent = new PlayerMoveEvent(type, x, y, z);
        Aurora.EVENT_BUS.post(moveEvent);
        super.move(type, moveEvent.x, moveEvent.y, moveEvent.z);
    }
    
    @Inject(method = { "onUpdate" }, at = { @At("HEAD") }, cancellable = true)
    public void onUpdate(final CallbackInfo p_Info) {
        final EventPlayerUpdate l_Event = new EventPlayerUpdate();
        Aurora.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled()) {
            p_Info.cancel();
        }
    }
}
