package me.memeszz.aurora.module.modules.render;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.util.EnumHand;
import me.memeszz.aurora.mixin.accessor.IItemRenderer;
import me.memeszz.aurora.event.events.UpdateEvent;
import me.memeszz.aurora.module.Module;

public class OldChangeItemAnim extends Module
{
    public OldChangeItemAnim() {
        super("ItemAnim", Category.RENDER);
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        if (((IItemRenderer)OldChangeItemAnim.mc.entityRenderer.itemRenderer).getPrevEquippedProgressMainHand() >= 0.9) {
            ((IItemRenderer)OldChangeItemAnim.mc.entityRenderer.itemRenderer).setEquippedProgressMainHand(1.0f);
            ((IItemRenderer)OldChangeItemAnim.mc.entityRenderer.itemRenderer).setItemStackMainHand(OldChangeItemAnim.mc.player.getHeldItem(EnumHand.MAIN_HAND));
        }
        if (((IItemRenderer)OldChangeItemAnim.mc.entityRenderer.itemRenderer).getPrevEquippedProgressOffHand() >= 0.9) {
            ((IItemRenderer)OldChangeItemAnim.mc.entityRenderer.itemRenderer).setEquippedProgressOffHand(1.0f);
            ((IItemRenderer)OldChangeItemAnim.mc.entityRenderer.itemRenderer).setItemStackOffHand(OldChangeItemAnim.mc.player.getHeldItem(EnumHand.OFF_HAND));
        }
    }
}
