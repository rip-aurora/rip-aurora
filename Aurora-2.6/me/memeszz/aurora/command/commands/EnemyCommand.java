package me.memeszz.aurora.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.util.enemy.Enemies;
import me.memeszz.aurora.command.Command;

public class EnemyCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "enemy", "enemies", "e" };
    }
    
    @Override
    public String getSyntax() {
        return "enemy <add | del> <name>";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("add")) {
            if (!Enemies.getEnemies().contains(Enemies.getEnemyByName(args[1]))) {
                Enemies.addEnemy(args[1]);
                Command.sendClientMessage(ChatFormatting.GREEN + "Added enemy with name " + args[1]);
            }
            else {
                Command.sendClientMessage(ChatFormatting.DARK_RED + args[1] + " is already an enemy!");
            }
        }
        else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")) {
            Enemies.delEnemy(args[1]);
            Command.sendClientMessage(ChatFormatting.RED + "Removed enemy with name " + args[1]);
        }
        else {
            Command.sendClientMessage(this.getSyntax());
        }
    }
}
