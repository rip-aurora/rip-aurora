package me.memeszz.aurora.module.modules.movement;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import me.memeszz.aurora.event.events.UpdateEvent;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Step extends Module
{
    Setting.d height;
    Setting.mode mode;
    
    public Step() {
        super("Step", Category.MOVEMENT);
    }
    
    public void onDisable() {
        Step.mc.player.stepHeight = 0.5f;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Vanilla");
        this.mode = this.registerMode("Mode", modes, "Vanilla");
        this.height = this.registerD("Height", 2.0, 0.0, 6.0);
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        if ((Step.mc.player != null || Step.mc.world != null) && this.mode.getValue().equals("Vanilla")) {
            Step.mc.player.stepHeight = (float)this.height.getValue();
        }
    }
}
