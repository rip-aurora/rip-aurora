package me.memeszz.aurora.module.modules.render;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketParticles;
import me.memeszz.aurora.event.EventStageable;
import me.memeszz.aurora.event.events.PacketEvent;
import me.memeszz.aurora.module.Module;

public class NoLag extends Module
{
    public NoLag() {
        super("NoLag", Category.MISC);
    }
    
    @Listener
    public void onUpdate(final PacketEvent.Receive event) {
        if (event.getStage() == EventStageable.EventStage.PRE) {
            if (event.getPacket() instanceof SPacketParticles || event.getPacket() instanceof SPacketEffect) {
                event.setCanceled(true);
            }
            if (event.getPacket() instanceof SPacketSoundEffect) {
                final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
                if (packet.getCategory() == SoundCategory.PLAYERS && packet.getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
