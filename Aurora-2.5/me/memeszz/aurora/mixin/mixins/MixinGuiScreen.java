
package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;
import me.memeszz.aurora.module.modules.render.ShulkerPreview;
import net.minecraft.item.ItemShulkerBox;
import me.memeszz.aurora.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { GuiScreen.class }, priority = 9999)
public abstract class MixinGuiScreen
{
    @Shadow
    public Minecraft mc;
    
    @Inject(method = { "renderToolTip" }, at = { @At("HEAD") }, cancellable = true)
    public void renderToolTip(final ItemStack is, final int x, final int y, final CallbackInfo ci) {
        if (ModuleManager.isModuleEnabled("ShulkerPreview") && is.getItem() instanceof ItemShulkerBox) {
            final NBTTagCompound tagCompound = is.getTagCompound();
            if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10)) {
                final NBTTagCompound blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag");
                if (blockEntityTag.hasKey("Items", 9)) {
                    ci.cancel();
                    ShulkerPreview.nbt = blockEntityTag;
                    ShulkerPreview.itemStack = is;
                    ShulkerPreview.active = true;
                    if (!(ShulkerPreview.pinned = Keyboard.isKeyDown(42))) {
                        ShulkerPreview.drawX = x;
                        ShulkerPreview.drawY = y;
                    }
                }
            }
        }
    }
}
