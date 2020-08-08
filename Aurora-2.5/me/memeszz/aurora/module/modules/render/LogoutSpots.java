
package me.memeszz.aurora.module.modules.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import me.memeszz.aurora.util.render.RenderUtil;
import me.memeszz.aurora.event.events.RenderEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.function.Predicate;
import java.util.ConcurrentModificationException;
import me.memeszz.aurora.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.event.world.WorldEvent;
import me.memeszz.aurora.event.events.PlayerLeaveEvent;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PlayerJoinEvent;
import me.zero.alpine.listener.Listener;
import java.util.List;
import net.minecraft.entity.Entity;
import java.util.Map;
import me.memeszz.aurora.module.Module;

public class LogoutSpots extends Module
{
    Map<Entity, String> loggedPlayers;
    List<Entity> lastTickEntities;
    @EventHandler
    private final Listener<PlayerJoinEvent> listener1;
    @EventHandler
    private final Listener<PlayerLeaveEvent> listener2;
    @EventHandler
    private final Listener<WorldEvent.Unload> listener3;
    @EventHandler
    private final Listener<WorldEvent.Load> listener4;
    
    public LogoutSpots() {
        super("LogoutSpots", Category.RENDER, "Shows where players log out");
        this.loggedPlayers = new ConcurrentHashMap<Entity, String>();
        this.listener1 = new Listener<PlayerJoinEvent>(event -> this.loggedPlayers.forEach((e, s) -> {
            try {
                if (e.getName().equalsIgnoreCase(event.getName())) {
                    this.loggedPlayers.remove(e);
                    Command.sendClientMessage(ChatFormatting.BOLD + event.getName() + " reconnected!");
                }
            }
            catch (ConcurrentModificationException ex) {
                ex.printStackTrace();
            }
        }), (Predicate<PlayerJoinEvent>[])new Predicate[0]);
        String date;
        String pos;
        this.listener2 = new Listener<PlayerLeaveEvent>(event -> {
            if (LogoutSpots.mc.world == null) {
                return;
            }
            else {
                this.lastTickEntities.forEach(e -> {
                    if (e.getName().equalsIgnoreCase(event.getName())) {
                        date = new SimpleDateFormat("k:mm").format(new Date());
                        this.loggedPlayers.put(e, date);
                        pos = "x" + e.getPosition().getX() + " y" + e.getPosition().getY() + " z" + e.getPosition().getZ();
                        Command.sendClientMessage(ChatFormatting.BOLD + event.getName() + " disconnected at " + pos + "!");
                    }
                });
                return;
            }
        }, (Predicate<PlayerLeaveEvent>[])new Predicate[0]);
        this.listener3 = new Listener<WorldEvent.Unload>(event -> {
            this.lastTickEntities.clear();
            if (LogoutSpots.mc.player == null) {
                this.loggedPlayers.clear();
            }
            else if (!LogoutSpots.mc.player.isDead) {
                this.loggedPlayers.clear();
            }
            return;
        }, (Predicate<WorldEvent.Unload>[])new Predicate[0]);
        this.listener4 = new Listener<WorldEvent.Load>(event -> {
            this.lastTickEntities.clear();
            if (LogoutSpots.mc.player == null) {
                this.loggedPlayers.clear();
            }
            else if (!LogoutSpots.mc.player.isDead) {
                this.loggedPlayers.clear();
            }
        }, (Predicate<WorldEvent.Load>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        this.lastTickEntities = (List<Entity>)LogoutSpots.mc.world.loadedEntityList;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        this.loggedPlayers.forEach((e, time) -> {
            if (LogoutSpots.mc.player.getDistance(e) < 500.0f) {
                RenderUtil.prepareGL();
                RenderUtil.drawBoundingBox(e.getRenderBoundingBox(), 1.0f, Color.RED.getRGB());
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                this.drawNametag(e, time);
                GlStateManager.disableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                RenderUtil.releaseGL();
            }
        });
    }
    
    public void onEnable() {
        this.lastTickEntities = new ArrayList<Entity>();
        this.loggedPlayers.clear();
    }
    
    public void onDisable() {
        this.loggedPlayers.clear();
        this.lastTickEntities.clear();
    }
    
    private void drawNametag(final Entity entityIn, final String t) {
        GlStateManager.pushMatrix();
        final float f = LogoutSpots.mc.player.getDistance(entityIn);
        final float sc = (f < 25.0f) ? 0.5f : 2.0f;
        float m = f / 20.0f * (float)Math.pow(1.258925437927246, 0.1 / sc);
        if (m < 0.5f) {
            m = 0.5f;
        }
        if (m > 5.0f) {
            m = 5.0f;
        }
        final Vec3d interp = getInterpolatedRenderPos(entityIn, LogoutSpots.mc.getRenderPartialTicks());
        float mm;
        if (m > 2.0f) {
            mm = m / 2.0f;
        }
        else {
            mm = m;
        }
        final float yAdd = entityIn.height + mm;
        final double x = interp.x;
        final double y = interp.y + yAdd;
        final double z = interp.z;
        final float viewerYaw = LogoutSpots.mc.getRenderManager().playerViewY;
        final float viewerPitch = LogoutSpots.mc.getRenderManager().playerViewX;
        final boolean isThirdPersonFrontal = LogoutSpots.mc.getRenderManager().options.thirdPersonView == 2;
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-viewerYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(m, m, m);
        final FontRenderer fontRendererIn = LogoutSpots.mc.fontRenderer;
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        final String line1 = entityIn.getName() + "  (" + t + ")";
        final String line2 = "x" + entityIn.getPosition().getX() + " y" + entityIn.getPosition().getY() + " z" + entityIn.getPosition().getZ();
        final int i = fontRendererIn.getStringWidth(line1) / 2;
        final int ii = fontRendererIn.getStringWidth(line2) / 2;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableTexture2D();
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        fontRendererIn.drawStringWithShadow(line1, (float)(-i), 10.0f, Color.RED.darker().getRGB());
        fontRendererIn.drawStringWithShadow(line2, (float)(-ii), 20.0f, Color.RED.darker().getRGB());
        GlStateManager.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.popMatrix();
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }
    
    public static Vec3d getInterpolatedRenderPos(final Entity entity, final float ticks) {
        return getInterpolatedPos(entity, ticks).subtract(LogoutSpots.mc.getRenderManager().renderPosX, LogoutSpots.mc.getRenderManager().renderPosY, LogoutSpots.mc.getRenderManager().renderPosZ);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double x, final double y, final double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }
}
