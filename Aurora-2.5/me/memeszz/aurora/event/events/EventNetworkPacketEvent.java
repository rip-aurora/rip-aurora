
package me.memeszz.aurora.event.events;

import net.minecraft.network.Packet;
import me.memeszz.aurora.event.AuroraEvent;

public class EventNetworkPacketEvent extends AuroraEvent
{
    public Packet m_Packet;
    
    public EventNetworkPacketEvent(final Packet p_Packet) {
        this.m_Packet = p_Packet;
    }
    
    public Packet GetPacket() {
        return this.m_Packet;
    }
    
    public Packet getPacket() {
        return this.m_Packet;
    }
}
