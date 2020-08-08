package me.memeszz.aurora.event.events;

public class PlayerLeaveEvent
{
    private final String name;
    
    public PlayerLeaveEvent(final String n) {
        this.name = n;
    }
    
    public String getName() {
        return this.name;
    }
}
