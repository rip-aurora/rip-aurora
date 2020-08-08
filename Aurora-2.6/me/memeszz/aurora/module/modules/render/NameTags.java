package me.memeszz.aurora.module.modules.render;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemArmor;
import me.memeszz.aurora.util.math.MathUtil;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import me.memeszz.aurora.util.Wrapper;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.util.render.RenderUtil;
import me.memeszz.aurora.util.font.FontUtils;
import me.memeszz.aurora.module.modules.gui.ClickGuiModule;
import java.awt.Color;
import me.memeszz.aurora.util.colour.Rainbow;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.util.friends.Friends;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import me.memeszz.aurora.mixin.accessor.IRenderManager;
import me.memeszz.aurora.mixin.accessor.IMinecraft;
import net.minecraft.entity.player.EntityPlayer;
import me.memeszz.aurora.event.events.RenderEvent;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class NameTags extends Module
{
    Setting.d scale;
    Setting.b rainbowOutline;
    Setting.b ping;
    Setting.b gamemode;
    Setting.b health;
    
    public NameTags() {
        super("NameTags", Category.RENDER, "");
    }
    
    @Override
    public void setup() {
        this.scale = this.registerD("Scale", 1.9, 0.1, 10.0);
        this.gamemode = this.registerB("Gamemode", true);
        this.ping = this.registerB("Ping", true);
        this.health = this.registerB("Health", true);
        this.rainbowOutline = this.registerB("RainbowOutline", true);
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        for (final EntityPlayer p : NameTags.mc.world.playerEntities) {
            if (p != NameTags.mc.getRenderViewEntity() && p.isEntityAlive()) {
                final double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * ((IMinecraft)NameTags.mc).getTimer().renderPartialTicks - ((IRenderManager)NameTags.mc.getRenderManager()).getRenderPosX();
                final double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * ((IMinecraft)NameTags.mc).getTimer().renderPartialTicks - ((IRenderManager)NameTags.mc.getRenderManager()).getRenderPosY();
                final double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * ((IMinecraft)NameTags.mc).getTimer().renderPartialTicks - ((IRenderManager)NameTags.mc.getRenderManager()).getRenderPosZ();
                if (p.getName().startsWith("Body #")) {
                    continue;
                }
                this.renderNametag(p, pX, pY, pZ);
            }
        }
    }
    
    private int getHealthColor(final Entity entity) {
        final int scale = (int)Math.round(255.0 - ((EntityLivingBase)entity).getHealth() * 255.0 / ((EntityLivingBase)entity).getMaxHealth());
        final int damageColor = 255 - scale << 8 | scale << 16;
        return 0xFF000000 | damageColor;
    }
    
    private void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        GL11.glPushMatrix();
        String name = (Friends.isFriend(player.getName()) ? ChatFormatting.AQUA : ChatFormatting.WHITE) + player.getName();
        if (this.gamemode.getValue()) {
            name = name + "" + this.getGMText(player) + "§f";
        }
        if (this.ping.getValue()) {
            name = name + " " + this.getPing(player) + "ms";
        }
        if (this.health.getValue()) {
            name = name + " §r" + MathHelper.ceil(player.getHealth() + player.getAbsorptionAmount());
        }
        final float var14 = 0.016666668f * this.getNametagSize((EntityLivingBase)player);
        GL11.glTranslated((double)(float)x, (float)y + 2.5, (double)(float)z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-NameTags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(NameTags.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var14, -var14, var14);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GL11.glDisable(2929);
        Color c;
        if (this.rainbowOutline.getValue()) {
            c = Rainbow.getColor();
        }
        else {
            c = new Color(0, 0, 0, 140);
        }
        final int width = FontUtils.getStringWidth(ClickGuiModule.customFont.getValue(), name) / 2;
        if (ClickGuiModule.customFont.getValue()) {
            RenderUtil.drawBorderedRect(-width - 3, 8.0, width + 2, 22.0, 1.2, 1962934272, c.getRGB());
        }
        else {
            RenderUtil.drawBorderedRect(-width - 3, 8.0, width + 2, 20.0, 1.2, 1962934272, c.getRGB());
        }
        if (ClickGuiModule.customFont.getValue()) {
            Aurora.fontRenderer.drawStringWithShadow(name, -width, 11.0, this.getHealthColor((Entity)player));
        }
        else {
            Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(name, (float)(-width), 10.0f, this.getHealthColor((Entity)player));
        }
        int xOffset = 0;
        for (final ItemStack armourStack : player.inventory.armorInventory) {
            if (armourStack != null) {
                xOffset -= 8;
            }
        }
        player.getHeldItemMainhand();
        xOffset -= 8;
        final ItemStack renderStack = player.getHeldItemMainhand().copy();
        this.renderItem(renderStack, xOffset, -10);
        xOffset += 16;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armourStack2 = (ItemStack)player.inventory.armorInventory.get(index);
            final ItemStack renderStack2 = armourStack2.copy();
            this.renderItem(renderStack2, xOffset, -10);
            xOffset += 16;
        }
        player.getHeldItemOffhand();
        xOffset += 0;
        final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
        this.renderItem(renderOffhand, xOffset, -10);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private float getNametagSize(final EntityLivingBase player) {
        final ScaledResolution scaledRes = new ScaledResolution(NameTags.mc);
        final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 0.0 + this.scale.getValue());
        return (float)twoDscale + NameTags.mc.player.getDistance((Entity)player) / 5.6f;
    }
    
    public String getGMText(final EntityPlayer player) {
        if (player.isCreative()) {
            return " [C]";
        }
        if (player.isSpectator()) {
            return " [I]";
        }
        if (!player.isAllowEdit() && !player.isSpectator()) {
            return " [A]";
        }
        if (!player.isCreative() && !player.isSpectator() && player.isAllowEdit()) {
            return " [S]";
        }
        return "";
    }
    
    private void renderItem(final ItemStack stack, final int x, final int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        NameTags.mc.getRenderItem().zLevel = -100.0f;
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        NameTags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y / 2 - 12);
        NameTags.mc.getRenderItem().renderItemOverlays(NameTags.mc.fontRenderer, stack, x, y / 2 - 12);
        NameTags.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        this.renderEnchantText(stack, x, y - 18);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    public int getPing(final EntityPlayer player) {
        int ping = 0;
        try {
            ping = (int)MathUtil.clamp((float)Objects.requireNonNull(NameTags.mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1.0f, 300.0f);
        }
        catch (NullPointerException ex) {}
        return ping;
    }
    
    private void renderEnchantText(final ItemStack stack, final int x, final int y) {
        int encY = y - 24;
        int yCount = encY + 5;
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemTool) {
            final float green = (stack.getMaxDamage() - (float)stack.getItemDamage()) / stack.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), dmg + "%", x * 2 + 8, y + 26, new Color((int)(red * 255.0f), (int)(green * 255.0f), 0).getRGB());
        }
        final NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            final short id = enchants.getCompoundTagAt(index).getShort("id");
            final short level = enchants.getCompoundTagAt(index).getShort("lvl");
            final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc != null) {
                String encName = enc.isCurse() ? (TextFormatting.RED + enc.getTranslatedName((int)level).substring(11).substring(0, 1).toLowerCase()) : enc.getTranslatedName((int)level).substring(0, 1).toLowerCase();
                encName += level;
                GL11.glPushMatrix();
                GL11.glScalef(0.9f, 0.9f, 0.0f);
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), encName, x * 2 + 13, yCount, -1);
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
                encY += 8;
                yCount -= 10;
            }
        }
    }
}
