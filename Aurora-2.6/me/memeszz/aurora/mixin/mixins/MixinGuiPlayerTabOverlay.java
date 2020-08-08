package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.memeszz.aurora.module.modules.misc.FriendsTab;
import java.util.List;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiPlayerTabOverlay.class })
public abstract class MixinGuiPlayerTabOverlay
{
    @Redirect(method = { "renderPlayerlist" }, at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;"))
    public List subList(final List list, final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, FriendsTab.INSTANCE.isEnabled() ? Math.min(FriendsTab.INSTANCE.tabsize.getValue(), list.size()) : toIndex);
    }
    
    @Inject(method = { "getPlayerName" }, at = { @At("HEAD") }, cancellable = true)
    public void getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn, final CallbackInfoReturnable returnable) {
        if (FriendsTab.INSTANCE.isEnabled()) {
            returnable.cancel();
            returnable.setReturnValue(FriendsTab.getPlayerName(networkPlayerInfoIn));
        }
    }
}
