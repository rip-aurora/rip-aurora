
package me.memeszz.aurora.module.modules.combat;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.Aurora;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import java.util.function.Predicate;
import me.memeszz.aurora.command.Command;
import me.memeszz.aurora.util.friends.Friends;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.TotemPopEvent;
import me.zero.alpine.listener.Listener;
import java.util.Collection;
import java.util.HashMap;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class TotemPops extends Module
{
    private Setting.b msg;
    private Setting.b friend;
    private final Setting.mode mode;
    private Setting.i msgDelaySlider;
    private HashMap<String, Integer> popList;
    public static Collection<Integer> amountPoped;
    public static String playernamePop;
    private int msgDelay;
    @EventHandler
    public Listener<TotemPopEvent> totemPopEvent;
    @EventHandler
    public Listener<PacketEvent.Receive> totemPopListener;
    
    public TotemPops() {
        super("PopCounter", Category.COMBAT, "Alerts When A Player Pops A Totem");
        this.popList = new HashMap<String, Integer>();
        int popCounter;
        int newPopCounter;
        int popCounter2;
        int newPopCounter2;
        this.totemPopEvent = new Listener<TotemPopEvent>(event -> {
            if (TotemPops.mc.player == null || TotemPops.mc.world == null) {
                return;
            }
            else {
                if (this.popList == null) {
                    this.popList = new HashMap<String, Integer>();
                }
                if (!this.friend.getValue() && !Friends.isFriend(event.getEntity().getName())) {
                    if (this.popList.get(event.getEntity().getName()) == null) {
                        this.popList.put(event.getEntity().getName(), 1);
                        Command.sendClientMessage(this.colorchoice() + event.getEntity().getName() + " popped " + 1 + " totem!");
                        if (this.msg.getValue() && this.msgDelay > this.msgDelaySlider.getValue() * 20 && !event.getEntity().getName().equals(TotemPops.mc.player.getName())) {
                            TotemPops.mc.player.sendChatMessage("/msg " + event.getEntity().getName() + " You're Poppin!");
                            this.msgDelay = 0;
                        }
                    }
                    else if (this.popList.get(event.getEntity().getName()) != null) {
                        popCounter = this.popList.get(event.getEntity().getName());
                        newPopCounter = ++popCounter;
                        this.popList.put(event.getEntity().getName(), newPopCounter);
                        Command.sendClientMessage(this.colorchoice() + event.getEntity().getName() + " popped " + newPopCounter + " totems!");
                        if (this.msg.getValue() && this.msgDelay > this.msgDelaySlider.getValue() * 20 && !event.getEntity().getName().equals(TotemPops.mc.player.getName())) {
                            TotemPops.mc.player.sendChatMessage("/msg " + event.getEntity().getName() + " You're Poppin!");
                            this.msgDelay = 0;
                        }
                    }
                }
                if (this.friend.getValue()) {
                    if (this.popList.get(event.getEntity().getName()) == null) {
                        this.popList.put(event.getEntity().getName(), 1);
                        Command.sendClientMessage(this.colorchoice() + event.getEntity().getName() + " popped " + 1 + " totem!");
                        if (this.msg.getValue() && this.msgDelay > this.msgDelaySlider.getValue() * 20 && !event.getEntity().getName().equals(TotemPops.mc.player.getName())) {
                            if (Friends.isFriend(event.getEntity().getName())) {
                                TotemPops.mc.player.sendChatMessage("/msg " + event.getEntity().getName() + " You Popped Stay Safe Friend");
                                this.msgDelay = 0;
                            }
                            if (!Friends.isFriend(event.getEntity().getName())) {
                                TotemPops.mc.player.sendChatMessage("/msg " + event.getEntity().getName() + " You're Poppin!");
                                this.msgDelay = 0;
                            }
                        }
                    }
                    else if (this.popList.get(event.getEntity().getName()) != null) {
                        popCounter2 = this.popList.get(event.getEntity().getName());
                        newPopCounter2 = ++popCounter2;
                        this.popList.put(event.getEntity().getName(), newPopCounter2);
                        Command.sendClientMessage(this.colorchoice() + event.getEntity().getName() + " popped " + newPopCounter2 + " totems!");
                        if (this.msg.getValue() && this.msgDelay > this.msgDelaySlider.getValue() * 20 && !event.getEntity().getName().equals(TotemPops.mc.player.getName())) {
                            if (Friends.isFriend(event.getEntity().getName())) {
                                TotemPops.mc.player.sendChatMessage("/msg " + event.getEntity().getName() + " You Popped Stay Safe Friend");
                                this.msgDelay = 0;
                            }
                            if (!Friends.isFriend(event.getEntity().getName())) {
                                TotemPops.mc.player.sendChatMessage("/msg " + event.getEntity().getName() + " You're Poppin!");
                                this.msgDelay = 0;
                            }
                        }
                    }
                }
                return;
            }
        }, (Predicate<TotemPopEvent>[])new Predicate[0]);
        SPacketEntityStatus packet;
        Entity entity;
        this.totemPopListener = new Listener<PacketEvent.Receive>(event -> {
            if (TotemPops.mc.world == null || TotemPops.mc.player == null) {
                return;
            }
            else {
                if (event.getPacket() instanceof SPacketEntityStatus) {
                    packet = (SPacketEntityStatus)event.getPacket();
                    if (packet.getOpCode() == 35) {
                        entity = packet.getEntity((World)TotemPops.mc.world);
                        Aurora.EVENT_BUS.post(new TotemPopEvent(entity));
                    }
                }
                return;
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.msg = this.registerB("Message", false);
        this.friend = this.registerB("AlertFriends", true);
        this.msgDelaySlider = this.registerI("DelaySlider", 4, 0, 20);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("BLACK");
        modes.add("RED");
        modes.add("AQUA");
        modes.add("BLUE");
        modes.add("GOLD");
        modes.add("GRAY");
        modes.add("WHITE");
        modes.add("GREEN");
        modes.add("YELLOW");
        modes.add("DARK_RED");
        modes.add("DARK_AQUA");
        modes.add("DARK_BLUE");
        modes.add("DARK_GRAY");
        modes.add("DARK_GREEN");
        modes.add("DARK_PURPLE");
        modes.add("LIGHT_PURPLE");
        this.mode = this.registerMode("Mode", modes, "RED");
    }
    
    @Override
    public void onUpdate() {
        TotemPops.playernamePop = this.popList.toString();
        TotemPops.amountPoped = this.popList.values();
        ++this.msgDelay;
        for (final EntityPlayer player : TotemPops.mc.world.playerEntities) {
            if (player.getHealth() <= 0.0f && this.popList.containsKey(player.getName())) {
                Command.sendClientMessage(this.colorchoice() + player.getName() + " died after popping " + this.popList.get(player.getName()) + " totems!");
                this.popList.remove(player.getName(), this.popList.get(player.getName()));
                if (!this.msg.getValue() || this.msgDelay <= this.msgDelaySlider.getValue() * 20 || player.getName() == TotemPops.mc.player.getName()) {
                    continue;
                }
                if (!Friends.isFriend(player.getName())) {
                    TotemPops.mc.player.sendChatMessage("/msg " + player.getName() + " You Died!");
                    this.msgDelay = 0;
                }
                if (!Friends.isFriend(player.getName())) {
                    continue;
                }
                TotemPops.mc.player.sendChatMessage("/msg " + player.getName() + " My nigga you died! ");
                this.msgDelay = 0;
            }
        }
    }
    
    @Override
    public void onRender() {
    }
    
    private String colorchoice() {
        final String value = this.mode.getValue();
        switch (value) {
            case "BLACK": {
                return "§0";
            }
            case "RED": {
                return "§c";
            }
            case "AQUA": {
                return "§b";
            }
            case "BLUE": {
                return "§9";
            }
            case "GOLD": {
                return "§6";
            }
            case "GRAY": {
                return "§7";
            }
            case "WHITE": {
                return "§f";
            }
            case "GREEN": {
                return "§a";
            }
            case "YELLOW": {
                return "§e";
            }
            case "DARK_RED": {
                return "§4";
            }
            case "DARK_AQUA": {
                return "§3";
            }
            case "DARK_BLUE": {
                return "§1";
            }
            case "DARK_GRAY": {
                return "§8";
            }
            case "DARK_GREEN": {
                return "§2";
            }
            case "DARK_PURPLE": {
                return "§5";
            }
            case "LIGHT_PURPLE": {
                return "§d";
            }
            default: {
                return "";
            }
        }
    }
}
