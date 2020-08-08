
package me.memeszz.aurora.module.modules.player;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.module.Module;

public class PortalGodMode extends Module
{
    @EventHandler
    private final Listener<PacketEvent.Send> listener;
    
    public PortalGodMode() {
        super("PortalGodmode", Category.PLAYER, "Godmode when you go through a portal");
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketConfirmTeleport) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
