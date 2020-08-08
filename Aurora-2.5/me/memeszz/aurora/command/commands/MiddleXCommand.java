
package me.memeszz.aurora.command.commands;

import me.memeszz.aurora.util.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import me.memeszz.aurora.command.Command;

public class MiddleXCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "getmiddlex", "middlex", "getmiddle" };
    }
    
    @Override
    public String getSyntax() {
        return this.getAlias()[0];
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Wrapper.sendClientMessage(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2 + "");
    }
}
