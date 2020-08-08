package me.memeszz.aurora.module.modules.gui;

import net.minecraft.potion.PotionEffect;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.util.font.FontUtils;
import me.memeszz.aurora.module.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.resources.I18n;
import java.text.DecimalFormat;
import me.memeszz.aurora.setting.Setting;
import java.awt.Color;
import me.memeszz.aurora.module.Module;

public class PotionEffects extends Module
{
    int count;
    Color c;
    private Setting.i x;
    private Setting.i y;
    private Setting.b sortUp;
    private Setting.b right;
    DecimalFormat format2;
    
    public PotionEffects() {
        super("PotionEffects", Category.GUI);
        this.format2 = new DecimalFormat("00");
        this.setDrawn(false);
    }
    
    @Override
    public void setup() {
        this.x = this.registerI("X", 100, 0, 1000);
        this.y = this.registerI("Y", 100, 0, 1000);
        this.sortUp = this.registerB("SortUp", true);
        this.right = this.registerB("AlignRight", false);
    }
    
    @Override
    public void onRender() {
        this.count = 0;
        try {
            final String name;
            final double duration;
            final int amplifier;
            final int color;
            final double p1;
            final String seconds;
            final String s;
            PotionEffects.mc.player.getActivePotionEffects().forEach(effect -> {
                name = I18n.format(effect.getPotion().getName(), new Object[0]);
                duration = effect.getDuration() / 19.99f;
                amplifier = effect.getAmplifier() + 1;
                color = effect.getPotion().getLiquidColor();
                p1 = duration % 60.0;
                seconds = this.format2.format(p1);
                s = name + " " + amplifier + ChatFormatting.GRAY + " " + (int)duration / 60 + ":" + seconds;
                if (this.sortUp.getValue()) {
                    if (this.right.getValue()) {
                        this.drawText(s, this.x.getValue() - this.getWidth(s), this.y.getValue() + this.count * 10, color);
                    }
                    else {
                        this.drawText(s, this.x.getValue(), this.y.getValue() + this.count * 10, color);
                    }
                    ++this.count;
                }
                else {
                    if (this.right.getValue()) {
                        this.drawText(s, this.x.getValue() - this.getWidth(s), this.y.getValue() + this.count * -10, color);
                    }
                    else {
                        this.drawText(s, this.x.getValue(), this.y.getValue() + this.count * -10, color);
                    }
                    ++this.count;
                }
            });
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    private void drawText(final String s, final int x, final int y, final int color) {
        this.c = new Color(((Hud)ModuleManager.getModuleByName("Hud")).red.getValue(), ((Hud)ModuleManager.getModuleByName("Hud")).green.getValue(), ((Hud)ModuleManager.getModuleByName("Hud")).blue.getValue());
        if (((Hud)ModuleManager.getModuleByName("Hud")).rainbow.getValue()) {
            final ClickGuiModule clickGuiModule = (ClickGuiModule)ModuleManager.getModuleByName("ClickGui");
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), s, x, y, color);
        }
        else {
            final ClickGuiModule clickGuiModule2 = (ClickGuiModule)ModuleManager.getModuleByName("ClickGui");
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), s, x, y, color);
        }
    }
    
    private int getWidth(final String s) {
        final ClickGuiModule clickGuiModule = (ClickGuiModule)ModuleManager.getModuleByName("ClickGui");
        if (ClickGuiModule.customFont.getValue()) {
            return Aurora.fontRenderer.getStringWidth(s);
        }
        return PotionEffects.mc.fontRenderer.getStringWidth(s);
    }
}
