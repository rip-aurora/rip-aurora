package me.memeszz.aurora.module.modules.combat;

import net.minecraft.entity.Entity;
import me.memeszz.aurora.Aurora;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import me.memeszz.aurora.event.events.PacketEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import me.memeszz.aurora.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.util.friends.Friends;
import me.memeszz.aurora.event.events.TotemPopEvent;
import java.util.HashMap;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class TotemPopCounter extends Module
{
    private final Setting.b friend;
    private HashMap<String, Integer> popList;
    
    public TotemPopCounter() {
        super("PopCounter", Category.COMBAT, "Alerts When A Player Pops A Totem");
        this.popList = new HashMap<String, Integer>();
        this.friend = this.registerB("AlertFriends", true);
    }
    
    @Listener
    public void onUpdate(final TotemPopEvent event) {
        if (TotemPopCounter.mc.player == null || TotemPopCounter.mc.world == null) {
            return;
        }
        if (this.popList == null) {
            this.popList = new HashMap<String, Integer>();
        }
        if (!this.friend.getValue()) {
            if (!Friends.isFriend(event.getEntity().getName())) {
                if (this.popList.get(event.getEntity().getName()) == null) {
                    this.popList.put(event.getEntity().getName(), 1);
                    Command.sendClientMessage(ChatFormatting.RED + event.getEntity().getName() + " popped " + ChatFormatting.YELLOW + 1 + ChatFormatting.RED + " totem!");
                }
            }
            else if (this.popList.get(event.getEntity().getName()) != null) {
                int popCounter = this.popList.get(event.getEntity().getName());
                final int newPopCounter = ++popCounter;
                this.popList.put(event.getEntity().getName(), newPopCounter);
                Command.sendClientMessage(ChatFormatting.RED + event.getEntity().getName() + ChatFormatting.RED + " popped " + ChatFormatting.YELLOW + newPopCounter + ChatFormatting.RED + " totems!");
            }
        }
        if (this.friend.getValue()) {
            if (this.popList.get(event.getEntity().getName()) == null) {
                this.popList.put(event.getEntity().getName(), 1);
                Command.sendClientMessage(ChatFormatting.RED + event.getEntity().getName() + ChatFormatting.RED + " popped " + ChatFormatting.YELLOW + 1 + ChatFormatting.RED + " totem!");
            }
            else if (this.popList.get(event.getEntity().getName()) != null) {
                int popCounter = this.popList.get(event.getEntity().getName());
                final int newPopCounter = ++popCounter;
                this.popList.put(event.getEntity().getName(), newPopCounter);
                Command.sendClientMessage(ChatFormatting.RED + event.getEntity().getName() + ChatFormatting.RED + " popped " + ChatFormatting.YELLOW + newPopCounter + ChatFormatting.RED + " totems!");
            }
        }
    }
    
    @Listener
    @Override
    public void onUpdate() {
        for (final EntityPlayer player : TotemPopCounter.mc.world.playerEntities) {
            if (player.getHealth() <= 0.0f && this.popList.containsKey(player.getName())) {
                Command.sendClientMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + this.popList.get(player.getName()) + ChatFormatting.RED + " totems!");
                this.popList.remove(player.getName(), this.popList.get(player.getName()));
            }
        }
    }
    
    @Listener
    public void onUpdate(final PacketEvent.Receive event) {
        if (TotemPopCounter.mc.world == null || TotemPopCounter.mc.player == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getOpCode() == 35) {
                final Entity entity = packet.getEntity((World)TotemPopCounter.mc.world);
                Aurora.getInstance().getEventManager().dispatchEvent(new TotemPopEvent(entity));
            }
        }
    }
}
