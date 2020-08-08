
package me.memeszz.aurora.command.commands;

import java.io.File;
import java.awt.Desktop;
import me.memeszz.aurora.command.Command;

public class OpenFolderCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "openfolder", "folder" };
    }
    
    @Override
    public String getSyntax() {
        return "openfolder";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        try {
            Desktop.getDesktop().open(new File("Aurora"));
        }
        catch (Exception e) {
            Command.sendClientMessage("Error: " + e.getMessage());
        }
    }
}
