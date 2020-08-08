
package me.memeszz.aurora.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.util.font.CFontRenderer;

public class Wrapper
{
    private static CFontRenderer fontRenderer;
    public static final Aurora mod;
    private static String prefix;
    public static final Minecraft mc;
    public static volatile Wrapper INSTANCE;
    
    public static void init() {
        Wrapper.fontRenderer = Aurora.fontRenderer;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    
    public static World getWorld() {
        return (World)getMinecraft().world;
    }
    
    public static int getKey(final String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }
    
    public static void sendErrorMessage(final String message) {
        if (Wrapper.mc.player == null) {
            return;
        }
    }
    
    public static void sendClientMessage(final String message) {
        if (Wrapper.mc.player == null) {
            return;
        }
        final ITextComponent itc = new TextComponentString(Wrapper.prefix + ChatFormatting.GRAY + message).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (ITextComponent)new TextComponentString("Aurora"))));
        Wrapper.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(itc, 5936);
    }
    
    public static CFontRenderer getFontRenderer() {
        return Wrapper.fontRenderer;
    }
    
    static {
        Wrapper.INSTANCE = new Wrapper();
        mc = Minecraft.getMinecraft();
        mod = Aurora.getInstance();
        Wrapper.prefix = ChatFormatting.DARK_GRAY + "[" + ChatFormatting.AQUA + "Aurora" + ChatFormatting.DARK_GRAY + "] ";
    }
}
