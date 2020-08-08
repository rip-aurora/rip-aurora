package me.memeszz.aurora.command;

import me.memeszz.aurora.util.Wrapper;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.command.commands.ClientnameCommand;
import me.memeszz.aurora.command.commands.EnemyCommand;
import me.memeszz.aurora.command.commands.LoadAnnouncerCommand;
import me.memeszz.aurora.command.commands.MiddleXCommand;
import me.memeszz.aurora.command.commands.OpenFolderCommand;
import me.memeszz.aurora.command.commands.ConfigCommand;
import me.memeszz.aurora.command.commands.MacroCommand;
import me.memeszz.aurora.command.commands.RainbowSpeedCommand;
import me.memeszz.aurora.command.commands.FriendCommand;
import me.memeszz.aurora.command.commands.PrefixCommand;
import me.memeszz.aurora.command.commands.ModsCommand;
import me.memeszz.aurora.command.commands.CmdsCommand;
import me.memeszz.aurora.command.commands.SetCommand;
import me.memeszz.aurora.command.commands.DrawnCommand;
import me.memeszz.aurora.command.commands.ToggleCommand;
import me.memeszz.aurora.command.commands.BindCommand;
import me.memeszz.aurora.command.commands.CoordsCommand;
import java.util.ArrayList;

public class CommandManager
{
    private static ArrayList<Command> commands;
    boolean b;
    
    public static void initCommands() {
        CommandManager.commands = new ArrayList<Command>();
        addCommand(new CoordsCommand());
        addCommand(new BindCommand());
        addCommand(new ToggleCommand());
        addCommand(new DrawnCommand());
        addCommand(new SetCommand());
        addCommand(new CmdsCommand());
        addCommand(new ModsCommand());
        addCommand(new PrefixCommand());
        addCommand(new FriendCommand());
        addCommand(new RainbowSpeedCommand());
        addCommand(new MacroCommand());
        addCommand(new ConfigCommand());
        addCommand(new OpenFolderCommand());
        addCommand(new MiddleXCommand());
        addCommand(new LoadAnnouncerCommand());
        addCommand(new EnemyCommand());
        addCommand(new ClientnameCommand());
    }
    
    public static void addCommand(final Command c) {
        CommandManager.commands.add(c);
    }
    
    public static ArrayList<Command> getCommands() {
        return CommandManager.commands;
    }
    
    public void callCommand(final String input) {
        final String[] split = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        final String command = split[0];
        final String args = input.substring(command.length()).trim();
        this.b = false;
        final String[] array;
        int length;
        int i = 0;
        String s;
        final String anotherString;
        final String s2;
        CommandManager.commands.forEach(c -> {
            c.getAlias();
            for (length = array.length; i < length; ++i) {
                s = array[i];
                if (s.equalsIgnoreCase(anotherString)) {
                    this.b = true;
                    try {
                        c.onCommand(s2, s2.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                    }
                    catch (Exception e) {
                        Wrapper.sendClientMessage(ChatFormatting.RED + c.getSyntax());
                    }
                }
            }
            return;
        });
        if (!this.b) {
            Wrapper.sendClientMessage(ChatFormatting.RED + "Unknown command!");
        }
    }
}
