package me.memeszz.aurora.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import me.memeszz.aurora.command.CommandManager;
import me.memeszz.aurora.command.Command;

public class CmdsCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "commands", "cmds" };
    }
    
    @Override
    public String getSyntax() {
        return "commands";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final int size = CommandManager.getCommands().size();
        final TextComponentString msg = new TextComponentString("§7Commands: ");
        for (int i = 0; i < size; ++i) {
            final Command c = CommandManager.getCommands().get(i);
            if (c != null) {
                msg.appendSibling(new TextComponentString(c.getAlias()[0] + ((i == size - 1) ? "" : ", ")).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (ITextComponent)new TextComponentString(c.getSyntax())))));
            }
        }
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((ITextComponent)msg);
    }
}
