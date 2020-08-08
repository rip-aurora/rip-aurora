package me.memeszz.aurora.module.modules.world;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import java.util.Iterator;
import me.memeszz.aurora.util.Wrapper;
import net.minecraft.init.MobEffects;
import me.memeszz.aurora.event.events.UpdateEvent;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;
import me.memeszz.aurora.module.Module;

public class str2detect extends Module
{
    private final Set<EntityPlayer> str;
    public static final Minecraft mc;
    
    public str2detect() {
        super("StrengthDetect", Category.WORLD, "Tells you in chat when someone has str 2/1");
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        for (final EntityPlayer player : str2detect.mc.world.playerEntities) {
            if (player.equals((Object)str2detect.mc.player)) {
                continue;
            }
            if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(player)) {
                Wrapper.sendClientMessage(player.getDisplayNameString() + " Has Strength");
                this.str.add(player);
            }
            if (!this.str.contains(player)) {
                continue;
            }
            if (player.isPotionActive(MobEffects.STRENGTH)) {
                continue;
            }
            Wrapper.sendClientMessage(player.getDisplayNameString() + " Has Ran Out Of Strength");
            this.str.remove(player);
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
