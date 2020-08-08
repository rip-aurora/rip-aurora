package me.memeszz.aurora.module.modules.player;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import me.memeszz.aurora.event.events.PacketEvent;
import me.memeszz.aurora.module.Module;

public class PortalGodMode extends Module
{
    public PortalGodMode() {
        super("PortalGodmode", Category.PLAYER, "Godmode when you go through a portal");
    }
    
    @Listener
    public void onUpdate(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
}
