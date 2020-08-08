
package me.memeszz.aurora.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import me.memeszz.aurora.module.Module;

public class ReverseStep extends Module
{
    public ReverseStep() {
        super("ReverseStep", Category.MOVEMENT, "Makes you fall down into holes faster");
    }
    
    @Override
    public void onUpdate() {
        if (ReverseStep.mc.player == null || ReverseStep.mc.world == null || ReverseStep.mc.player.isInWater() || ReverseStep.mc.player.isInLava()) {
            return;
        }
        if (ReverseStep.mc.player.onGround) {
            final EntityPlayerSP player = ReverseStep.mc.player;
            --player.motionY;
        }
    }
}
