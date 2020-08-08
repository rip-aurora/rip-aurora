
package me.memeszz.aurora.event.events;

import me.memeszz.aurora.event.AuroraEvent;

public class PlayerLeaveEvent extends AuroraEvent
{
    private final String name;
    
    public PlayerLeaveEvent(final String n) {
        this.name = n;
    }
    
    public String getName() {
        return this.name;
    }
}
