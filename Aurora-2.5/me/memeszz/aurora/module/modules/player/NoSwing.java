
package me.memeszz.aurora.module.modules.player;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketAnimation;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.module.Module;

public class NoSwing extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    
    public NoSwing() {
        super("NoSwing", Category.PLAYER, "Prevents swinging animation server side");
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketAnimation) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
