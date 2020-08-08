package me.memeszz.aurora.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.Style;
import me.memeszz.aurora.module.Module;
import net.minecraft.util.text.TextComponentString;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.command.Command;

public class ModsCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "modules", "mods" };
    }
    
    @Override
    public String getSyntax() {
        return "modules";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final int size = ModuleManager.getModules().size();
        final TextComponentString msg = new TextComponentString("§7Modules: §f ");
        for (int i = 0; i < size; ++i) {
            final Module mod = ModuleManager.getModules().get(i);
            if (mod != null) {
                msg.appendSibling(new TextComponentString((mod.isEnabled() ? "§a" : "§c") + mod.getName() + "§7" + ((i == size - 1) ? "" : ", ")).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (ITextComponent)new TextComponentString(mod.getCategory().name()))).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Command.getPrefix() + "toggle " + mod.getName()))));
            }
        }
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((ITextComponent)msg);
    }
}
