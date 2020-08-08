package me.memeszz.aurora.module.modules.render;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import me.memeszz.aurora.event.events.UpdateEvent;
import me.memeszz.aurora.module.Module;

public class Brightness extends Module
{
    float old;
    
    public Brightness() {
        super("Brightness", Category.RENDER, "Lets you see shit when it's dark");
    }
    
    public void onEnable() {
        this.old = Brightness.mc.gameSettings.gammaSetting;
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        Brightness.mc.gameSettings.gammaSetting = 666.0f;
    }
    
    public void onDisable() {
        Brightness.mc.gameSettings.gammaSetting = this.old;
    }
}
