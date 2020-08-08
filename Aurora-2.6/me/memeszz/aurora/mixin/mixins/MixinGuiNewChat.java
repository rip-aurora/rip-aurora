package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.Gui;
import me.memeszz.aurora.module.modules.world.BetterChat;
import me.memeszz.aurora.module.ModuleManager;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(final int left, final int top, final int right, final int bottom, final int color) {
        if (!ModuleManager.isModuleEnabled("BetterChat") || !((BetterChat)ModuleManager.getModuleByName("BetterChat")).clearBkg.getValue()) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}
