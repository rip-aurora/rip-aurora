
package me.memeszz.aurora.module.modules.player;

import net.minecraft.init.Items;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class FastUse extends Module
{
    Setting.b xp;
    Setting.b crystals;
    Setting.b all;
    Setting.b breakS;
    
    public FastUse() {
        super("FastUse", Category.PLAYER, "Sets right click / block break delay to 0");
    }
    
    @Override
    public void setup() {
        this.xp = this.registerB("EXP", true);
        this.crystals = this.registerB("Crystals", false);
        this.all = this.registerB("Everything", false);
        this.breakS = this.registerB("FastBreak", true);
    }
    
    @Override
    public void onUpdate() {
        if (this.xp.getValue() && FastUse.mc.player != null && (FastUse.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || FastUse.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE)) {
            FastUse.mc.rightClickDelayTimer = 0;
        }
        if (this.crystals.getValue() && FastUse.mc.player != null && (FastUse.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || FastUse.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)) {
            FastUse.mc.rightClickDelayTimer = 0;
        }
        if (this.all.getValue()) {
            FastUse.mc.rightClickDelayTimer = 0;
        }
        if (this.breakS.getValue()) {
            FastUse.mc.playerController.blockHitDelay = 0;
        }
    }
}
