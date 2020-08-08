package me.memeszz.aurora.module.modules.player;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.memeszz.aurora.event.events.PacketEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.Packet;
import java.util.Queue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.memeszz.aurora.module.Module;

public class Blink extends Module
{
    EntityOtherPlayerMP entity;
    private final Queue<Packet> packets;
    
    public Blink() {
        super("Blink", Category.PLAYER, "Cancels most packets");
        this.packets = new ConcurrentLinkedQueue<Packet>();
    }
    
    @Listener
    public void onUpdate(final PacketEvent.Send event) {
        final Packet packet = event.getPacket();
        if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
            return;
        }
        this.packets.add(packet);
        event.setCanceled(true);
    }
    
    public void onEnable() {
        (this.entity = new EntityOtherPlayerMP((World)Blink.mc.world, Blink.mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)Blink.mc.player);
        this.entity.rotationYaw = Blink.mc.player.rotationYaw;
        this.entity.rotationYawHead = Blink.mc.player.rotationYawHead;
        Blink.mc.world.addEntityToWorld(666, (Entity)this.entity);
    }
    
    public void onDisable() {
        if (this.entity != null) {
            Blink.mc.world.removeEntity((Entity)this.entity);
        }
        if (this.packets.size() > 0) {
            for (final Packet packet : this.packets) {
                Blink.mc.player.connection.sendPacket(packet);
            }
            this.packets.clear();
        }
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.packets.size() + "§7]";
    }
}
