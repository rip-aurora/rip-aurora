
package me.memeszz.aurora.command.commands;

import me.memeszz.aurora.util.Wrapper;
import me.memeszz.aurora.command.Command;

public class PrefixCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "prefix", "setprefix" };
    }
    
    @Override
    public String getSyntax() {
        return "prefix <character>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Command.setPrefix(args[0]);
        Wrapper.sendClientMessage("Command prefix set to " + Command.getPrefix());
    }
}
