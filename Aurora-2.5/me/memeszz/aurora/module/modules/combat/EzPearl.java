
package me.memeszz.aurora.module.modules.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import me.memeszz.aurora.util.Wrapper;
import me.memeszz.aurora.module.Module;

public class EzPearl extends Module
{
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int delay;
    
    public EzPearl() {
        super("EasyPearl", Category.COMBAT, "Throws a pearl");
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
    }
    
    public void onEnable() {
        this.playerHotbarSlot = EzPearl.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    public void onDisable() {
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.delay = 0;
    }
    
    @Override
    public void onUpdate() {
        ++this.delay;
        final int pearlSlot = this.findPearlInHotbar();
        if (pearlSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != pearlSlot) {
            EzPearl.mc.player.inventory.currentItem = pearlSlot;
            this.lastHotbarSlot = pearlSlot;
        }
        if (this.delay > 1) {
            EzPearl.mc.rightClickMouse();
            this.delay = 0;
            this.toggle();
        }
    }
    
    private int findPearlInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            final Item item;
            if (stack != ItemStack.EMPTY && (item = stack.getItem()) instanceof ItemEnderPearl) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}
