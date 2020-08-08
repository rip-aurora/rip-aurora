
package me.memeszz.aurora.module.modules.render;

import me.memeszz.aurora.module.Module;

public class OldChangeItemAnim extends Module
{
    public OldChangeItemAnim() {
        super("ItemAnim", Category.RENDER);
    }
    
    @Override
    public void onUpdate() {
        if (OldChangeItemAnim.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            OldChangeItemAnim.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            OldChangeItemAnim.mc.entityRenderer.itemRenderer.itemStackMainHand = OldChangeItemAnim.mc.player.getHeldItemMainhand();
        }
    }
}
