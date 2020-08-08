
package me.memeszz.aurora.gui.components;

import me.memeszz.aurora.util.font.FontUtils;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import me.memeszz.aurora.module.modules.gui.ClickGuiModule;
import java.util.Iterator;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.Aurora;
import java.util.ArrayList;
import me.memeszz.aurora.gui.Frame;
import me.memeszz.aurora.module.Module;
import me.memeszz.aurora.gui.Component;

public class Button extends Component
{
    public Module mod;
    public Frame parent;
    public int offset;
    private boolean isHovered;
    private final ArrayList<Component> subcomponents;
    public boolean open;
    private final int height;
    
    public Button(final Module mod, final Frame parent, final int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        this.height = 16;
        int opY = offset + 16;
        if (Aurora.getInstance().settingsManager.getSettingsForMod(mod) != null && !Aurora.getInstance().settingsManager.getSettingsForMod(mod).isEmpty()) {
            for (final Setting s : Aurora.getInstance().settingsManager.getSettingsForMod(mod)) {
                switch (s.getType()) {
                    case MODE: {
                        this.subcomponents.add(new ModeButton((Setting.mode)s, this, mod, opY));
                        opY += 16;
                        continue;
                    }
                    case STRING: {
                        this.subcomponents.add(new StringButton((Setting.s)s, this, opY));
                        opY += 16;
                        continue;
                    }
                    case BOOLEAN: {
                        this.subcomponents.add(new CheckBox((Setting.b)s, this, opY));
                        opY += 16;
                        continue;
                    }
                    case DOUBLE: {
                        this.subcomponents.add(new DoubleSlider((Setting.d)s, this, opY));
                        opY += 16;
                        continue;
                    }
                    case INT: {
                        this.subcomponents.add(new IntSlider((Setting.i)s, this, opY));
                        opY += 16;
                        continue;
                    }
                    default: {
                        continue;
                    }
                }
            }
        }
        this.subcomponents.add(new Keybind(this, opY));
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 16;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 16;
        }
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset + 1, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 16 + this.offset, this.isHovered ? (this.mod.isEnabled() ? new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), ClickGuiModule.alpha.getValue()).getRGB() : new Color(0, 0, 0, 150).darker().darker().getRGB()) : (this.mod.isEnabled() ? new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), ClickGuiModule.alpha.getValue()).getRGB() : new Color(0, 0, 0, 150).getRGB()));
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.offset + 1, new Color(0, 0, 0, 150).getRGB());
        FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), this.mod.getName(), this.parent.getX() + 2, this.parent.getY() + this.offset + 2 + 2, -1);
        if (this.subcomponents.size() > 1) {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), this.open ? "^" : "v", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, -1);
        }
        if (this.open && !this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.renderComponent();
            }
        }
    }
    
    @Override
    public int getHeight() {
        if (this.open) {
            return 16 * (this.subcomponents.size() + 1);
        }
        return 16;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (final Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        for (final Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 16 + this.offset;
    }
}
