package me.memeszz.aurora.module.modules.render;

import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class BlockHighlight extends Module
{
    public static Setting.i red;
    public static Setting.i green;
    public static Setting.i blue;
    public static Setting.i alpha;
    
    public BlockHighlight() {
        super("BlockHighlight", Category.RENDER, "Highlights the block you're looking at");
    }
    
    @Override
    public void setup() {
        BlockHighlight.red = this.registerI("Red", 255, 0, 255);
        BlockHighlight.green = this.registerI("Green", 255, 0, 255);
        BlockHighlight.blue = this.registerI("Blue", 255, 0, 255);
        BlockHighlight.alpha = this.registerI("Alpha", 255, 0, 255);
    }
}
