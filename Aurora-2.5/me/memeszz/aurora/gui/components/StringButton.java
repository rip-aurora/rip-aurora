
package me.memeszz.aurora.gui.components;

import me.memeszz.aurora.util.font.FontUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.module.modules.gui.ClickGuiModule;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.gui.Component;

public class StringButton extends Component
{
    private final Setting.s op;
    private final Button parent;
    private int offset;
    
    public StringButton(final Setting.s option, final Button button, final int offset) {
        this.op = option;
        this.parent = button;
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, new Color(0, 0, 0, 150).getRGB());
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, new Color(0, 0, 0, 150).getRGB());
        FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), this.op.getName() + " " + ChatFormatting.GRAY + "-set", this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
}
