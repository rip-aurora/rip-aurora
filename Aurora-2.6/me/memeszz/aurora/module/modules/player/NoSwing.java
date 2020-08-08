package me.memeszz.aurora.module.modules.player;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.network.play.client.CPacketAnimation;
import me.memeszz.aurora.event.events.PacketEvent;
import me.memeszz.aurora.module.Module;

public class NoSwing extends Module
{
    public NoSwing() {
        super("NoSwing", Category.PLAYER, "Prevents swinging animation server side");
    }
    
    @Listener
    public void onUpdate(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketAnimation) {
            event.setCanceled(true);
        }
    }
}
