package me.memeszz.aurora.module.modules.player;

import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Reach extends Module
{
    public Setting.d distance;
    
    public Reach() {
        super("Reach", Category.PLAYER, "Discord Rich Presence");
    }
    
    @Override
    public void setup() {
        this.distance = this.registerD("Distance", 6.0, 0.0, 10.0);
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.distance.getValue() + "§7]";
    }
}
