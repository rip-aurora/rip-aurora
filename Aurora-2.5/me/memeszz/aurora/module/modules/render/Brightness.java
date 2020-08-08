
package me.memeszz.aurora.module.modules.render;

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
    
    @Override
    public void onUpdate() {
        Brightness.mc.gameSettings.gammaSetting = 666.0f;
    }
    
    public void onDisable() {
        Brightness.mc.gameSettings.gammaSetting = this.old;
    }
}
