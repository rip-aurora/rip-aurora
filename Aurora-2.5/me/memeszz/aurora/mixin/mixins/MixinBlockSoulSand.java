
package me.memeszz.aurora.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.memeszz.aurora.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockSoulSand;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockSoulSand.class })
public abstract class MixinBlockSoulSand
{
    @Inject(method = { "onEntityCollision" }, at = { @At("HEAD") }, cancellable = true)
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn, final CallbackInfo info) {
        if (ModuleManager.isModuleEnabled("NoSlow")) {
            info.cancel();
        }
    }
}
