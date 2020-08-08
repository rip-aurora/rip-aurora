
package me.memeszz.aurora.module.modules.movement;

import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Sprint extends Module
{
    Setting.mode mode;
    
    public Sprint() {
        super("Sprint", Category.MOVEMENT, "Automatically sprint");
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Legit");
        modes.add("Rage");
        this.mode = this.registerMode("Mode", modes, "Rage");
    }
    
    public void onDisable() {
        super.onDisable();
        if (Sprint.mc.world != null) {
            Sprint.mc.player.setSprinting(false);
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.mode.getValue().equals("Rage")) {
            if ((Sprint.mc.gameSettings.keyBindForward.isKeyDown() || Sprint.mc.gameSettings.keyBindBack.isKeyDown() || Sprint.mc.gameSettings.keyBindLeft.isKeyDown() || Sprint.mc.gameSettings.keyBindRight.isKeyDown()) && !Sprint.mc.player.isSneaking() && !Sprint.mc.player.collidedHorizontally && Sprint.mc.player.getFoodStats().getFoodLevel() > 6.0f) {
                Sprint.mc.player.setSprinting(true);
            }
            if (this.mode.getValue().equals("Legit") && Sprint.mc.gameSettings.keyBindForward.isKeyDown() && !Sprint.mc.player.isSneaking() && !Sprint.mc.player.isHandActive() && !Sprint.mc.player.collidedHorizontally && Sprint.mc.currentScreen == null && Sprint.mc.player.getFoodStats().getFoodLevel() > 6.0f) {
                Sprint.mc.player.setSprinting(true);
            }
        }
    }
}
