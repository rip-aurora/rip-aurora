
package me.memeszz.aurora.command.commands;

import org.lwjgl.opengl.Display;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.command.Command;

public class ClientnameCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "name", "modname", "clientname", "suffix", "watermark" };
    }
    
    @Override
    public String getSyntax() {
        return "name <new name>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (!args[0].replace("__", " ").equalsIgnoreCase("")) {
            Aurora.MODNAME = args[0].replace("__", " ");
            Display.setTitle(Aurora.MODNAME + " " + "2.5");
            Command.sendClientMessage("set client name to " + args[0].replace("__", " "));
        }
        else {
            Command.sendClientMessage(this.getSyntax());
        }
    }
}
