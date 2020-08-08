package me.memeszz.aurora.module.modules.misc;

import me.memeszz.aurora.command.Command;
import me.memeszz.aurora.util.friends.Friends;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class FriendsTab extends Module
{
    public static FriendsTab INSTANCE;
    public Setting.i tabsize;
    
    public FriendsTab() {
        super("FriendsTab", Category.MISC);
        this.tabsize = this.registerI("Tabsize", 255, 1, 255);
        FriendsTab.INSTANCE = this;
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String dname = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (Friends.isFriend(dname)) {
            return String.format("%sa%s", Command.SECTIONSIGN(), dname);
        }
        return dname;
    }
}
