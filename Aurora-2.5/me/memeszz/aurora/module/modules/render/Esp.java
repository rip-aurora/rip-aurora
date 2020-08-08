
package me.memeszz.aurora.module.modules.render;

import java.util.ArrayList;
import net.minecraft.entity.Entity;
import java.util.List;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Esp extends Module
{
    public Setting.mode mode;
    public Setting.b players;
    public Setting.b entity;
    public Setting.i width;
    List<Entity> entities;
    
    public Esp() {
        super("Esp", Category.RENDER, "Draws a box around entities");
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("OutLine");
        modes.add("Glow");
        this.mode = this.registerMode("Mode", modes, "OutLine");
        this.players = this.registerB("Players", false);
        this.entity = this.registerB("Entities", false);
        this.width = this.registerI("Width", 3, 0, 5);
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.mode.getValue() + "§7]";
    }
}
