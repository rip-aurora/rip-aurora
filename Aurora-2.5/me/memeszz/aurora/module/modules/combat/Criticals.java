
package me.memeszz.aurora.module.modules.combat;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.item.ItemSword;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Criticals extends Module
{
    Setting.mode mode;
    @EventHandler
    private final Listener<PacketEvent.Send> listener;
    
    public Criticals() {
        super("Criticals", Category.COMBAT, "Increases chance for a critical hit");
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (Criticals.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && Criticals.mc.player.onGround) {
                if (this.mode.getValue().equals("Jump")) {
                    Criticals.mc.player.jump();
                }
                if (this.mode.getValue().equals("Packet")) {
                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.10000000149011612, Criticals.mc.player.posZ, false));
                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Packet");
        modes.add("Jump");
        this.mode = this.registerMode("Mode", modes, "Packet");
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.mode.getValue() + "§7]";
    }
}
