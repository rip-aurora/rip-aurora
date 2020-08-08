
package me.memeszz.aurora.mixin.mixins;

import net.minecraft.util.text.ITextComponent;
import me.memeszz.aurora.util.misc.TickRate;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.event.events.PacketEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetworkManager.class })
public abstract class MixinNetworkManager
{
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onSendPacket(final Packet<?> packet, final CallbackInfo callbackInfo) {
        final PacketEvent.Send event = new PacketEvent.Send(packet);
        Aurora.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void onChannelRead(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        TickRate.update(packet);
        final PacketEvent.Receive event = new PacketEvent.Receive(packet);
        Aurora.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "closeChannel" }, at = { @At("HEAD") })
    public void preCloseChannel(final ITextComponent message, final CallbackInfo callbackInfo) {
        TickRate.reset();
    }
}
