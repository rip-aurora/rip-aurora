package me.memeszz.aurora.module.modules.render;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.entity.Entity;
import me.memeszz.aurora.event.events.UpdateEvent;
import net.minecraft.entity.passive.EntityTameable;
import me.memeszz.aurora.util.math.MathUtil;
import net.minecraft.entity.passive.AbstractHorse;
import java.util.Iterator;
import java.util.Objects;
import me.memeszz.aurora.util.entity.EntityUtil;
import java.util.HashMap;
import java.util.Map;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class MobOwner extends Module
{
    Setting.b speed;
    Setting.b jump;
    Setting.b hp;
    Setting.i requestTime;
    private final Map<String, String> cachedUUIDs;
    private int apiRequests;
    private final String invalidText = "Offline or invalid UUID!";
    private static long startTime;
    private static long startTime1;
    
    public MobOwner() {
        super("MobOwner", Category.RENDER, "Draws a box around entities");
        this.cachedUUIDs = new HashMap<String, String>() {};
        this.apiRequests = 0;
        this.speed = this.registerB("Speed", true);
        this.jump = this.registerB("Jump", false);
        this.hp = this.registerB("Health", false);
        this.requestTime = this.registerI("ResetData", 20, 10, 20);
    }
    
    private String getUsername(final String uuid) {
        for (final Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
            if (entries.getKey().equalsIgnoreCase(uuid)) {
                return entries.getValue();
            }
        }
        try {
            try {
                if (this.apiRequests > 10) {
                    return "Too many API requests";
                }
                this.cachedUUIDs.put(uuid, Objects.requireNonNull(EntityUtil.getNameFromUUID(uuid)).replace("\"", ""));
                ++this.apiRequests;
            }
            catch (IllegalStateException illegal) {
                this.cachedUUIDs.put(uuid, "Offline or invalid UUID!");
            }
        }
        catch (NullPointerException e) {
            this.cachedUUIDs.put(uuid, "Offline or invalid UUID!");
        }
        for (final Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
            if (entries.getKey().equalsIgnoreCase(uuid)) {
                return entries.getValue();
            }
        }
        return "Offline or invalid UUID!";
    }
    
    private void resetCache() {
        if (MobOwner.startTime == 0L) {
            MobOwner.startTime = System.currentTimeMillis();
        }
        if (MobOwner.startTime + this.requestTime.getValue() * 1000 <= System.currentTimeMillis()) {
            MobOwner.startTime = System.currentTimeMillis();
            for (final Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
                if (entries.getKey().equalsIgnoreCase("Offline or invalid UUID!")) {
                    this.cachedUUIDs.clear();
                }
            }
        }
    }
    
    private void resetRequests() {
        if (MobOwner.startTime1 == 0L) {
            MobOwner.startTime1 = System.currentTimeMillis();
        }
        if (MobOwner.startTime1 + 10000L <= System.currentTimeMillis()) {
            MobOwner.startTime1 = System.currentTimeMillis();
            if (this.apiRequests >= 2) {
                this.apiRequests = 0;
            }
        }
    }
    
    private String getSpeed(final AbstractHorse horse) {
        if (!this.speed.getValue()) {
            return "";
        }
        return " S: " + MathUtil.round(43.17 * horse.getAIMoveSpeed(), 2);
    }
    
    private String getJump(final AbstractHorse horse) {
        if (!this.jump.getValue()) {
            return "";
        }
        return " J: " + MathUtil.round(-0.1817584952 * Math.pow(horse.getHorseJumpStrength(), 3.0) + 3.689713992 * Math.pow(horse.getHorseJumpStrength(), 2.0) + 2.128599134 * horse.getHorseJumpStrength() - 0.343930367, 2);
    }
    
    private String getHealth(final AbstractHorse horse) {
        if (!this.hp.getValue()) {
            return "";
        }
        return " HP: " + MathUtil.round(horse.getHealth(), 2);
    }
    
    private String getHealth(final EntityTameable tameable) {
        if (!this.hp.getValue()) {
            return "";
        }
        return " HP: " + MathUtil.round(tameable.getHealth(), 2);
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        this.resetRequests();
        this.resetCache();
        for (final Entity entity : MobOwner.mc.world.loadedEntityList) {
            if (entity instanceof EntityTameable) {
                final EntityTameable entityTameable = (EntityTameable)entity;
                if (entityTameable.isTamed() && entityTameable.getOwner() != null) {
                    entityTameable.setAlwaysRenderNameTag(true);
                    entityTameable.setCustomNameTag("Owner: " + entityTameable.getOwner().getDisplayName().getFormattedText() + this.getHealth(entityTameable));
                }
            }
            if (entity instanceof AbstractHorse) {
                final AbstractHorse abstractHorse = (AbstractHorse)entity;
                if (!abstractHorse.isTame()) {
                    continue;
                }
                if (abstractHorse.getOwnerUniqueId() == null) {
                    continue;
                }
                abstractHorse.setAlwaysRenderNameTag(true);
                abstractHorse.setCustomNameTag("Owner: " + this.getUsername(abstractHorse.getOwnerUniqueId().toString()) + this.getSpeed(abstractHorse) + this.getJump(abstractHorse) + this.getHealth(abstractHorse));
            }
        }
    }
    
    public void onDisable() {
        this.cachedUUIDs.clear();
        for (final Entity entity : MobOwner.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityTameable) && !(entity instanceof AbstractHorse)) {
                continue;
            }
            try {
                entity.setAlwaysRenderNameTag(false);
            }
            catch (Exception ex) {}
        }
    }
    
    static {
        MobOwner.startTime = 0L;
        MobOwner.startTime1 = 0L;
    }
}
