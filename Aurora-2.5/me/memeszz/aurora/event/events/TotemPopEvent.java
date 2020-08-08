
package me.memeszz.aurora.event.events;

import net.minecraft.entity.Entity;
import me.memeszz.aurora.event.AuroraEvent;

public class TotemPopEvent extends AuroraEvent
{
    private final Entity entity;
    
    public TotemPopEvent(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
