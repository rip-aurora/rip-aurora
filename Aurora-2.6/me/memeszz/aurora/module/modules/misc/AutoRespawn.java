package me.memeszz.aurora.module.modules.misc;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.client.gui.GuiScreen;
import me.memeszz.aurora.util.Wrapper;
import net.minecraft.client.gui.GuiGameOver;
import me.memeszz.aurora.event.events.GuiScreenDisplayedEvent;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class AutoRespawn extends Module
{
    Setting.b coords;
    
    public AutoRespawn() {
        super("AutoRespawn", Category.MISC, "Respawn when you die");
    }
    
    @Override
    public void setup() {
        this.coords = this.registerB("DeathCoords", true);
    }
    
    @Listener
    public void onUpdate(final GuiScreenDisplayedEvent event) {
        if (event.getScreen() instanceof GuiGameOver) {
            if (this.coords.getValue()) {
                Wrapper.sendClientMessage(String.format("You died at x%d y%d z%d", (int)AutoRespawn.mc.player.posX, (int)AutoRespawn.mc.player.posY, (int)AutoRespawn.mc.player.posZ));
            }
            if (AutoRespawn.mc.player != null) {
                AutoRespawn.mc.player.respawnPlayer();
            }
            AutoRespawn.mc.displayGuiScreen((GuiScreen)null);
        }
    }
}
