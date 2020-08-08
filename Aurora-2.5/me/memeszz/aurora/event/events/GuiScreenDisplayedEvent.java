
package me.memeszz.aurora.event.events;

import net.minecraft.client.gui.GuiScreen;
import me.memeszz.aurora.event.AuroraEvent;

public class GuiScreenDisplayedEvent extends AuroraEvent
{
    private final GuiScreen guiScreen;
    
    public GuiScreenDisplayedEvent(final GuiScreen screen) {
        this.guiScreen = screen;
    }
    
    public GuiScreen getScreen() {
        return this.guiScreen;
    }
}
