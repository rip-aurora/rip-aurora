
package me.memeszz.aurora.module.modules.gui;

import java.util.Iterator;
import me.memeszz.aurora.util.misc.TickRate;
import net.minecraft.util.ResourceLocation;
import me.memeszz.aurora.util.colour.ColourHolder;
import net.minecraft.client.renderer.GlStateManager;
import me.memeszz.aurora.Aurora;
import java.util.Comparator;
import me.memeszz.aurora.util.font.FontUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.util.colour.Rainbow;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import net.minecraft.client.renderer.RenderItem;
import java.awt.Color;
import me.memeszz.aurora.module.Module;

public class Hud extends Module
{
    int modCount;
    Color c;
    private static final RenderItem itemRender;
    private Setting.b welcomer;
    private Setting.b server;
    private Setting.b ping;
    private Setting.b ArmorHud;
    private Setting.b fps;
    private Setting.b watermark;
    public Setting.b rainbow;
    private Setting.b coordinates;
    private Setting.b tps;
    private Setting.b arraylist;
    public Setting.i red;
    public Setting.i green;
    public Setting.i blue;
    Setting.mode mode;
    String coords;
    
    public Hud() {
        super("Hud", Category.GUI, "Attacks nearby players");
        this.setDrawn(false);
    }
    
    @Override
    public void setup() {
        this.watermark = this.registerB("Watermark", true);
        this.welcomer = this.registerB("Welcomer", true);
        this.server = this.registerB("Server", true);
        this.ping = this.registerB("Ping", true);
        this.tps = this.registerB("Tps", true);
        this.fps = this.registerB("Fps", true);
        this.coordinates = this.registerB("Coords", true);
        this.ArmorHud = this.registerB("ArmorHud", true);
        this.arraylist = this.registerB("ArrayList", true);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Bottom");
        modes.add("Top");
        this.mode = this.registerMode("Mode", modes, "Top");
        this.red = this.registerI("Red", 255, 0, 255);
        this.green = this.registerI("Green", 255, 0, 255);
        this.blue = this.registerI("Blue", 255, 0, 255);
        this.rainbow = this.registerB("Rainbow", false);
    }
    
    public static int getItems(final Item i) {
        return Hud.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum() + Hud.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum();
    }
    
    public String getGaps() {
        return String.valueOf(getItems(Items.GOLDEN_APPLE));
    }
    
    public String getCrystals() {
        return String.valueOf(getItems(Items.END_CRYSTAL));
    }
    
    @Override
    public void onRender() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (this.arraylist.getValue()) {
            if (this.rainbow.getValue()) {
                this.c = Rainbow.getColor();
            }
            else {
                this.c = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
            }
            this.modCount = 0;
            if (this.mode.getValue().equalsIgnoreCase("Top")) {
                ModuleManager.getModules().stream().filter(Module::isEnabled).filter(Module::isDrawn).sorted(Comparator.comparing(module -> FontUtils.getStringWidth(ClickGuiModule.customFont.getValue(), module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo()) * -1)).forEach(m -> {
                    FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo(), 958 - FontUtils.getStringWidth(ClickGuiModule.customFont.getValue(), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo()), 3 + this.modCount * 10, this.c.getRGB());
                    ++this.modCount;
                    return;
                });
            }
            if (this.mode.getValue().equalsIgnoreCase("Bottom")) {
                ModuleManager.getModules().stream().filter(Module::isEnabled).filter(Module::isDrawn).sorted(Comparator.comparing(module -> FontUtils.getStringWidth(ClickGuiModule.customFont.getValue(), module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo()) * -1)).forEach(m -> {
                    FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo(), 958 - FontUtils.getStringWidth(ClickGuiModule.customFont.getValue(), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo()), sr.getScaledHeight() - 3 - Aurora.fontRenderer.getHeight() + this.modCount * -10, this.c.getRGB());
                    ++this.modCount;
                    return;
                });
            }
        }
        if (this.ArmorHud.getValue()) {
            GlStateManager.enableTexture2D();
            final ScaledResolution resolution = new ScaledResolution(Hud.mc);
            final int i = resolution.getScaledWidth() / 2;
            int iteration = 0;
            final int y = resolution.getScaledHeight() - 55 - (Hud.mc.player.isInWater() ? 10 : 0);
            for (final ItemStack is : Hud.mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) {
                    continue;
                }
                final int x = i - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                Hud.itemRender.zLevel = 200.0f;
                Hud.itemRender.renderItemAndEffectIntoGUI(is, x, y);
                Hud.itemRender.renderItemOverlayIntoGUI(Hud.mc.fontRenderer, is, x, y, "");
                Hud.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), s, x + 19 - 2 - Aurora.fontRenderer.getStringWidth(s), y + 9, 16777215);
                final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - green;
                final int dmg = 100 - (int)(red * 100.0f);
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), dmg + "", x + 8 - Aurora.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        int posY = 2;
        final Color c = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
        if (this.watermark.getValue()) {
            Hud.mc.renderEngine.bindTexture(new ResourceLocation("aurora", "minecraft/textures/misc/aurora.png"));
            final String text = Aurora.MODNAME + " " + "2.5";
            if (!this.rainbow.getValue()) {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), text, 2, posY, c.getRGB());
            }
            else {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), text, 2, posY, Rainbow.getInt());
            }
            posY += 10;
        }
        if (!Hud.mc.isSingleplayer() && this.server.getValue()) {
            if (!this.rainbow.getValue()) {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "§rServer §f" + Hud.mc.currentServerData.serverIP + "", 2, posY, c.getRGB());
            }
            else {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "§rServer §f" + Hud.mc.currentServerData.serverIP + "", 2, posY, Rainbow.getInt());
            }
            posY += 10;
        }
        if (this.ping.getValue()) {
            if (!this.rainbow.getValue()) {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "§rPing §f" + this.getPing() + "ms", 2, posY, c.getRGB());
            }
            else {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "§rPing §f" + this.getPing() + "ms", 2, posY, Rainbow.getInt());
            }
            posY += 10;
        }
        if (this.tps.getValue()) {
            if (!this.rainbow.getValue()) {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "§rTPS §f" + TickRate.TPS + "", 2, posY, c.getRGB());
            }
            else {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "§rTPS §f" + TickRate.TPS + "", 2, posY, Rainbow.getInt());
            }
            posY += 10;
        }
        if (this.fps.getValue()) {
            if (!this.rainbow.getValue()) {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "§rFPS §f" + Minecraft.debugFPS + "", 2, posY, c.getRGB());
            }
            else {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "§rFPS §f" + Minecraft.debugFPS + "", 2, posY, Rainbow.getInt());
            }
            posY += 10;
        }
        if (this.coordinates.getValue()) {
            if (Hud.mc.player.dimension == -1) {
                this.coords = ChatFormatting.GRAY + "XYZ " + ChatFormatting.WHITE + Hud.mc.player.getPosition().getX() + ", " + Hud.mc.player.getPosition().getY() + ", " + Hud.mc.player.getPosition().getZ() + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + Hud.mc.player.getPosition().getX() * 8 + ", " + Hud.mc.player.getPosition().getZ() * 8 + ChatFormatting.GRAY + "]";
            }
            else {
                this.coords = ChatFormatting.GRAY + "XYZ " + ChatFormatting.WHITE + Hud.mc.player.getPosition().getX() + ", " + Hud.mc.player.getPosition().getY() + ", " + Hud.mc.player.getPosition().getZ() + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + Hud.mc.player.getPosition().getX() / 8 + ", " + Hud.mc.player.getPosition().getZ() / 8 + ChatFormatting.GRAY + "]";
            }
            if (this.rainbow.getValue()) {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), this.coords, 0, sr.getScaledHeight() - 2 - Aurora.fontRenderer.getHeight(), Rainbow.getInt());
            }
            else {
                FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), this.coords, 0, sr.getScaledHeight() - 2 - Aurora.fontRenderer.getHeight(), c.getRGB());
            }
        }
        if (this.welcomer.getValue()) {
            final ScaledResolution resolution2 = new ScaledResolution(Hud.mc);
            final String text2 = "Welcome " + Hud.mc.player.getName() + ":^)";
            if (this.rainbow.getValue()) {
                this.drawCentredString(text2, resolution2.getScaledWidth() / 2, 2, Rainbow.getInt());
            }
            else {
                this.drawCentredString(text2, resolution2.getScaledWidth() / 2, 2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
            }
        }
    }
    
    public int getPing() {
        int p = -1;
        if (Hud.mc.player == null || Hud.mc.getConnection() == null || Hud.mc.getConnection().getPlayerInfo(Hud.mc.player.getName()) == null || Hud.mc.player.getName() == null) {
            p = -1;
        }
        else {
            p = Hud.mc.getConnection().getPlayerInfo(Hud.mc.player.getName()).getResponseTime();
        }
        return p;
    }
    
    private void drawCentredString(final String text, final int x, final int y, final int color) {
        FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), text, x - FontUtils.getStringWidth(ClickGuiModule.customFont.getValue(), text) / 2, y, color);
    }
    
    static {
        itemRender = Minecraft.getMinecraft().getRenderItem();
    }
}
