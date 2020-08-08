
package me.memeszz.aurora.module.modules.movement;

import net.minecraft.util.MovementInput;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.module.Module;

public class NoSlow extends Module
{
    @EventHandler
    private final Listener<InputUpdateEvent> eventListener;
    
    public NoSlow() {
        super("NoSlow", Category.MOVEMENT, "Prevents item use form slowing you down");
        final MovementInput movementInput;
        final MovementInput movementInput2;
        this.eventListener = new Listener<InputUpdateEvent>(event -> {
            if (NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
                event.getMovementInput();
                movementInput.moveStrafe *= 5.0f;
                event.getMovementInput();
                movementInput2.moveForward *= 5.0f;
            }
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);
    }
}
