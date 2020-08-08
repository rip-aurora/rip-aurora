
package me.memeszz.aurora.module.modules.combat;

import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import me.memeszz.aurora.util.block.RotationManager;
import net.minecraft.item.ItemSword;
import java.util.function.Consumer;
import java.util.Comparator;
import me.memeszz.aurora.util.friends.Friends;
import net.minecraft.entity.Entity;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class KillAura extends Module
{
    private Setting.d range;
    private boolean swordOnly;
    private boolean caCheck;
    private Setting.b criticals;
    private Setting.b rotate;
    private boolean tpsSync;
    private boolean isAttacking;
    public static Entity target;
    
    public KillAura() {
        super("KillAura", Category.COMBAT, "Attacks nearby players");
    }
    
    @Override
    public void setup() {
        this.swordOnly = true;
        this.caCheck = true;
        this.tpsSync = false;
        this.isAttacking = false;
        this.range = this.registerD("Range", 4.5, 0.0, 10.0);
        this.criticals = this.registerB("Criticals", true);
        this.rotate = this.registerB("Rotate", true);
    }
    
    @Override
    public void onUpdate() {
        if (KillAura.mc.player.isDead || KillAura.mc.player.getHealth() <= 0.0f) {
            return;
        }
        KillAura.mc.world.playerEntities.stream().filter(entity -> entity != KillAura.mc.player).filter(entity -> KillAura.mc.player.getDistance(entity) <= this.range.getValue()).filter(e -> e.getHealth() > 0.0f).filter(e -> !e.isDead).filter(e -> !Friends.isFriend(e.getName())).sorted(Comparator.comparing(e -> KillAura.mc.player.getDistance(e))).forEach(this::attack);
    }
    
    private void attack(final Entity e) {
        if (this.swordOnly && !(KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
            return;
        }
        final float i = 1.0f;
        KillAura.target = e;
        if (KillAura.mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
            this.isAttacking = true;
            final boolean rotatee = this.rotate.getValue();
            if (rotatee) {
                RotationManager.lookAt(e, (EntityPlayer)KillAura.mc.player);
            }
            KillAura.mc.playerController.attackEntity((EntityPlayer)KillAura.mc.player, e);
            KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
            if (rotatee) {
                RotationManager.resetRotation();
            }
            this.isAttacking = false;
        }
    }
}
