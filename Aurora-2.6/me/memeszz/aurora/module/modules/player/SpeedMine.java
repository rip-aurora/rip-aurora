package me.memeszz.aurora.module.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import me.memeszz.aurora.event.events.EventPlayerDamageBlock;
import me.memeszz.aurora.event.events.EventPlayerClickBlock;
import me.memeszz.aurora.event.events.EventPlayerResetBlockRemoving;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.client.Minecraft;
import me.memeszz.aurora.mixin.accessor.IPlayerControllerMP;
import me.memeszz.aurora.event.events.UpdateEvent;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class SpeedMine extends Module
{
    Setting.mode mode;
    Setting.b reset;
    Setting.b FastFall;
    Setting.b doubleBreak;
    
    public SpeedMine() {
        super("SpeedMine", Category.PLAYER, "Mine blocks faster");
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.mode.getValue() + "§7]";
    }
    
    @Override
    public void setup() {
        this.reset = this.registerB("Reset", false);
        this.FastFall = this.registerB("FastFall", false);
        this.doubleBreak = this.registerB("DoubleBreak", false);
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Packet");
        modes.add("Damage");
        modes.add("Instant");
        this.mode = this.registerMode("Mode", modes, "Packet");
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        ((IPlayerControllerMP)SpeedMine.mc.playerController).setBlockHitDelay(0);
        if (this.reset.getValue() && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) {
            ((IPlayerControllerMP)SpeedMine.mc.playerController).setIsHittingBlock(false);
        }
        if (SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && this.FastFall.getValue() && SpeedMine.mc.player.onGround) {
            final EntityPlayerSP player = SpeedMine.mc.player;
            --player.motionY;
        }
    }
    
    @Listener
    public void setReset(final EventPlayerResetBlockRemoving event) {
        if (SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && this.reset.getValue()) {
            event.setCanceled(true);
        }
    }
    
    @Listener
    public void clickBlock(final EventPlayerClickBlock event) {
        if (SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && this.reset.getValue() && ((IPlayerControllerMP)SpeedMine.mc.playerController).getCurBlockDamageMP() > 0.1f) {
            ((IPlayerControllerMP)SpeedMine.mc.playerController).setIsHittingBlock(true);
        }
    }
    
    @Listener
    public void damageBlock(final EventPlayerDamageBlock event) {
        if (this.canBreak(event.getPos())) {
            if (this.reset.getValue()) {
                ((IPlayerControllerMP)SpeedMine.mc.playerController).setIsHittingBlock(false);
            }
            if (this.mode.getValue().equalsIgnoreCase("Instant")) {
                SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getDirection()));
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getDirection()));
                SpeedMine.mc.playerController.onPlayerDestroyBlock(event.getPos());
                SpeedMine.mc.world.setBlockToAir(event.getPos());
            }
            if (SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                if (this.mode.getValue().equalsIgnoreCase("Packet")) {
                    SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getDirection()));
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getDirection()));
                    event.setCanceled(true);
                }
                if (this.mode.getValue().equalsIgnoreCase("Damage") && ((IPlayerControllerMP)SpeedMine.mc.playerController).getCurBlockDamageMP() >= 0.7f) {
                    ((IPlayerControllerMP)SpeedMine.mc.playerController).setCurBlockDamageMP(1.0f);
                }
            }
            if (this.doubleBreak.getValue()) {
                final BlockPos above = event.getPos().add(0, 1, 0);
                if (this.canBreak(above) && SpeedMine.mc.player.getDistance((double)above.getX(), (double)above.getY(), (double)above.getZ()) <= 5.0) {
                    SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.getDirection()));
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.getDirection()));
                    SpeedMine.mc.playerController.onPlayerDestroyBlock(above);
                    SpeedMine.mc.world.setBlockToAir(above);
                }
            }
        }
    }
    
    private boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = SpeedMine.mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, (World)Minecraft.getMinecraft().world, pos) != -1.0f;
    }
}
