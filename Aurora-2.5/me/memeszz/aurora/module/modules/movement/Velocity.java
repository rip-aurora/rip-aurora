
package me.memeszz.aurora.module.modules.movement;

import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.module.Module;

public class Velocity extends Module
{
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener;
    
    public Velocity() {
        super("Velocity", Category.MOVEMENT, "Prevents you from taking knockback");
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                event.cancel();
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
}
