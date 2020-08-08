
package me.memeszz.aurora.module.modules.player;

import net.minecraft.client.entity.EntityPlayerSP;
import me.memeszz.aurora.module.Module;

public class NoWeb extends Module
{
    public NoWeb() {
        super("FastFallWeb", Category.PLAYER);
    }
    
    @Override
    public void onUpdate() {
        if (NoWeb.mc.player.isInWeb) {
            final EntityPlayerSP player = NoWeb.mc.player;
            --player.motionY;
            final EntityPlayerSP player2 = NoWeb.mc.player;
            --player2.motionY;
            final EntityPlayerSP player3 = NoWeb.mc.player;
            --player3.motionY;
            final EntityPlayerSP player4 = NoWeb.mc.player;
            --player4.motionY;
        }
    }
}
