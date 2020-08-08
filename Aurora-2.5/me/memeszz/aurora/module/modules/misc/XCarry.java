
package me.memeszz.aurora.module.modules.misc;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketCloseWindow;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.module.Module;

public class XCarry extends Module
{
    @EventHandler
    private final Listener<PacketEvent.Send> listener;
    
    public XCarry() {
        super("XCarry", Category.MISC, "lets you carry items in your crafting slots");
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketCloseWindow && ((CPacketCloseWindow)event.getPacket()).windowId == XCarry.mc.player.inventoryContainer.windowId) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
