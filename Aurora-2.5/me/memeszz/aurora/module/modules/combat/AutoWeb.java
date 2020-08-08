
package me.memeszz.aurora.module.modules.combat;

import java.util.Iterator;
import me.memeszz.aurora.util.entity.EntityUtil;
import me.memeszz.aurora.util.friends.Friends;
import net.minecraft.block.BlockWeb;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import me.memeszz.aurora.util.block.BlockInteractionHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class AutoWeb extends Module
{
    private Setting.d range;
    private Setting.b rotate;
    private Setting.i tickDelay;
    private Setting.i blocksPerTick;
    Setting.mode mode;
    private EntityPlayer closestTarget;
    private String lastTickTargetName;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int delayStep;
    private boolean isSneaking;
    private int offsetStep;
    private boolean firstRun;
    
    public AutoWeb() {
        super("AutoWeb", Category.COMBAT, "Attacks nearby players");
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.delayStep = 0;
        this.isSneaking = false;
        this.offsetStep = 0;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Full");
        modes.add("Feet");
        modes.add("Body");
        this.mode = this.registerMode("Mode", modes, "Feet");
        this.range = this.registerD("Range", 4.5, 0.0, 6.0);
        this.blocksPerTick = this.registerI("BPT", 4, 0, 10);
        this.tickDelay = this.registerI("TimeoutTicks", 1, 0, 10);
        this.rotate = this.registerB("Rotate", true);
    }
    
    @Override
    protected void onEnable() {
        if (AutoWeb.mc.player == null) {
            this.disable();
            return;
        }
        this.firstRun = true;
        this.playerHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    protected void onDisable() {
        if (AutoWeb.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoWeb.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoWeb.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onUpdate() {
        if (AutoWeb.mc.player == null) {
            return;
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        this.findClosestTarget();
        if (this.closestTarget == null) {
            if (this.firstRun) {
                this.firstRun = false;
            }
            return;
        }
        if (this.firstRun) {
            this.firstRun = false;
            this.lastTickTargetName = this.closestTarget.getName();
        }
        else if (!this.lastTickTargetName.equals(this.closestTarget.getName())) {
            this.lastTickTargetName = this.closestTarget.getName();
            this.offsetStep = 0;
        }
        final List<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (this.mode.getValue().equalsIgnoreCase("Full")) {
            Collections.addAll(placeTargets, Offsets.FULL);
        }
        if (this.mode.getValue().equalsIgnoreCase("Feet")) {
            Collections.addAll(placeTargets, Offsets.FEET);
        }
        if (this.mode.getValue().equalsIgnoreCase("Body")) {
            Collections.addAll(placeTargets, Offsets.BODY);
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= placeTargets.size()) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos((Vec3d)placeTargets.get(this.offsetStep));
            final BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).down().add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
            if (this.placeBlockInRange(targetPos, this.range.getValue())) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                AutoWeb.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoWeb.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
    }
    
    private boolean placeBlockInRange(final BlockPos pos, final double range) {
        final Block block = AutoWeb.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        final EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!Surround.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = AutoWeb.mc.world.getBlockState(neighbour).getBlock();
        if (AutoWeb.mc.player.getPositionVector().distanceTo(hitVec) > range) {
            return false;
        }
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiSlot) {
            AutoWeb.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            AutoWeb.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoWeb.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            Surround.faceVectorPacketInstant(hitVec);
        }
        AutoWeb.mc.playerController.processRightClickBlock(AutoWeb.mc.player, AutoWeb.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoWeb.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoWeb.mc.rightClickDelayTimer = 4;
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoWeb.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockWeb) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)AutoWeb.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == AutoWeb.mc.player) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (!EntityUtil.isLiving((Entity)target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (AutoWeb.mc.player.getDistance((Entity)target) >= AutoWeb.mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.mode.getValue() + "§7]";
    }
    
    private static class Offsets
    {
        private static final Vec3d[] FULL;
        private static final Vec3d[] FEET;
        private static final Vec3d[] BODY;
        
        static {
            FULL = new Vec3d[] { new Vec3d(0.0, 2.0, 0.0), new Vec3d(0.0, 1.0, 0.0) };
            FEET = new Vec3d[] { new Vec3d(0.0, 1.0, 0.0) };
            BODY = new Vec3d[] { new Vec3d(0.0, 2.0, 0.0) };
        }
    }
}
