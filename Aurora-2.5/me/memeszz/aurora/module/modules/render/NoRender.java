
package me.memeszz.aurora.module.modules.render;

import net.minecraft.init.MobEffects;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class NoRender extends Module
{
    public Setting.b armor;
    Setting.b fire;
    Setting.b blind;
    Setting.b nausea;
    public Setting.b hurtCam;
    @EventHandler
    public Listener<RenderBlockOverlayEvent> blockOverlayEventListener;
    
    public NoRender() {
        super("NoRender", Category.RENDER, "Prevents rendering some things");
        this.blockOverlayEventListener = new Listener<RenderBlockOverlayEvent>(event -> {
            if (this.fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
                event.setCanceled(true);
            }
        }, (Predicate<RenderBlockOverlayEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.armor = this.registerB("Armor", false);
        this.fire = this.registerB("Fire", false);
        this.blind = this.registerB("Blindness", false);
        this.nausea = this.registerB("Nausea", false);
        this.hurtCam = this.registerB("HurtCam", false);
    }
    
    @Override
    public void onUpdate() {
        if (this.blind.getValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (this.nausea.getValue() && NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
        }
    }
}
