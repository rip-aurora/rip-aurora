
package me.memeszz.aurora.module.modules.misc;

import java.text.DecimalFormat;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Timer extends Module
{
    int tickWait;
    float hudInfo;
    Setting.d speedUsual;
    Setting.d fastUsual;
    Setting.d tickToFast;
    Setting.d tickToNoFast;
    
    public Timer() {
        super("Timer", Category.MISC, "Clinet's TimerSwitch");
        this.tickWait = 0;
        this.hudInfo = 0.0f;
        this.speedUsual = this.registerD("Speed", 4.2, 1.0, 10.0);
        this.fastUsual = this.registerD("FastSpeed", 10.0, 1.0, 1000.0);
        this.tickToFast = this.registerD("TickToFast", 4.0, 0.0, 20.0);
        this.tickToNoFast = this.registerD("TickToDisableFast", 7.0, 0.0, 20.0);
    }
    
    public void onDisable() {
        Timer.mc.timer.tickLength = 50.0f;
    }
    
    @Override
    public void onUpdate() {
        if (this.tickWait == (float)this.tickToFast.getValue()) {
            Timer.mc.timer.tickLength = 50.0f / (float)this.fastUsual.getValue();
            this.hudInfo = (float)this.fastUsual.getValue();
        }
        if (this.tickWait >= (float)this.tickToNoFast.getValue()) {
            this.tickWait = 0;
            Timer.mc.timer.tickLength = 50.0f / (float)this.speedUsual.getValue();
            this.hudInfo = (float)this.speedUsual.getValue();
        }
        ++this.tickWait;
    }
    
    @Override
    public String getHudInfo() {
        return new DecimalFormat("0.##").format(this.hudInfo);
    }
}
