package me.memeszz.aurora.event.events;

public class PlayerJoinEvent
{
    private final String name;
    
    public PlayerJoinEvent(final String n) {
        this.name = n;
    }
    
    public String getName() {
        return this.name;
    }
}
