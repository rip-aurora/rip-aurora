package me.memeszz.aurora.util.block;

import me.memeszz.aurora.util.Wrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;

public class RotationManager
{
    private static boolean shouldRotate;
    private static float yaw;
    private static float pitch;
    
    public static void lookAt(final Entity entity, final EntityPlayer me) {
        lookAt(entity.posX, entity.posY, entity.posZ, me);
    }
    
    public static void lookAt(final double x, final double y, final double z, final EntityPlayer me) {
        final double[] v = calculateLookAt(x, y, z, me);
        RotationManager.shouldRotate = true;
        RotationManager.yaw = (float)v[0];
        RotationManager.pitch = (float)v[1];
    }
    
    public static void resetRotation() {
        RotationManager.yaw = Wrapper.mc.player.rotationYaw;
        RotationManager.pitch = Wrapper.mc.player.rotationPitch;
        RotationManager.shouldRotate = false;
    }
    
    private static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }
    
    static {
        RotationManager.shouldRotate = false;
        RotationManager.yaw = -1.0f;
        RotationManager.pitch = -1.0f;
    }
}
