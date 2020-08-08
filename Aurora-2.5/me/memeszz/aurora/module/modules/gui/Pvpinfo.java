
package me.memeszz.aurora.module.modules.gui;

import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.util.font.FontUtils;
import net.minecraft.init.Items;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Pvpinfo extends Module
{
    int c;
    Setting.i x;
    Setting.i y;
    Setting.b totems;
    
    public Pvpinfo() {
        super("PvPInfo", Category.GUI);
        this.setDrawn(false);
    }
    
    @Override
    public void setup() {
        this.x = this.registerI("X", 900, 0, 1920);
        this.y = this.registerI("Y", 500, 0, 1080);
        this.totems = this.registerB("Totems", true);
    }
    
    public static int getItems(final Item i) {
        return Pvpinfo.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum() + Pvpinfo.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum();
    }
    
    public String getTotemsStr() {
        return String.valueOf(getItems(Items.TOTEM_OF_UNDYING));
    }
    
    public int getTotems() {
        return getItems(Items.TOTEM_OF_UNDYING);
    }
    
    @Override
    public void onRender() {
        if (Pvpinfo.mc.player == null || Pvpinfo.mc.world == null) {
            return;
        }
        int totemColor;
        if (this.getTotems() < 1) {
            totemColor = 16711680;
        }
        else {
            totemColor = 65280;
        }
        if (this.totems.getValue()) {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), this.getTotemsStr(), this.x.getValue(), this.y.getValue() + 20, totemColor);
        }
        final int enabledC = 65280;
        final int disabledC = 16711680;
        if (ModuleManager.getModuleByName("CrystalAura").isEnabled()) {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "CA", this.x.getValue(), this.y.getValue() + 10, enabledC);
        }
        else {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "CA", this.x.getValue(), this.y.getValue() + 10, disabledC);
        }
        if (ModuleManager.getModuleByName("AutoTrap").isEnabled()) {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "AT", this.x.getValue(), this.y.getValue(), enabledC);
        }
        else {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "AT", this.x.getValue(), this.y.getValue(), disabledC);
        }
        if (ModuleManager.getModuleByName("Surround").isEnabled()) {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "SD", this.x.getValue(), this.y.getValue() - 10, enabledC);
        }
        else {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "SD", this.x.getValue(), this.y.getValue() - 10, disabledC);
        }
        if (ModuleManager.getModuleByName("KillAura").isEnabled()) {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "KA", this.x.getValue(), this.y.getValue() - 20, enabledC);
        }
        else {
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), "KA", this.x.getValue(), this.y.getValue() - 20, disabledC);
        }
    }
}
