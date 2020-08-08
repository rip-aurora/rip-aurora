package me.memeszz.aurora.module.modules.misc;

import me.memeszz.aurora.module.Module;

public class ToggleMsgs extends Module
{
    public ToggleMsgs() {
        super("ToggleMsgs", Category.MISC, "Sends a client side message when you toggle any module");
        this.setDrawn(false);
        this.setEnabled(true);
    }
}
