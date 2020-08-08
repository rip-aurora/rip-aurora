package me.memeszz.aurora.module.modules.combat;

import me.memeszz.aurora.mixin.accessor.ICPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import me.memeszz.aurora.event.events.PacketEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import net.minecraft.util.NonNullList;
import me.memeszz.aurora.util.friends.Friends;
import me.memeszz.aurora.util.entity.EntityUtil;
import me.memeszz.aurora.util.render.RenderUtil;
import me.memeszz.aurora.event.events.RenderEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.EnumHand;
import me.memeszz.aurora.util.block.BlockInteractionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import me.memeszz.aurora.event.events.UpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class HoleFill extends Module
{
    private Setting.d range;
    private Setting.i smartRange;
    private Setting.b smart;
    private Setting.b toggleOff;
    private Setting.b webs;
    private BlockPos render;
    private Entity closestTarget;
    private int delay;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    
    public HoleFill() {
        super("HoleFiller", Category.COMBAT, "Attacks nearby players");
        this.delay = 0;
    }
    
    @Override
    public void setup() {
        this.range = this.registerD("Range", 3.0, 0.0, 6.0);
        this.smartRange = this.registerI("SmartRange", 3, 0, 6);
        this.smart = this.registerB("SmartFill", false);
        this.toggleOff = this.registerB("ToggleOff", true);
        this.webs = this.registerB("Webs", true);
    }
    
    public void onEnable() {
        this.delay = 0;
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        ++this.delay;
        if (HoleFill.mc.world == null) {
            return;
        }
        if (this.delay > 6 && this.toggleOff.getValue()) {
            this.disable();
            this.delay = 0;
        }
        if (this.smart.getValue()) {
            this.findClosestTarget();
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        BlockPos q = null;
        int obsidianSlot = (HoleFill.mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) ? HoleFill.mc.player.inventory.currentItem : -1;
        if (obsidianSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (this.webs.getValue()) {
                    if (HoleFill.mc.player.inventory.getStackInSlot(l).getItem() != Item.getItemFromBlock(Blocks.WEB)) {
                        if (HoleFill.mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                            obsidianSlot = l;
                            break;
                        }
                    }
                }
            }
        }
        if (obsidianSlot == -1) {
            return;
        }
        for (final BlockPos blockPos : blocks) {
            if (HoleFill.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(blockPos)).isEmpty()) {
                if (this.smart.getValue() && this.isInRange(blockPos)) {
                    q = blockPos;
                }
                else {
                    q = blockPos;
                }
            }
        }
        this.render = q;
        if (q != null && HoleFill.mc.player.onGround) {
            final int oldSlot = HoleFill.mc.player.inventory.currentItem;
            if (HoleFill.mc.player.inventory.currentItem != obsidianSlot) {
                HoleFill.mc.player.inventory.currentItem = obsidianSlot;
            }
            this.lookAtPacket(q.getX() + 0.5, q.getY() - 0.5, q.getZ() + 0.5, (EntityPlayer)HoleFill.mc.player);
            BlockInteractionHelper.placeBlockScaffold(this.render);
            HoleFill.mc.player.swingArm(EnumHand.MAIN_HAND);
            HoleFill.mc.player.inventory.currentItem = oldSlot;
            resetRotation();
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            RenderUtil.prepare(7);
            RenderUtil.drawBox(this.render, 253, 253, 11, 30, 63);
            RenderUtil.release();
            RenderUtil.prepare(7);
            RenderUtil.drawBoundingBoxBlockPos(this.render, 1.0f, 253, 253, 11, 255);
            RenderUtil.release();
        }
    }
    
    private double getDistanceToBlockPos(final BlockPos pos1, final BlockPos pos2) {
        final double x = pos1.getX() - pos2.getX();
        final double y = pos1.getY() - pos2.getY();
        final double z = pos1.getZ() - pos2.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean IsHole(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 0, 0);
        final BlockPos boost3 = blockPos.add(0, 0, -1);
        final BlockPos boost4 = blockPos.add(1, 0, 0);
        final BlockPos boost5 = blockPos.add(-1, 0, 0);
        final BlockPos boost6 = blockPos.add(0, 0, 1);
        final BlockPos boost7 = blockPos.add(0, 2, 0);
        final BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        final BlockPos boost9 = blockPos.add(0, -1, 0);
        return HoleFill.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && HoleFill.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && HoleFill.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && (HoleFill.mc.world.getBlockState(boost3).getBlock() == Blocks.OBSIDIAN || HoleFill.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK) && (HoleFill.mc.world.getBlockState(boost4).getBlock() == Blocks.OBSIDIAN || HoleFill.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK) && (HoleFill.mc.world.getBlockState(boost5).getBlock() == Blocks.OBSIDIAN || HoleFill.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK) && (HoleFill.mc.world.getBlockState(boost6).getBlock() == Blocks.OBSIDIAN || HoleFill.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK) && HoleFill.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && (HoleFill.mc.world.getBlockState(boost9).getBlock() == Blocks.OBSIDIAN || HoleFill.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK);
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(HoleFill.mc.player.posX), Math.floor(HoleFill.mc.player.posY), Math.floor(HoleFill.mc.player.posZ));
    }
    
    public BlockPos getClosestTargetPos() {
        if (this.closestTarget != null) {
            return new BlockPos(Math.floor(this.closestTarget.posX), Math.floor(this.closestTarget.posY), Math.floor(this.closestTarget.posZ));
        }
        return null;
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)HoleFill.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == HoleFill.mc.player) {
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
                this.closestTarget = (Entity)target;
            }
            else {
                if (HoleFill.mc.player.getDistance((Entity)target) >= HoleFill.mc.player.getDistance(this.closestTarget)) {
                    continue;
                }
                this.closestTarget = (Entity)target;
            }
        }
    }
    
    private boolean isInRange(final BlockPos blockPos) {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)this.range.getValue(), (int)this.range.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::IsHole).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return positions.contains((Object)blockPos);
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        if (this.smart.getValue() && this.closestTarget != null) {
            positions.addAll((Collection)this.getSphere(this.getClosestTargetPos(), (float)this.smartRange.getValue(), (int)this.range.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::IsHole).filter((Predicate<? super Object>)this::isInRange).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
        else if (!this.smart.getValue()) {
            positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)this.range.getValue(), (int)this.range.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::IsHole).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        HoleFill.yaw = yaw1;
        HoleFill.pitch = pitch1;
        HoleFill.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (HoleFill.isSpoofingAngles) {
            HoleFill.yaw = HoleFill.mc.player.rotationYaw;
            HoleFill.pitch = HoleFill.mc.player.rotationPitch;
            HoleFill.isSpoofingAngles = false;
        }
    }
    
    @Listener
    public void onUpdate(final PacketEvent.Send event) {
        final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
        if (packet instanceof CPacketPlayer && HoleFill.isSpoofingAngles) {
            ((ICPacketPlayer)packet).setYaw((float)HoleFill.yaw);
            ((ICPacketPlayer)packet).setPitch((float)HoleFill.pitch);
        }
    }
    
    public void onDisable() {
        this.delay = 0;
        this.closestTarget = null;
        this.render = null;
        resetRotation();
    }
}
