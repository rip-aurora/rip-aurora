package me.memeszz.aurora.module.modules.render;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import me.memeszz.aurora.module.Module;

public class AntiFog extends Module
{
    public AntiFog() {
        super("AntiFog", Category.RENDER);
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
