
package me.memeszz.aurora.module.modules.misc;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import me.memeszz.aurora.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class ChatSuffix extends Module
{
    Setting.b blue;
    Setting.mode mode;
    @EventHandler
    private final Listener<PacketEvent.Send> listener;
    
    public ChatSuffix() {
        super("ChatSuffix", Category.MISC, "Adds a suffix to your messages");
        String old;
        String suffix;
        String s;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                if (!((CPacketChatMessage)event.getPacket()).getMessage().startsWith("/") && !((CPacketChatMessage)event.getPacket()).getMessage().startsWith(Command.getPrefix())) {
                    old = ((CPacketChatMessage)event.getPacket()).getMessage();
                    suffix = " \u23d0 ";
                    s = old + suffix;
                    if (this.blue.getValue()) {
                        s = old + "`" + suffix;
                    }
                    if (s.length() <= 255) {
                        ((CPacketChatMessage)event.getPacket()).message = s;
                    }
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Green");
        modes.add("Blue");
        this.mode = this.registerMode("Color", modes, "Green");
        this.blue = this.registerB("Blue", false);
    }
}
