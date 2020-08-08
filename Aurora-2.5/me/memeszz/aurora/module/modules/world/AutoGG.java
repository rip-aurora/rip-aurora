
package me.memeszz.aurora.module.modules.world;

import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import me.memeszz.aurora.module.Module;

public class AutoGG extends Module
{
    public static AutoGG INSTANCE;
    static List<String> AutoGgMessages;
    private ConcurrentHashMap targetedPlayers;
    int index;
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener;
    @EventHandler
    private final Listener<LivingDeathEvent> livingDeathEventListener;
    
    public AutoGG() {
        super("AutoGG", Category.WORLD, "Sends a message in chat when you kill someone");
        this.targetedPlayers = null;
        this.index = -1;
        CPacketUseEntity cPacketUseEntity;
        Entity targetEntity;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (AutoGG.mc.player != null) {
                if (this.targetedPlayers == null) {
                    this.targetedPlayers = new ConcurrentHashMap();
                }
                if (event.getPacket() instanceof CPacketUseEntity) {
                    cPacketUseEntity = (CPacketUseEntity)event.getPacket();
                    if (cPacketUseEntity.getAction().equals((Object)CPacketUseEntity.Action.ATTACK)) {
                        targetEntity = cPacketUseEntity.getEntityFromWorld((World)AutoGG.mc.world);
                        if (targetEntity instanceof EntityPlayer) {
                            this.addTargetedPlayer(targetEntity.getName());
                        }
                    }
                }
            }
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        EntityLivingBase entity;
        EntityPlayer player;
        String name;
        this.livingDeathEventListener = new Listener<LivingDeathEvent>(event -> {
            if (AutoGG.mc.player != null) {
                if (this.targetedPlayers == null) {
                    this.targetedPlayers = new ConcurrentHashMap();
                }
                entity = event.getEntityLiving();
                if (entity != null && entity instanceof EntityPlayer) {
                    player = (EntityPlayer)entity;
                    if (player.getHealth() <= 0.0f) {
                        name = player.getName();
                        if (this.shouldAnnounce(name)) {
                            this.doAnnounce(name);
                        }
                    }
                }
            }
            return;
        }, (Predicate<LivingDeathEvent>[])new Predicate[0]);
        AutoGG.INSTANCE = this;
    }
    
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap();
    }
    
    public void onDisable() {
        this.targetedPlayers = null;
    }
    
    @Override
    public void onUpdate() {
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
        }
        for (final Entity entity : AutoGG.mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)entity;
                if (player.getHealth() > 0.0f) {
                    continue;
                }
                final String name = player.getName();
                if (this.shouldAnnounce(name)) {
                    this.doAnnounce(name);
                    break;
                }
                continue;
            }
        }
        this.targetedPlayers.forEach((namex, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(namex);
            }
            else {
                this.targetedPlayers.put(namex, timeout - 1);
            }
        });
    }
    
    private boolean shouldAnnounce(final String name) {
        return this.targetedPlayers.containsKey(name);
    }
    
    private void doAnnounce(final String name) {
        this.targetedPlayers.remove(name);
        if (this.index >= AutoGG.AutoGgMessages.size() - 1) {
            this.index = -1;
        }
        ++this.index;
        String message;
        if (AutoGG.AutoGgMessages.size() > 0) {
            message = AutoGG.AutoGgMessages.get(this.index);
        }
        else {
            message = "GET FUCK BY AURORA PUSSY!!!";
        }
        String messageSanitized = message.replaceAll("\u00e0¸¢\u00e0¸\u2021", "").replace("{name}", name);
        if (messageSanitized.length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        AutoGG.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageSanitized));
    }
    
    public void addTargetedPlayer(final String name) {
        if (!Objects.equals(name, AutoGG.mc.player.getName())) {
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ConcurrentHashMap();
            }
            this.targetedPlayers.put(name, 20);
        }
    }
    
    public static void addAutoGgMessage(final String s) {
        AutoGG.AutoGgMessages.add(s);
    }
    
    public static List<String> getAutoGgMessages() {
        return AutoGG.AutoGgMessages;
    }
    
    static {
        AutoGG.AutoGgMessages = new ArrayList<String>();
    }
}
