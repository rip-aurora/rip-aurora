
package me.memeszz.aurora.module.modules.movement;

import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class FastSwim extends Module
{
    private Setting.b up;
    private Setting.b forward;
    private Setting.b sprint;
    private Setting.b only2b;
    private Setting.b down;
    int divider;
    
    public FastSwim() {
        super("FastSwim", Category.MOVEMENT, "Allows The Player To Swim Faster Horizontally and Vertically");
        this.divider = 5;
    }
    
    @Override
    public void setup() {
        this.up = this.registerB("FastSwimUp", true);
        this.down = this.registerB("FastSwimDown", true);
        this.forward = this.registerB("FastSwimForward", true);
        this.sprint = this.registerB("AutoSprintInLiquid", true);
        this.only2b = this.registerB("Only2b", true);
    }
    
    @Override
    public void onUpdate() {
        if (this.only2b.getValue() && !FastSwim.mc.isSingleplayer() && FastSwim.mc.currentServerData.serverIP.equalsIgnoreCase("2b2t.org")) {
            if (this.sprint.getValue() && (FastSwim.mc.player.isInLava() || FastSwim.mc.player.isInWater())) {
                FastSwim.mc.player.setSprinting(true);
            }
            if ((FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) && FastSwim.mc.gameSettings.keyBindJump.isKeyDown() && this.up.getValue()) {
                FastSwim.mc.player.motionY = 0.725 / this.divider;
            }
            if (FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) {
                if ((this.forward.getValue() && FastSwim.mc.gameSettings.keyBindForward.isKeyDown()) || FastSwim.mc.gameSettings.keyBindLeft.isKeyDown() || FastSwim.mc.gameSettings.keyBindRight.isKeyDown() || FastSwim.mc.gameSettings.keyBindBack.isKeyDown()) {
                    FastSwim.mc.player.jumpMovementFactor = 0.34f / this.divider;
                }
                else {
                    FastSwim.mc.player.jumpMovementFactor = 0.0f;
                }
            }
            if (FastSwim.mc.player.isInWater() && this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final int divider2 = this.divider * -1;
                FastSwim.mc.player.motionY = 2.2 / divider2;
            }
            if (FastSwim.mc.player.isInLava() && this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final int divider2 = this.divider * -1;
                FastSwim.mc.player.motionY = 0.91 / divider2;
            }
        }
        if (!this.only2b.getValue()) {
            if (this.sprint.getValue() && (FastSwim.mc.player.isInLava() || FastSwim.mc.player.isInWater())) {
                FastSwim.mc.player.setSprinting(true);
            }
            if ((FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) && FastSwim.mc.gameSettings.keyBindJump.isKeyDown() && this.up.getValue()) {
                FastSwim.mc.player.motionY = 0.725 / this.divider;
            }
            if (FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) {
                if ((this.forward.getValue() && FastSwim.mc.gameSettings.keyBindForward.isKeyDown()) || FastSwim.mc.gameSettings.keyBindLeft.isKeyDown() || FastSwim.mc.gameSettings.keyBindRight.isKeyDown() || FastSwim.mc.gameSettings.keyBindBack.isKeyDown()) {
                    FastSwim.mc.player.jumpMovementFactor = 0.34f / this.divider;
                }
                else {
                    FastSwim.mc.player.jumpMovementFactor = 0.0f;
                }
            }
            if (FastSwim.mc.player.isInWater() && this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final int divider2 = this.divider * -1;
                FastSwim.mc.player.motionY = 2.2 / divider2;
            }
            if (FastSwim.mc.player.isInLava() && this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final int divider2 = this.divider * -1;
                FastSwim.mc.player.motionY = 0.91 / divider2;
            }
        }
    }
}
