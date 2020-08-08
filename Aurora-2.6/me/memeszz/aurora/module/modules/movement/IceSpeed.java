package me.memeszz.aurora.module.modules.movement;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.init.Blocks;
import me.memeszz.aurora.event.events.UpdateEvent;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class IceSpeed extends Module
{
    Setting.d speed;
    
    public IceSpeed() {
        super("IceSpeed", Category.MOVEMENT, "SPEED");
    }
    
    @Override
    public void setup() {
        this.speed = this.registerD("Speed", 0.4, 0.0, 1.0);
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        Blocks.ICE.slipperiness = (float)this.speed.getValue();
        Blocks.PACKED_ICE.slipperiness = (float)this.speed.getValue();
        Blocks.FROSTED_ICE.slipperiness = (float)this.speed.getValue();
    }
    
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}
