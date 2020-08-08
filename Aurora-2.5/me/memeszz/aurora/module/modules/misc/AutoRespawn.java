
package me.memeszz.aurora.module.modules.misc;

import java.util.function.Predicate;
import net.minecraft.client.gui.GuiScreen;
import me.memeszz.aurora.util.Wrapper;
import net.minecraft.client.gui.GuiGameOver;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.GuiScreenDisplayedEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class AutoRespawn extends Module
{
    Setting.b coords;
    @EventHandler
    private final Listener<GuiScreenDisplayedEvent> listener;
    
    public AutoRespawn() {
        super("AutoRespawn", Category.MISC, "Respawn when you die");
        this.listener = new Listener<GuiScreenDisplayedEvent>(event -> {
            if (event.getScreen() instanceof GuiGameOver) {
                if (this.coords.getValue()) {
                    Wrapper.sendClientMessage(String.format("You died at x%d y%d z%d", (int)AutoRespawn.mc.player.posX, (int)AutoRespawn.mc.player.posY, (int)AutoRespawn.mc.player.posZ));
                }
                if (AutoRespawn.mc.player != null) {
                    AutoRespawn.mc.player.respawnPlayer();
                }
                AutoRespawn.mc.displayGuiScreen((GuiScreen)null);
            }
        }, (Predicate<GuiScreenDisplayedEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.coords = this.registerB("DeathCoords", true);
    }
}
