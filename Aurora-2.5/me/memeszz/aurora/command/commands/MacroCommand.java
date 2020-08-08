
package me.memeszz.aurora.command.commands;

import me.memeszz.aurora.util.Wrapper;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.util.macro.Macro;
import org.lwjgl.input.Keyboard;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.command.Command;

public class MacroCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "macro", "macros" };
    }
    
    @Override
    public String getSyntax() {
        return "macro <add | del> <key> <value>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("add")) {
            Aurora.getInstance().macroManager.delMacro(Aurora.getInstance().macroManager.getMacroByKey(Keyboard.getKeyIndex(args[1])));
            Aurora.getInstance().macroManager.addMacro(new Macro(Keyboard.getKeyIndex(args[1].toUpperCase()), args[2].replace("_", " ")));
            Wrapper.sendClientMessage(ChatFormatting.GREEN + "Added" + ChatFormatting.GRAY + " macro for key \"" + args[1].toUpperCase() + "\" with value \"" + args[2].replace("_", " ") + "\".");
        }
        if (args[0].equalsIgnoreCase("del")) {
            if (Aurora.getInstance().macroManager.getMacros().contains(Aurora.getInstance().macroManager.getMacroByKey(Keyboard.getKeyIndex(args[1].toUpperCase())))) {
                Aurora.getInstance().macroManager.delMacro(Aurora.getInstance().macroManager.getMacroByKey(Keyboard.getKeyIndex(args[1].toUpperCase())));
                Wrapper.sendClientMessage(ChatFormatting.RED + "Removed " + ChatFormatting.GRAY + "macro for key \"" + args[1].toUpperCase() + "\".");
            }
            else {
                Wrapper.sendClientMessage(ChatFormatting.RED + "That macro doesn't exist!");
            }
        }
    }
}
