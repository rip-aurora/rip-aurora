package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Redirect;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.event.events.GuiScreenDisplayedEvent;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.memeszz.aurora.Aurora;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.util.Timer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import me.memeszz.aurora.mixin.accessor.IMinecraft;

@Mixin({ Minecraft.class })
public abstract class MixinMinecraft implements IMinecraft
{
    @Shadow
    public EntityPlayerSP player;
    @Shadow
    public PlayerControllerMP playerController;
    
    @Accessor
    @Override
    public abstract Timer getTimer();
    
    @Accessor
    @Override
    public abstract void setSession(final Session p0);
    
    @Accessor
    @Override
    public abstract void setRightClickDelayTimer(final int p0);
    
    @Accessor
    @Override
    public abstract Session getSession();
    
    @Accessor
    @Override
    public abstract ServerData getCurrentServerData();
    
    @Inject(method = { "shutdown()V" }, at = { @At("HEAD") })
    public void saveSettingsOnShutdown(final CallbackInfo ci) {
        Aurora.saveConfig();
        System.out.println("Saved Aurora config!");
    }
    
    @Inject(method = { "displayGuiScreen" }, at = { @At("HEAD") })
    private void displayGuiScreen(final GuiScreen guiScreenIn, final CallbackInfo info) {
        final GuiScreenDisplayedEvent screenEvent = new GuiScreenDisplayedEvent(guiScreenIn);
        Aurora.getInstance().getEventManager().dispatchEvent(screenEvent);
    }
    
    @Redirect(method = { "sendClickBlockToController" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    private boolean isHandActive(final EntityPlayerSP player) {
        return !ModuleManager.isModuleEnabled("MultiTask") && this.player.isHandActive();
    }
    
    @Redirect(method = { "rightClickMouse" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z"))
    private boolean isHittingBlock(final PlayerControllerMP playerControllerMP) {
        return !ModuleManager.isModuleEnabled("MultiTask") && this.playerController.getIsHittingBlock();
    }
}
