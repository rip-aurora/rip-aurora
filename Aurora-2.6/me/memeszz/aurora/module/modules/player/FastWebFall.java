package me.memeszz.aurora.module.modules.player;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import me.memeszz.aurora.mixin.accessor.IEntity;
import me.memeszz.aurora.event.events.UpdateEvent;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class FastWebFall extends Module
{
    Setting.mode mode;
    Setting.d speedDown;
    
    public FastWebFall() {
        super("FastFallWeb", Category.PLAYER);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("2b");
        modes.add("Non2b");
        this.mode = this.registerMode("Mode", modes, "2b");
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        if (((IEntity)FastWebFall.mc.player).getIsInWeb()) {
            if (this.mode.getValue().equalsIgnoreCase("non2b")) {
                for (int i = 0; i < 15; ++i) {
                    final EntityPlayerSP player = FastWebFall.mc.player;
                    --player.motionY;
                }
            }
            if (this.mode.getValue().equalsIgnoreCase("2b")) {
                FastWebFall.mc.player.motionY = -0.22000000000000003;
            }
        }
    }
}
