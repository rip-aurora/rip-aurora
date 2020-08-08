
package me.memeszz.aurora.event.events;

import net.minecraft.entity.Entity;
import me.memeszz.aurora.event.AuroraEvent;

public class EntityUseTotemEvent extends AuroraEvent
{
    private final Entity entity;
    
    public EntityUseTotemEvent(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
