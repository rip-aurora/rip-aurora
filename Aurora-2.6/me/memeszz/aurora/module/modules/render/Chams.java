package me.memeszz.aurora.module.modules.render;

import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Chams extends Module
{
    public static Setting.i width;
    public static Setting.i red;
    public static Setting.i green;
    public static Setting.i blue;
    public static Setting.i alpha;
    public static Setting.i invisRed;
    public static Setting.i invisGreen;
    public static Setting.i invisBlue;
    public static Setting.i invisAlpha;
    
    public Chams() {
        super("Chams", Category.RENDER);
    }
    
    @Override
    public void setup() {
        Chams.width = this.registerI("Width", 2, 1, 10);
        Chams.red = this.registerI("InvisibleRed", 255, 0, 255);
        Chams.green = this.registerI("InvisibleGreen", 0, 0, 255);
        Chams.blue = this.registerI("InvisibleBlue", 255, 0, 255);
        Chams.alpha = this.registerI("InvisibleAlpha", 255, 0, 255);
        Chams.invisRed = this.registerI("VisibleRed", 0, 0, 255);
        Chams.invisGreen = this.registerI("VisibleGreen", 0, 0, 255);
        Chams.invisBlue = this.registerI("VisibleBlue", 0, 0, 255);
        Chams.invisAlpha = this.registerI("VisibleAlpha", 200, 0, 255);
    }
}
