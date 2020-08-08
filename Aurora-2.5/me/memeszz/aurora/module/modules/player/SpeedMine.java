
package me.memeszz.aurora.module.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemPickaxe;
import me.memeszz.aurora.event.events.EventPlayerDamageBlock;
import me.memeszz.aurora.event.events.EventPlayerClickBlock;
import me.memeszz.aurora.event.events.EventPlayerResetBlockRemoving;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.EventPlayerUpdate;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class SpeedMine extends Module
{
    Setting.mode mode;
    Setting.b reset;
    Setting.b FastFall;
    Setting.b doubleBreak;
    @EventHandler
    private final Listener<EventPlayerUpdate> OnPlayerUpdate;
    @EventHandler
    private final Listener<EventPlayerResetBlockRemoving> ResetBlock;
    @EventHandler
    private final Listener<EventPlayerClickBlock> ClickBlock;
    @EventHandler
    private final Listener<EventPlayerDamageBlock> OnDamageBlock;
    
    public SpeedMine() {
        super("SpeedMine", Category.PLAYER, "Mine blocks faster");
        EntityPlayerSP player;
        this.OnPlayerUpdate = new Listener<EventPlayerUpdate>(p_Event -> {
            if (SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                SpeedMine.mc.playerController.blockHitDelay = 0;
                if (this.reset.getValue() && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) {
                    SpeedMine.mc.playerController.isHittingBlock = false;
                }
                if (this.FastFall.getValue() && SpeedMine.mc.player.onGround) {
                    player = SpeedMine.mc.player;
                    --player.motionY;
                }
            }
            return;
        }, (Predicate<EventPlayerUpdate>[])new Predicate[0]);
        this.ResetBlock = new Listener<EventPlayerResetBlockRemoving>(p_Event -> {
            if (SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && this.reset.getValue()) {
                p_Event.cancel();
            }
            return;
        }, (Predicate<EventPlayerResetBlockRemoving>[])new Predicate[0]);
        this.ClickBlock = new Listener<EventPlayerClickBlock>(p_Event -> {
            if (SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && this.reset.getValue() && SpeedMine.mc.playerController.curBlockDamageMP > 0.1f) {
                SpeedMine.mc.playerController.isHittingBlock = true;
            }
            return;
        }, (Predicate<EventPlayerClickBlock>[])new Predicate[0]);
        BlockPos above;
        this.OnDamageBlock = new Listener<EventPlayerDamageBlock>(p_Event -> {
            if (SpeedMine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                if (this.canBreak(p_Event.getPos())) {
                    if (this.reset.getValue()) {
                        SpeedMine.mc.playerController.isHittingBlock = false;
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Packet")) {
                        SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, p_Event.getPos(), p_Event.getDirection()));
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, p_Event.getPos(), p_Event.getDirection()));
                        p_Event.cancel();
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Damage") && SpeedMine.mc.playerController.curBlockDamageMP >= 0.7f) {
                        SpeedMine.mc.playerController.curBlockDamageMP = 1.0f;
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Instant")) {
                        SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, p_Event.getPos(), p_Event.getDirection()));
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, p_Event.getPos(), p_Event.getDirection()));
                        SpeedMine.mc.playerController.onPlayerDestroyBlock(p_Event.getPos());
                        SpeedMine.mc.world.setBlockToAir(p_Event.getPos());
                    }
                }
                if (this.doubleBreak.getValue()) {
                    above = p_Event.getPos().add(0, 1, 0);
                    if (this.canBreak(above) && SpeedMine.mc.player.getDistance((double)above.getX(), (double)above.getY(), (double)above.getZ()) <= 5.0) {
                        SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, p_Event.getDirection()));
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, p_Event.getDirection()));
                        SpeedMine.mc.playerController.onPlayerDestroyBlock(above);
                        SpeedMine.mc.world.setBlockToAir(above);
                    }
                }
            }
        }, (Predicate<EventPlayerDamageBlock>[])new Predicate[0]);
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
    
    private boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = SpeedMine.mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, (World)Minecraft.getMinecraft().world, pos) != -1.0f;
    }
}
