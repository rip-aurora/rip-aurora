package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.memeszz.aurora.module.ModuleManager;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import me.memeszz.aurora.mixin.accessor.IEntity;

@Mixin({ Entity.class })
public abstract class MixinEntity implements IEntity
{
    @Accessor
    @Override
    public abstract boolean getIsInWeb();
    
    @Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void velocity(final Entity entity, final double x, final double y, final double z) {
        if (!ModuleManager.isModuleEnabled("NoSlow")) {
            entity.motionX += x;
            entity.motionY += y;
            entity.motionZ += z;
            entity.isAirBorne = true;
        }
    }
}
