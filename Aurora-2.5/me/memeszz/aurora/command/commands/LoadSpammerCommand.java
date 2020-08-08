
package me.memeszz.aurora.command.commands;

import me.memeszz.aurora.util.Wrapper;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.module.modules.world.Spammer;
import me.memeszz.aurora.command.Command;

public class LoadSpammerCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "loadspammer" };
    }
    
    @Override
    public String getSyntax() {
        return "loadspammer";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Spammer.text.clear();
        Aurora.getInstance().configUtils.loadSpammer();
        Wrapper.sendClientMessage("Loaded Spammer File");
    }
}
