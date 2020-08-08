
package me.memeszz.aurora.command.commands;

import me.memeszz.aurora.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.command.Command;

public class ToggleCommand extends Command
{
    boolean found;
    
    @Override
    public String[] getAlias() {
        return new String[] { "toggle", "t" };
    }
    
    @Override
    public String getSyntax() {
        return "toggle <Module>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        this.found = false;
        ModuleManager.getModules().forEach(m -> {
            if (m.getName().equalsIgnoreCase(args[0])) {
                if (m.isEnabled()) {
                    m.disable();
                    this.found = true;
                    Command.sendClientMessage(m.getName() + ChatFormatting.RED + " disabled!");
                }
                else if (!m.isEnabled()) {
                    m.enable();
                    this.found = true;
                    Command.sendClientMessage(m.getName() + ChatFormatting.GREEN + " enabled!");
                }
            }
            return;
        });
        if (!this.found && args.length == 1) {
            Command.sendClientMessage(ChatFormatting.DARK_RED + "Module not found!");
        }
    }
}
