
package me.memeszz.aurora.module.modules.world;

import java.util.function.Predicate;
import me.memeszz.aurora.command.Command;
import me.memeszz.aurora.event.events.PlayerLeaveEvent;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PlayerJoinEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Welcomer extends Module
{
    Setting.b publicS;
    @EventHandler
    private final Listener<PlayerJoinEvent> listener1;
    @EventHandler
    private final Listener<PlayerLeaveEvent> listener2;
    
    public Welcomer() {
        super("Welcomer", Category.WORLD, "Sends a message when someone joins the server");
        this.listener1 = new Listener<PlayerJoinEvent>(event -> {
            if (this.publicS.getValue()) {
                Welcomer.mc.player.sendChatMessage(event.getName() + " joined the game");
            }
            else {
                Command.sendClientMessage(event.getName() + " joined the game");
            }
            return;
        }, (Predicate<PlayerJoinEvent>[])new Predicate[0]);
        this.listener2 = new Listener<PlayerLeaveEvent>(event -> {
            if (this.publicS.getValue()) {
                Welcomer.mc.player.sendChatMessage(event.getName() + " left the game");
            }
            else {
                Command.sendClientMessage(event.getName() + " left the game");
            }
            return;
        }, (Predicate<PlayerLeaveEvent>[])new Predicate[0]);
        this.publicS = this.registerB("Public", false);
    }
}
