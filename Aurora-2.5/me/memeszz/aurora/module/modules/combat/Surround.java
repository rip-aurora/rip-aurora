
package me.memeszz.aurora.module.modules.combat;

import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockObsidian;
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
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.EventChorusTeleport;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import net.minecraft.util.math.Vec3d;
import me.memeszz.aurora.module.Module;

public class Surround extends Module
{
    private int totalTicksRunning;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int offsetStep;
    private int delayStep;
    private boolean firstRun;
    private Vec3d playerPos;
    private boolean isSneaking;
    private Setting.i tickDelay;
    private Setting.i blocksPerTick;
    private Setting.b rotate;
    private Setting.mode onChorus;
    private Setting.b autoCenter;
    private Setting.b triggerable;
    private Setting.i timeoutTicks;
    private Setting.b offInAir;
    @EventHandler
    private final Listener<EventChorusTeleport> tp;
    
    public Surround() {
        super("Surround", Category.COMBAT, "Attacks nearby players");
        this.totalTicksRunning = 0;
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.offsetStep = 0;
        this.delayStep = 0;
        this.isSneaking = false;
        this.tp = new Listener<EventChorusTeleport>(event -> {
            if (!this.onChorus.getValue().equals("None")) {
                if (this.onChorus.getValue().equals("Enable")) {
                    this.enable();
                }
                else {
                    this.disable();
                }
            }
        }, (Predicate<EventChorusTeleport>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.tickDelay = this.registerI("Delay", 0, 0, 10);
        this.timeoutTicks = this.registerI("TimeoutTicks", 0, 0, 10);
        this.blocksPerTick = this.registerI("Bpt", 10, 0, 10);
        this.rotate = this.registerB("Rotate", true);
        this.triggerable = this.registerB("Triggerable", false);
        this.autoCenter = this.registerB("CenterTp", true);
        this.offInAir = this.registerB("ToggleInAir", true);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Disable");
        modes.add("EnableAndDisable");
        modes.add("None");
        this.onChorus = this.registerMode("OnChorus", modes, "None");
    }
    
    private void centerPlayer(final double x, final double y, final double z) {
        Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
        Surround.mc.player.setPosition(x, y, z);
    }
    
    double getDst(final Vec3d vec) {
        return this.playerPos.distanceTo(vec);
    }
    
    @Override
    protected void onEnable() {
        if (Surround.mc.player == null) {
            this.disable();
            return;
        }
        final BlockPos centerPos = Surround.mc.player.getPosition();
        this.playerPos = Surround.mc.player.getPositionVector();
        final double y = centerPos.getY();
        double x = centerPos.getX();
        double z = centerPos.getZ();
        final Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
        final Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
        final Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
        final Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);
        if (this.autoCenter.getValue()) {
            if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                x = centerPos.getX() + 0.5;
                z = centerPos.getZ() + 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                x = centerPos.getX() + 0.5;
                z = centerPos.getZ() - 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                x = centerPos.getX() - 0.5;
                z = centerPos.getZ() - 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                x = centerPos.getX() - 0.5;
                z = centerPos.getZ() + 0.5;
                this.centerPlayer(x, y, z);
            }
        }
        this.firstRun = true;
        this.playerHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    protected void onDisable() {
        if (Surround.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            Surround.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onUpdate() {
        if (this.triggerable.getValue() && this.totalTicksRunning >= this.timeoutTicks.getValue()) {
            this.totalTicksRunning = 0;
            this.disable();
            return;
        }
        if (this.offInAir.getValue() && !Surround.mc.player.onGround) {
            this.disable();
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        if (this.firstRun) {
            this.firstRun = false;
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            Vec3d[] offsetPattern = new Vec3d[0];
            int maxSteps = 0;
            offsetPattern = Offsets.SURROUND;
            maxSteps = Offsets.SURROUND.length;
            if (this.offsetStep >= maxSteps) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetStep]);
            final BlockPos targetPos = new BlockPos(Surround.mc.player.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
            if (this.placeBlock(targetPos)) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                Surround.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
        ++this.totalTicksRunning;
    }
    
    private boolean placeBlock(final BlockPos pos) {
        final Block block = Surround.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : Surround.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
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
        final Block neighbourBlock = Surround.mc.world.getBlockState(neighbour).getBlock();
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiSlot) {
            Surround.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        Surround.mc.playerController.processRightClickBlock(Surround.mc.player, Surround.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        Surround.mc.player.swingArm(EnumHand.MAIN_HAND);
        Surround.mc.rightClickDelayTimer = 4;
        Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Surround.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockObsidian) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    public static boolean placeBlockScaffold(final BlockPos pos, final boolean rotate) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (canBeClicked(neighbor)) {
                new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
            }
        }
        return false;
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return Surround.mc.world.getBlockState(pos);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec) {
        final float[] rotations = getNeededRotations2(vec);
        Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], Surround.mc.player.onGround));
    }
    
    private static float[] getNeededRotations2(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { Surround.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - Surround.mc.player.rotationYaw), Surround.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - Surround.mc.player.rotationPitch) };
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(Surround.mc.player.posX, Surround.mc.player.posY + Surround.mc.player.getEyeHeight(), Surround.mc.player.posZ);
    }
    
    private static class Offsets
    {
        private static final Vec3d[] SURROUND;
        private static final Vec3d[] FULL;
        
        static {
            SURROUND = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0) };
            FULL = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 0.0) };
        }
    }
}
