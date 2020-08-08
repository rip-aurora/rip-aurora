package me.memeszz.aurora.module.modules.player;

import net.minecraft.block.state.IBlockState;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.gui.inventory.GuiCrafting;
import me.memeszz.aurora.event.events.UpdateEvent;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import me.memeszz.aurora.util.entity.EntityUtil;
import me.memeszz.aurora.util.block.BlockInteractionHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import me.memeszz.aurora.module.Module;

public class AutoBedCraft extends Module
{
    private BlockPos tablePostition;
    private int delay;
    private boolean hasCrafted;
    private boolean firstSlot;
    private boolean secondSlot;
    private boolean thirdSlot;
    private boolean fourthSlot;
    private boolean fifthSlot;
    private boolean sixthSlot;
    private boolean openCTable;
    Item woolForCrafting;
    
    public AutoBedCraft() {
        super("AutoBedCraft", Category.PLAYER, "Crafts Beds Automatically perfect for 1.13 and up servers");
        this.tablePostition = null;
        this.woolForCrafting = Item.getItemFromBlock(Blocks.WOOL);
    }
    
    public void onEnable() {
        this.tablePostition = null;
        this.hasCrafted = false;
        this.firstSlot = false;
        this.secondSlot = false;
        this.thirdSlot = false;
        this.fourthSlot = false;
        this.fifthSlot = false;
        this.sixthSlot = false;
        this.openCTable = false;
        this.delay = 0;
        final int tableSlot = this.getTableSlot();
        if (tableSlot != -1) {
            this.tablePostition = BlockInteractionHelper.getSphere(BlockInteractionHelper.GetLocalPlayerPosFloored(), 4.0f, 4, false, true, 0).stream().filter(p_Pos -> this.IsValidBlockPos(p_Pos)).min(Comparator.comparing(p_Pos -> EntityUtil.getDistanceOfEntityToBlock((Entity)AutoBedCraft.mc.player, p_Pos))).orElse(null);
        }
        if (this.tablePostition == null) {
            return;
        }
        AutoBedCraft.mc.player.inventory.currentItem = tableSlot;
        AutoBedCraft.mc.playerController.updateController();
        BlockInteractionHelper.placeBlockScaffold(this.tablePostition);
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        ++this.delay;
        if (AutoBedCraft.mc.player == null || AutoBedCraft.mc.world == null) {
            this.disable();
        }
        if (this.delay > 3 && !(AutoBedCraft.mc.currentScreen instanceof GuiCrafting) && !this.openCTable) {
            AutoBedCraft.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.tablePostition, EnumFacing.NORTH, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.openCTable = true;
        }
        if (AutoBedCraft.mc.currentScreen instanceof GuiCrafting && !this.hasCrafted) {
            for (int i = 9; i < 46; ++i) {
                final ItemStack stacks = AutoBedCraft.mc.player.openContainer.getSlot(i).getStack();
                if (stacks != ItemStack.EMPTY) {
                    final Item woolForCrafting = Item.getItemFromBlock(Blocks.WOOL);
                    final Item planksForCrafting = Item.getItemFromBlock(Blocks.PLANKS);
                    if (stacks.getItem() == woolForCrafting) {
                        if (this.delay > 0 && !this.firstSlot) {
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, i, 1, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, 1, 0, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            this.firstSlot = true;
                        }
                        if (this.delay > 4 && !this.secondSlot) {
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, i, 1, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, 2, 0, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            this.secondSlot = true;
                        }
                        if (this.delay > 8 && !this.thirdSlot) {
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, i, 0, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, 3, 0, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            this.thirdSlot = true;
                        }
                    }
                    if (stacks.getItem() == planksForCrafting) {
                        if (this.delay > 12 && !this.fourthSlot) {
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, i, 1, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, 4, 0, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            this.fourthSlot = true;
                        }
                        if (this.delay > 16 && !this.fifthSlot) {
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, i, 1, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, 5, 0, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            this.fifthSlot = true;
                        }
                        if (this.delay > 20 && !this.sixthSlot) {
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, i, 0, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            AutoBedCraft.mc.playerController.windowClick(((GuiContainer)AutoBedCraft.mc.currentScreen).inventorySlots.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)AutoBedCraft.mc.player);
                            this.sixthSlot = true;
                            this.hasCrafted = true;
                        }
                    }
                }
            }
        }
        if (this.firstSlot && this.secondSlot && this.thirdSlot && this.fourthSlot && this.fifthSlot && this.sixthSlot && this.hasCrafted && !(AutoBedCraft.mc.currentScreen instanceof GuiCrafting)) {
            this.disable();
        }
    }
    
    private boolean IsValidBlockPos(final BlockPos p_Pos) {
        final IBlockState state = AutoBedCraft.mc.world.getBlockState(p_Pos);
        if (state.getBlock() == Blocks.AIR && AutoBedCraft.mc.world.getBlockState(p_Pos.up()).getBlock() == Blocks.AIR) {
            final BlockInteractionHelper.ValidResult result = BlockInteractionHelper.valid(p_Pos);
            if (result == BlockInteractionHelper.ValidResult.Ok) {
                return true;
            }
        }
        return false;
    }
    
    private int getTableSlot() {
        for (int I = 0; I < 9; ++I) {
            final ItemStack stack = AutoBedCraft.mc.player.inventory.getStackInSlot(I);
            if (stack != ItemStack.EMPTY && Item.getIdFromItem(stack.getItem()) == 58) {
                return I;
            }
        }
        return -1;
    }
}
