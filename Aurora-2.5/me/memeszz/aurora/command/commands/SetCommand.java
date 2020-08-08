
package me.memeszz.aurora.command.commands;

import java.util.Iterator;
import me.memeszz.aurora.module.Module;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.command.Command;

public class SetCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "set" };
    }
    
    @Override
    public String getSyntax() {
        return "set <Module> <Setting> <Value>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        for (final Module m : ModuleManager.getModules()) {
            if (m.getName().equalsIgnoreCase(args[0])) {
                System.out.println("no");
            }
        }
    }
}
