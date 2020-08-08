package me.memeszz.aurora.module.modules.movement;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import me.memeszz.aurora.event.events.PlayerMoveEvent;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Speed extends Module
{
    Setting.b ground;
    
    public Speed() {
        super("Strafe", Category.MOVEMENT, "Makes you go fast");
    }
    
    @Override
    public void setup() {
        this.ground = this.registerB("Ground", true);
    }
    
    @Listener
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (Speed.mc.player != null && !Speed.mc.player.isSneaking() && !Speed.mc.player.isOnLadder() && !Speed.mc.player.isInLava() && !Speed.mc.player.isInWater() && !Speed.mc.player.capabilities.isFlying && (this.ground.getValue() || !Speed.mc.player.onGround)) {
            float playerSpeed = 0.3157f;
            float moveForward = Speed.mc.player.movementInput.moveForward;
            float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
            float rotationYaw = Speed.mc.player.rotationYaw;
            if (Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
                final int amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
                playerSpeed *= 1.0f + 0.2f * (amplifier + 1);
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            else {
                if (Speed.mc.player.onGround) {
                    event.setY(Speed.mc.player.motionY = 0.4);
                }
                if (moveForward != 0.0f) {
                    if (moveStrafe > 0.0f) {
                        rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
                    }
                    else if (moveStrafe < 0.0f) {
                        rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
                    }
                    moveStrafe = 0.0f;
                    if (moveForward > 0.0f) {
                        moveForward = 1.0f;
                    }
                    else if (moveForward < 0.0f) {
                        moveForward = -1.0f;
                    }
                }
                final double sin = Math.sin(Math.toRadians(rotationYaw + 90.0f));
                final double cos = Math.cos(Math.toRadians(rotationYaw + 90.0f));
                event.setX(moveForward * playerSpeed * cos + moveStrafe * playerSpeed * sin);
                event.setZ(moveForward * playerSpeed * sin - moveStrafe * playerSpeed * cos);
            }
        }
    }
}
