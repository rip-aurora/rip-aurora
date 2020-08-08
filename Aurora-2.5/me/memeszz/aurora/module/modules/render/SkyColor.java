
package me.memeszz.aurora.module.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import java.awt.Color;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class SkyColor extends Module
{
    Setting.i r;
    Setting.i g;
    Setting.i b;
    
    public SkyColor() {
        super("SkyColor", Category.RENDER, "Draws a box around entities");
        this.r = this.registerI("Red", 255, 0, 255);
        this.g = this.registerI("Green", 0, 0, 255);
        this.b = this.registerI("Blue", 255, 0, 255);
    }
    
    public int getSkyColorByTemp(final float par1) {
        return Color.red.getRGB();
    }
    
    @Override
    public void onUpdate() {
        if (this.isEnabled()) {
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
        else {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
    }
    
    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        event.setRed(this.r.getValue() / 255.0f);
        event.setGreen(this.g.getValue() / 255.0f);
        event.setBlue(this.b.getValue() / 255.0f);
    }
    
    @SubscribeEvent
    public void fogDensity(final EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.0f);
        event.setCanceled(true);
    }
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}
