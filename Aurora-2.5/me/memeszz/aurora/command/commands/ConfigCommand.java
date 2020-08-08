
package me.memeszz.aurora.command.commands;

import me.memeszz.aurora.util.Wrapper;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.command.Command;

public class ConfigCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "saveconfig", "savecfg" };
    }
    
    @Override
    public String getSyntax() {
        return "saveconfig";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Aurora.saveConfig();
        Wrapper.sendClientMessage("Saved Aurora config!");
    }
}
