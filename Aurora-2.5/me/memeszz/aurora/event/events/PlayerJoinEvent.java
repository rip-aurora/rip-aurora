
package me.memeszz.aurora.event.events;

import me.memeszz.aurora.event.AuroraEvent;

public class PlayerJoinEvent extends AuroraEvent
{
    private final String name;
    
    public PlayerJoinEvent(final String n) {
        this.name = n;
    }
    
    public String getName() {
        return this.name;
    }
}
