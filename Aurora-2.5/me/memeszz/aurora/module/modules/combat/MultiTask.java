
package me.memeszz.aurora.module.modules.combat;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;
import me.memeszz.aurora.module.Module;

public class MultiTask extends Module
{
    public MultiTask() {
        super("MultiTask", Category.COMBAT);
    }
    
    @Override
    public void onUpdate() {
        if (MultiTask.mc.gameSettings.keyBindUseItem.isKeyDown() && MultiTask.mc.player.getActiveHand() == EnumHand.MAIN_HAND) {
            if (!(MultiTask.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock) && MultiTask.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                if (MultiTask.mc.player.getHeldItemOffhand().getItem() instanceof ItemFood && MultiTask.mc.gameSettings.keyBindUseItem.isKeyDown() && MultiTask.mc.gameSettings.keyBindAttack.isKeyDown()) {
                    MultiTask.mc.player.setActiveHand(EnumHand.OFF_HAND);
                    final RayTraceResult r = MultiTask.mc.player.rayTrace(6.0, MultiTask.mc.getRenderPartialTicks());
                    MultiTask.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            else {
                final RayTraceResult r = MultiTask.mc.player.rayTrace(6.0, MultiTask.mc.getRenderPartialTicks());
                MultiTask.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(r.getBlockPos(), r.sideHit, EnumHand.OFF_HAND, 0.0f, 0.0f, 0.0f));
            }
        }
    }
}
