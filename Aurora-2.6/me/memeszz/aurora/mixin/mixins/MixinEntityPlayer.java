package me.memeszz.aurora.mixin.mixins;

import net.minecraft.entity.MoverType;
import me.memeszz.aurora.event.events.EventPlayerTravel;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.memeszz.aurora.event.events.PlayerJumpEvent;
import me.memeszz.aurora.Aurora;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    @Shadow
    public abstract String getName();
    
    public MixinEntityPlayer(final World worldIn) {
        super(worldIn);
    }
    
    @Inject(method = { "jump" }, at = { @At("HEAD") }, cancellable = true)
    public void onJump(final CallbackInfo ci) {
        if (Minecraft.getMinecraft().player.getName() == this.getName()) {
            Aurora.getInstance().getEventManager().dispatchEvent(new PlayerJumpEvent());
        }
    }
    
    @Inject(method = { "travel" }, at = { @At("HEAD") }, cancellable = true)
    public void travel(final float strafe, final float vertical, final float forward, final CallbackInfo info) {
        final EventPlayerTravel event = new EventPlayerTravel(strafe, vertical, forward);
        Aurora.getInstance().getEventManager().dispatchEvent(new EventPlayerTravel(strafe, vertical, forward));
        if (event.isCanceled()) {
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            info.cancel();
        }
    }
}
