package me.memeszz.aurora.gui;

import me.memeszz.aurora.util.font.FontUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.FontRenderer;
import java.util.Iterator;
import me.memeszz.aurora.gui.components.Button;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.module.modules.gui.ClickGuiModule;
import me.memeszz.aurora.module.Module;
import java.util.ArrayList;

public class Frame
{
    public ArrayList<Component> components;
    public Module.Category category;
    private boolean open;
    private final int width;
    private int y;
    private int x;
    private final int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    private int height;
    ClickGuiModule mod;
    
    public Frame(final Module.Category cat) {
        this.mod = (ClickGuiModule)ModuleManager.getModuleByName("ClickGuiModule");
        this.components = new ArrayList<Component>();
        this.category = cat;
        this.width = 88;
        this.x = 5;
        this.y = 5;
        this.barHeight = 16;
        this.dragX = 0;
        this.open = true;
        this.isDragging = false;
        int tY = this.barHeight;
        for (final Module mod : ModuleManager.getModulesInCategory(cat)) {
            final Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            tY += 16;
        }
        this.refresh();
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void setX(final int newX) {
        this.x = newX;
    }
    
    public void setY(final int newY) {
        this.y = newY;
    }
    
    public void setDrag(final boolean drag) {
        this.isDragging = drag;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public void renderFrame(final FontRenderer fontRenderer) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, ClickGUI.color);
        final Minecraft mc = Minecraft.getMinecraft();
        FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), this.category.name(), this.x + 2, this.y + 3, -1);
        if (this.open && !this.components.isEmpty()) {
            for (final Component component : this.components) {
                component.renderComponent();
            }
        }
    }
    
    public void refresh() {
        int off = this.barHeight;
        for (final Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
        this.height = off;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }
    
    public boolean isWithinHeader(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
}
