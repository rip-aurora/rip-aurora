
package me.memeszz.aurora.module.modules.render;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Fov extends Module
{
    Setting.i fovSlider;
    Setting.mode mode;
    public float defaultFov;
    
    public Fov() {
        super("FOV", Category.RENDER, "Change Fov and Make it lower/higher than vanilla");
        this.fovSlider = this.registerI("Fov", 120, 0, 180);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("ViewModelChanger");
        modes.add("FovChanger");
        this.mode = this.registerMode("Mode", modes, "ViewModelChanger");
    }
    
    @SubscribeEvent
    public void fovOn(final EntityViewRenderEvent.FOVModifier e) {
        if (this.mode.getValue().equals("ViewModelChanger")) {
            e.setFOV((float)this.fovSlider.getValue());
        }
    }
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.defaultFov = Fov.mc.gameSettings.fovSetting;
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        Fov.mc.gameSettings.fovSetting = this.defaultFov;
    }
}
