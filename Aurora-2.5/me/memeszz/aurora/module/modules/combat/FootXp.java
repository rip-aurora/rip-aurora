
package me.memeszz.aurora.module.modules.combat;

import java.util.function.Predicate;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.module.Module;

public class FootXp extends Module
{
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener;
    
    public FootXp() {
        super("FootXp", Category.COMBAT, "Increases chance for a critical hit");
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayer.Rotation && FootXp.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
                ((CPacketPlayer.Rotation)event.getPacket()).pitch = 90.0f;
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation && FootXp.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
                ((CPacketPlayer.PositionRotation)event.getPacket()).pitch = 90.0f;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
