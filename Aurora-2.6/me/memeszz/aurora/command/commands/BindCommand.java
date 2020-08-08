package me.memeszz.aurora.command.commands;

import me.memeszz.aurora.module.Module;
import me.memeszz.aurora.util.Wrapper;
import me.memeszz.aurora.module.ModuleManager;
import org.lwjgl.input.Keyboard;
import me.memeszz.aurora.command.Command;

public class BindCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "bind", "b" };
    }
    
    @Override
    public String getSyntax() {
        return "bind <Module> <Key>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final int key = Keyboard.getKeyIndex(args[1].toUpperCase());
        final int bind;
        ModuleManager.getModules().forEach(m -> {
            if (args[0].equalsIgnoreCase(m.getName())) {
                m.setBind(bind);
                Wrapper.sendClientMessage(args[0] + " bound to " + args[1].toUpperCase());
            }
        });
    }
}
