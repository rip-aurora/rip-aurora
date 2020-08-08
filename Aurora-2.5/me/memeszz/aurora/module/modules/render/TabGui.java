
package me.memeszz.aurora.module.modules.render;

import java.util.Iterator;
import java.util.ArrayList;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.util.font.FontUtils;
import net.minecraft.client.gui.Gui;
import me.memeszz.aurora.util.colour.Rainbow;
import me.memeszz.aurora.module.Module;

public class TabGui extends Module
{
    Category[] categories;
    int y;
    int modY;
    public static int selected;
    public static boolean extended;
    public static int selectedMod;
    public static Module currentMod;
    
    public TabGui() {
        super("TabGUI", Category.RENDER);
        this.categories = Category.values();
        this.setDrawn(false);
    }
    
    @Override
    public void onRender() {
        if (TabGui.selected < 0) {
            TabGui.selected = 0;
        }
        if (TabGui.selected > this.categories.length - 1) {
            TabGui.selected = this.categories.length - 1;
        }
        this.y = 2;
        this.modY = 2;
        TabGui.currentMod = null;
        for (final Category c : this.categories) {
            if (this.categories[TabGui.selected].equals(c)) {
                Gui.drawRect(2, this.y, 62, this.y + 12, Rainbow.getIntWithOpacity(100));
            }
            else {
                Gui.drawRect(2, this.y, 62, this.y + 12, 1712394513);
            }
            FontUtils.drawStringWithShadow(false, c.name(), 4, this.y + 2, -1);
            this.y += 12;
        }
        if (TabGui.extended) {
            final ArrayList<Module> mods = ModuleManager.getModulesInCategory(this.categories[TabGui.selected]);
            if (TabGui.selectedMod < 0) {
                TabGui.selectedMod = 0;
            }
            if (TabGui.selectedMod > mods.size() - 1) {
                TabGui.selectedMod = mods.size() - 1;
            }
            int width = 60;
            for (final Module m : mods) {
                final int newWidth = FontUtils.getStringWidth(false, m.getName()) + 2;
                if (newWidth > width) {
                    width = newWidth;
                }
            }
            for (final Module m : mods) {
                final int color = mods.get(TabGui.selectedMod).equals(m) ? Rainbow.getIntWithOpacity(100) : (m.isEnabled() ? 1712416273 : 1712394513);
                final int yy = this.modY;
                if (mods.get(TabGui.selectedMod).equals(m)) {
                    Gui.drawRect(64, yy, 64 + width, yy + 12, color);
                }
                else {
                    Gui.drawRect(64, yy, 64 + width, yy + 12, color);
                }
                FontUtils.drawStringWithShadow(false, m.getName(), 66, yy + 2, -1);
                if (mods.get(TabGui.selectedMod).equals(m)) {
                    TabGui.currentMod = m;
                }
                this.modY += 12;
            }
        }
    }
    
    static {
        TabGui.selected = 0;
        TabGui.extended = false;
        TabGui.selectedMod = 0;
        TabGui.currentMod = null;
    }
}
