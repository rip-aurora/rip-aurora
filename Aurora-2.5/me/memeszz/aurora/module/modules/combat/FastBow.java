
package me.memeszz.aurora.module.modules.combat;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.item.ItemBow;
import me.memeszz.aurora.module.Module;

public class FastBow extends Module
{
    public FastBow() {
        super("FastBow", Category.COMBAT, "Spams your bow");
    }
    
    @Override
    public void setup() {
    }
    
    @Override
    public void onUpdate() {
        if (FastBow.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && FastBow.mc.player.isHandActive() && FastBow.mc.player.getItemInUseMaxCount() >= 3) {
            FastBow.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, FastBow.mc.player.getHorizontalFacing()));
            FastBow.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(FastBow.mc.player.getActiveHand()));
            FastBow.mc.player.stopActiveHand();
        }
    }
}
