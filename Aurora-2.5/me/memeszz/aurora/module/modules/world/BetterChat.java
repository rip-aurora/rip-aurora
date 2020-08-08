
package me.memeszz.aurora.module.modules.world;

import me.memeszz.aurora.util.friends.Friend;
import net.minecraft.util.text.Style;
import java.util.function.Predicate;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import me.memeszz.aurora.util.friends.Friends;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class BetterChat extends Module
{
    public Setting.b clearBkg;
    Setting.b nameHighlight;
    Setting.b friendHighlight;
    @EventHandler
    private final Listener<ClientChatReceivedEvent> chatReceivedEventListener;
    
    public BetterChat() {
        super("BetterChat", Category.WORLD);
        String name;
        String s;
        Style style;
        this.chatReceivedEventListener = new Listener<ClientChatReceivedEvent>(event -> {
            if (BetterChat.mc.player != null) {
                name = BetterChat.mc.player.getName().toLowerCase();
                if (this.friendHighlight.getValue()) {
                    if (!event.getMessage().getUnformattedText().startsWith("<" + BetterChat.mc.player.getName() + ">")) {
                        Friends.getFriends().forEach(f -> {
                            if (event.getMessage().getUnformattedText().contains(f.getName())) {
                                event.getMessage().setStyle(event.getMessage().getStyle().setColor(TextFormatting.LIGHT_PURPLE));
                            }
                            return;
                        });
                    }
                }
                if (this.nameHighlight.getValue()) {
                    s = ChatFormatting.GOLD + "" + ChatFormatting.BOLD + BetterChat.mc.player.getName() + ChatFormatting.RESET;
                    style = event.getMessage().getStyle();
                    if (!event.getMessage().getUnformattedText().startsWith("<" + BetterChat.mc.player.getName() + ">") && event.getMessage().getUnformattedText().toLowerCase().contains(name)) {
                        event.getMessage().getStyle().setParentStyle(style.setBold(Boolean.valueOf(true)).setColor(TextFormatting.GOLD));
                    }
                }
            }
        }, (Predicate<ClientChatReceivedEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.clearBkg = this.registerB("Clear", true);
        this.nameHighlight = this.registerB("NameHighlight", false);
        this.friendHighlight = this.registerB("FriendHighlight", false);
    }
}
