package me.memeszz.aurora.module.modules.misc;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import me.memeszz.aurora.util.Wrapper;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.util.friends.Friends;
import org.lwjgl.input.Mouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.memeszz.aurora.module.Module;

public class MiddleClickFriends extends Module
{
    public MiddleClickFriends() {
        super("MCF", Category.MISC, "Middle click players to add / remove them as a friend");
    }
    
    @Listener
    public void mouseInputEvent(final InputEvent.MouseInputEvent event) {
        if (MiddleClickFriends.mc.objectMouseOver.typeOfHit.equals((Object)RayTraceResult.Type.ENTITY) && MiddleClickFriends.mc.objectMouseOver.entityHit instanceof EntityPlayer && Mouse.getEventButton() == 2) {
            if (!MiddleClickFriends.mc.player.isOnLadder() && Friends.isFriend(MiddleClickFriends.mc.objectMouseOver.entityHit.getName())) {
                Aurora.getInstance().friends.delFriend(MiddleClickFriends.mc.objectMouseOver.entityHit.getName());
                Wrapper.sendClientMessage(ChatFormatting.RED + "Removed " + MiddleClickFriends.mc.objectMouseOver.entityHit.getName() + " from friends list");
            }
            else {
                Aurora.getInstance().friends.addFriend(MiddleClickFriends.mc.objectMouseOver.entityHit.getName());
                Wrapper.sendClientMessage(ChatFormatting.AQUA + "Added " + MiddleClickFriends.mc.objectMouseOver.entityHit.getName() + " to friends list");
            }
        }
    }
}
