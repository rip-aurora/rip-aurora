package me.memeszz.aurora.module.modules.combat;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.memeszz.aurora.event.events.PacketEvent;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Criticals extends Module
{
    Setting.mode mode;
    
    public Criticals() {
        super("Criticals", Category.COMBAT, "Increases chance for a critical hit");
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Packet");
        modes.add("Jump");
        this.mode = this.registerMode("Mode", modes, "Packet");
    }
    
    @Listener
    public void onUpdate(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && Criticals.mc.player.onGround) {
            if (this.mode.getValue().equals("Jump")) {
                Criticals.mc.player.jump();
            }
            if (this.mode.getValue().equals("Packet")) {
                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.10000000149011612, Criticals.mc.player.posZ, false));
                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.mode.getValue() + "§7]";
    }
}
