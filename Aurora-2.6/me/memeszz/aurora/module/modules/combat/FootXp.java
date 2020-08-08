package me.memeszz.aurora.module.modules.combat;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import me.memeszz.aurora.mixin.accessor.ICPacketPlayer;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketPlayer;
import me.memeszz.aurora.event.events.PacketEvent;
import me.memeszz.aurora.module.Module;

public class FootXp extends Module
{
    public FootXp() {
        super("FootXp", Category.COMBAT, "Increases chance for a critical hit");
    }
    
    @Listener
    public void onUpdate(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && FootXp.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
            final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            ((ICPacketPlayer)packet).setPitch(90.0f);
        }
    }
}
