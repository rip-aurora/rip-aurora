
package me.memeszz.aurora.module.modules.gui;

import java.util.Iterator;
import me.memeszz.aurora.util.friends.Friends;
import net.minecraft.entity.player.EntityPlayer;
import me.memeszz.aurora.util.Wrapper;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.Aurora;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class AuroraGang extends Module
{
    public Setting.b customFont;
    private Setting.i x;
    private Setting.i y;
    public static String friends;
    private String str;
    private Setting.mode mode;
    
    public AuroraGang() {
        super("FriendsList", Category.GUI, "Displays Friends Names When In Range");
        this.setDrawn(false);
    }
    
    @Override
    public void setup() {
        this.x = this.registerI("X", 100, 0, 1000);
        this.y = this.registerI("Y", 100, 0, 1000);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("AuroraGang");
        modes.add("GoonSquad");
        this.mode = this.registerMode("Mode", modes, "AuroraGang");
    }
    
    @Override
    public void onRender() {
        int y = 2;
        if (ClickGuiModule.customFont.getValue()) {
            Aurora.fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + this.mode.getValue(), this.x.getValue(), this.y.getValue() - 10, 16777215);
        }
        else {
            Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + this.mode.getValue(), (float)this.x.getValue(), (float)(this.y.getValue() - 10), 16777215);
        }
        for (final Object o : AuroraGang.mc.world.getLoadedEntityList()) {
            if (o instanceof EntityPlayer && ((EntityPlayer)o).getName() != AuroraGang.mc.player.getName() && Friends.isFriend(((EntityPlayer)o).getName())) {
                AuroraGang.friends = ((EntityPlayer)o).getGameProfile().getName();
                this.str = " " + AuroraGang.friends;
                if (ClickGuiModule.customFont.getValue()) {
                    Aurora.fontRenderer.drawStringWithShadow(this.str, this.x.getValue(), y + this.y.getValue(), 16755200);
                }
                else {
                    Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(this.str, (float)this.x.getValue(), (float)(y + this.y.getValue()), 16755200);
                }
                y += 12;
            }
        }
    }
}
