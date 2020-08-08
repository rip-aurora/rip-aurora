
package me.memeszz.aurora.module.modules.world;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import java.util.Date;
import java.text.SimpleDateFormat;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class ChatTimeStamps extends Module
{
    Setting.mode format;
    Setting.mode color;
    Setting.mode decoration;
    Setting.b space;
    @EventHandler
    private final Listener<ClientChatReceivedEvent> listener;
    
    public ChatTimeStamps() {
        super("ChatTimeStamps", Category.WORLD);
        final String decoLeft;
        String decoRight;
        final String dateFormat;
        final String date;
        final TextComponentString textComponentString;
        final TextComponentString time;
        this.listener = new Listener<ClientChatReceivedEvent>(event -> {
            decoLeft = (this.decoration.getValue().equalsIgnoreCase(" ") ? "" : this.decoration.getValue().split(" ")[0]);
            decoRight = (this.decoration.getValue().equalsIgnoreCase(" ") ? "" : this.decoration.getValue().split(" ")[1]);
            if (this.space.getValue()) {
                decoRight += " ";
            }
            dateFormat = this.format.getValue().replace("H24", "k").replace("H12", "h");
            date = new SimpleDateFormat(dateFormat).format(new Date());
            new TextComponentString(ChatFormatting.getByName(this.color.getValue()) + decoLeft + date + decoRight + ChatFormatting.RESET);
            time = textComponentString;
            event.setMessage(time.appendSibling(event.getMessage()));
            return;
        }, (Predicate<ClientChatReceivedEvent>[])new Predicate[0]);
        final ArrayList<String> formats = new ArrayList<String>();
        formats.add("H24:mm");
        formats.add("H12:mm");
        formats.add("H12:mm a");
        formats.add("H24:mm:ss");
        formats.add("H12:mm:ss");
        formats.add("H12:mm:ss a");
        final ArrayList<String> deco = new ArrayList<String>();
        deco.add("< >");
        deco.add("[ ]");
        deco.add("{ }");
        deco.add(" ");
        final ArrayList<String> colors = new ArrayList<String>();
        for (final ChatFormatting cf : ChatFormatting.values()) {
            colors.add(cf.getName());
        }
        this.format = this.registerMode("Format", formats, "H12:mm");
        this.color = this.registerMode("Color", colors, ChatFormatting.AQUA.getName());
        this.decoration = this.registerMode("Deco", deco, "< >");
        this.space = this.registerB("Space", true);
    }
}
