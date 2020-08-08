
package me.memeszz.aurora.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.util.Wrapper;
import me.memeszz.aurora.event.EventProcessor;
import me.memeszz.aurora.command.Command;

public class RainbowSpeedCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "rainbowspeed", "rainbow" };
    }
    
    @Override
    public String getSyntax() {
        return "rainbowspeed <speed>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args.length == 1) {
            final int i = Integer.parseInt(args[0]);
            if (i <= 0) {
                EventProcessor.INSTANCE.setRainbowSpeed(0);
            }
            else {
                EventProcessor.INSTANCE.setRainbowSpeed(i);
            }
            Wrapper.sendClientMessage("Rainbow speed set to " + i);
        }
        else {
            Wrapper.sendClientMessage(ChatFormatting.RED + this.getSyntax());
        }
    }
}
