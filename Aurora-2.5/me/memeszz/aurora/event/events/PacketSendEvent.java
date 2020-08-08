
package me.memeszz.aurora.event.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketSendEvent extends Event
{
    private final Packet packet;
    
    public PacketSendEvent(final Packet packet) {
        this.packet = packet;
    }
    
    public final Packet getPacket() {
        return this.packet;
    }
    
    public static class Post extends PacketSendEvent
    {
        public Post(final Packet packet) {
            super(packet);
        }
    }
}
