package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.memeszz.aurora.module.modules.render.ShulkerPreview;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiContainer.class })
public abstract class MixinGuiContainer
{
    @Shadow
    public int guiLeft;
    @Shadow
    public int guiTop;
    
    @Inject(method = { "drawScreen" }, at = { @At("HEAD") }, cancellable = true)
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        ShulkerPreview.mouseX = mouseX;
        ShulkerPreview.mouseY = mouseY;
        ShulkerPreview.guiLeft = this.guiLeft;
        ShulkerPreview.guiTop = this.guiTop;
    }
}
