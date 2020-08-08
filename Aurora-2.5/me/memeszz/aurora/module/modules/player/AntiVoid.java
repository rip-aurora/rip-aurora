
package me.memeszz.aurora.module.modules.player;

import me.memeszz.aurora.util.Wrapper;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class AntiVoid extends Module
{
    Setting.mode mode;
    
    public AntiVoid() {
        super("AntiVoid", Category.PLAYER, "Attacks nearby players");
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Bounce");
        modes.add("Mini");
        modes.add("Dolphin");
        this.mode = this.registerMode("Mode", modes, "Bounce");
    }
    
    @Override
    public void onUpdate() {
        final Double yLevel = AntiVoid.mc.player.posY;
        if (yLevel <= 0.5) {
            Wrapper.sendClientMessage("§aAttempting To Get " + ChatFormatting.RED + AntiVoid.mc.player.getName() + ChatFormatting.GREEN + " Out Of The void!");
            if (this.mode.getValue().equals("Bounce")) {
                AntiVoid.mc.player.moveVertical = 10.0f;
                AntiVoid.mc.player.jump();
            }
            if (this.mode.getValue().equals("Mini")) {
                AntiVoid.mc.player.moveVertical = 5.0f;
                AntiVoid.mc.player.jump();
            }
            if (this.mode.getValue().equals("Dolphin")) {
                AntiVoid.mc.player.moveVertical = 2.0f;
                AntiVoid.mc.player.jump();
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.mode.getValue() + "§7]";
    }
}
