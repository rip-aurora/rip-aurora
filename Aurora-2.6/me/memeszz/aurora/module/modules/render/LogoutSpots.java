package me.memeszz.aurora.module.modules.render;

import me.memeszz.aurora.mixin.accessor.IRenderManager;
import net.minecraft.util.math.Vec3d;
import me.memeszz.aurora.util.font.FontUtils;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraft.client.renderer.GlStateManager;
import me.memeszz.aurora.module.modules.gui.ClickGuiModule;
import me.memeszz.aurora.util.render.RenderUtil;
import me.memeszz.aurora.event.events.RenderEvent;
import me.memeszz.aurora.event.events.UpdateEvent;
import me.memeszz.aurora.util.Wrapper;
import java.util.Date;
import java.text.SimpleDateFormat;
import me.memeszz.aurora.event.events.PlayerLeaveEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import java.util.ConcurrentModificationException;
import me.memeszz.aurora.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.event.events.PlayerJoinEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import net.minecraft.entity.Entity;
import java.util.Map;
import me.memeszz.aurora.module.Module;

public class LogoutSpots extends Module
{
    Map<Entity, String> loggedPlayers;
    List<Entity> lastTickEntities;
    
    public LogoutSpots() {
        super("LogoutSpots", Category.RENDER, "Shows where players log out");
        this.loggedPlayers = new ConcurrentHashMap<Entity, String>();
    }
    
    @Listener
    public void join(final PlayerJoinEvent event) {
        this.loggedPlayers.forEach((e, s) -> {
            try {
                if (e.getName().equalsIgnoreCase(event.getName())) {
                    this.loggedPlayers.remove(e);
                    Command.sendClientMessage(ChatFormatting.GREEN + event.getName() + " reconnected!");
                }
            }
            catch (ConcurrentModificationException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    @Listener
    public void onUpdate(final PlayerLeaveEvent event) {
        if (LogoutSpots.mc.world == null) {
            return;
        }
        String date;
        String pos;
        this.lastTickEntities.forEach(e -> {
            if (e.getName().equalsIgnoreCase(event.getName())) {
                date = new SimpleDateFormat("k:mm").format(new Date());
                this.loggedPlayers.put(e, date);
                pos = "x" + e.getPosition().getX() + " y" + e.getPosition().getY() + " z" + e.getPosition().getZ();
                Wrapper.sendClientMessage(ChatFormatting.LIGHT_PURPLE + event.getName() + "§c Disconnected At " + ChatFormatting.GREEN + pos + ".");
            }
        });
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        this.lastTickEntities = (List<Entity>)LogoutSpots.mc.world.loadedEntityList;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        this.loggedPlayers.forEach((e, time) -> {
            if (LogoutSpots.mc.player.getDistance(e) < 500.0f) {
                RenderUtil.prepareGL();
                RenderUtil.drawBoundingBox(e.getRenderBoundingBox(), 1.0f, ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), 255);
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
    
    @Listener
    public void unload(final WorldEvent.Unload event) {
        this.lastTickEntities.clear();
        if (LogoutSpots.mc.player == null) {
            this.loggedPlayers.clear();
        }
        else if (!LogoutSpots.mc.player.isDead) {
            this.loggedPlayers.clear();
        }
    }
    
    @Listener
    public void load(final WorldEvent.Load event) {
        this.lastTickEntities.clear();
        if (LogoutSpots.mc.player == null) {
            this.loggedPlayers.clear();
        }
        else if (!LogoutSpots.mc.player.isDead) {
            this.loggedPlayers.clear();
        }
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
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        final String line1 = entityIn.getName() + "  (" + t + ")";
        final String line2 = Color.GREEN.getRGB() + "x" + entityIn.getPosition().getX() + " y" + entityIn.getPosition().getY() + " z" + entityIn.getPosition().getZ();
        final int i = FontUtils.getStringWidth(ClickGuiModule.customFont.getValue(), line1) / 2;
        final int ii = FontUtils.getStringWidth(ClickGuiModule.customFont.getValue(), line2) / 2;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableTexture2D();
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), line1, -i, 10, Color.WHITE.getRGB());
        FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), line2, -ii, 20, Color.WHITE.getRGB());
        GlStateManager.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.popMatrix();
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }
    
    public static Vec3d getInterpolatedRenderPos(final Entity entity, final float ticks) {
        return getInterpolatedPos(entity, ticks).subtract(((IRenderManager)LogoutSpots.mc).getRenderPosX(), ((IRenderManager)LogoutSpots.mc).getRenderPosY(), ((IRenderManager)LogoutSpots.mc).getRenderPosZ());
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double x, final double y, final double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }
}
