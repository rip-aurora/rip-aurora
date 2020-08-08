
package me.memeszz.aurora.module.modules.combat;

import net.minecraft.block.BlockEnderChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import me.memeszz.aurora.util.block.BlockInteractionHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class AutoEchest extends Module
{
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int offsetStep;
    private int delayStep;
    private boolean firstRun;
    private boolean isSneaking;
    private Setting.i tickDelay2;
    private Setting.i blocksPerTick2;
    private Setting.b rotate2;
    
    public AutoEchest() {
        super("AutoEChest", Category.COMBAT, "Attacks nearby players");
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.offsetStep = 0;
        this.delayStep = 0;
        this.isSneaking = false;
    }
    
    @Override
    public void setup() {
        this.tickDelay2 = this.registerI("Delay", 0, 0, 10);
        this.blocksPerTick2 = this.registerI("Bpt", 4, 0, 10);
        this.rotate2 = this.registerB("Rotate", true);
    }
    
    @Override
    protected void onDisable() {
        if (AutoEchest.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoEchest.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoEchest.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoEchest.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onUpdate() {
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay2.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        if (this.firstRun) {
            this.firstRun = false;
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick2.getValue()) {
            final Vec3d[] offsetPattern = Offsets.SURROUND;
            final int maxSteps = Offsets.SURROUND.length;
            if (this.offsetStep >= maxSteps) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetStep]);
            final BlockPos targetPos = new BlockPos(AutoEchest.mc.player.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
            if (this.placeBlock(targetPos)) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                AutoEchest.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                AutoEchest.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoEchest.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
    }
    
    private boolean placeBlock(final BlockPos pos) {
        final Block block = AutoEchest.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : AutoEchest.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = AutoEchest.mc.world.getBlockState(neighbour).getBlock();
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiSlot) {
            AutoEchest.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            AutoEchest.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoEchest.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate2.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        AutoEchest.mc.playerController.processRightClickBlock(AutoEchest.mc.player, AutoEchest.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoEchest.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoEchest.mc.rightClickDelayTimer = 4;
        AutoEchest.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoEchest.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockEnderChest) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    private static class Offsets
    {
        private static final Vec3d[] SURROUND;
        
        static {
            SURROUND = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0) };
        }
    }
}
