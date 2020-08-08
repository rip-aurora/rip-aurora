package me.memeszz.aurora.module.modules.movement;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.memeszz.aurora.module.Module;

public class NoSlow extends Module
{
    public NoSlow() {
        super("NoSlow", Category.MOVEMENT, "Prevents item use form slowing you down");
    }
    
    @Listener
    public void onUpdate(final InputUpdateEvent event) {
        if (NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }
}
