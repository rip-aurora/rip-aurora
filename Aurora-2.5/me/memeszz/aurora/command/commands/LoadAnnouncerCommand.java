
package me.memeszz.aurora.command.commands;

import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.command.Command;

public class LoadAnnouncerCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "loadannouncer" };
    }
    
    @Override
    public String getSyntax() {
        return "loadannouncer";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Aurora.getInstance().configUtils.loadAnnouncer();
        Command.sendClientMessage("Loaded Announcer file");
    }
}
