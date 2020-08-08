package me.memeszz.aurora.module.modules.movement;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import me.memeszz.aurora.event.events.PacketEvent;
import me.memeszz.aurora.module.Module;

public class Velocity extends Module
{
    public Velocity() {
        super("Velocity", Category.MOVEMENT, "Prevents you from taking knockback");
    }
    
    @Listener
    public void listener(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            event.setCanceled(true);
        }
    }
}
