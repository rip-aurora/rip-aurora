
package me.memeszz.aurora.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import java.util.function.Predicate;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PlayerMoveEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Speed extends Module
{
    Setting.b ground;
    @EventHandler
    public Listener<PlayerMoveEvent> moveListener;
    
    public Speed() {
        super("Strafe", Category.MOVEMENT, "Makes you go fast");
        float playerSpeed;
        float moveForward;
        float moveStrafe;
        float rotationYaw;
        int amplifier;
        EntityPlayerSP player;
        final double motionY;
        double sin;
        double cos;
        this.moveListener = new Listener<PlayerMoveEvent>(event -> {
            if (Speed.mc.player != null && !Speed.mc.player.isSneaking() && !Speed.mc.player.isOnLadder() && !Speed.mc.player.isInLava() && !Speed.mc.player.isInWater() && !Speed.mc.player.capabilities.isFlying && (this.ground.getValue() || !Speed.mc.player.onGround)) {
                playerSpeed = 0.2873f;
                moveForward = Speed.mc.player.movementInput.moveForward;
                moveStrafe = Speed.mc.player.movementInput.moveStrafe;
                rotationYaw = Speed.mc.player.rotationYaw;
                if (Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
                    amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
                    playerSpeed *= 1.0f + 0.2f * (amplifier + 1);
                }
                if (moveForward == 0.0f && moveStrafe == 0.0f) {
                    event.setX(0.0);
                    event.setZ(0.0);
                }
                else {
                    if (Speed.mc.player.onGround) {
                        player = Speed.mc.player;
                        event.setY(player.motionY = motionY);
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
                    sin = Math.sin(Math.toRadians(rotationYaw + 90.0f));
                    cos = Math.cos(Math.toRadians(rotationYaw + 90.0f));
                    event.setX(moveForward * playerSpeed * cos + moveStrafe * playerSpeed * sin);
                    event.setZ(moveForward * playerSpeed * sin - moveStrafe * playerSpeed * cos);
                }
            }
        }, (Predicate<PlayerMoveEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.ground = this.registerB("Ground", true);
    }
}
