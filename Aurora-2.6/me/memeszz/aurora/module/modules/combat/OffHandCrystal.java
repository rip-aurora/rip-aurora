package me.memeszz.aurora.module.modules.combat;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.Entity;
import me.memeszz.aurora.util.friends.Friends;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import me.memeszz.aurora.event.events.UpdateEvent;
import me.memeszz.aurora.util.Wrapper;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class OffHandCrystal extends Module
{
    Setting.b crystalCheck;
    Setting.b totemdisable;
    Setting.d health;
    Setting.b announceUsage2;
    Setting.d enemyRange;
    Setting.b smart;
    public int crystals;
    
    public OffHandCrystal() {
        super("OffHandCrystal", Category.COMBAT, "Attacks nearby players");
    }
    
    @Override
    public void setup() {
        this.announceUsage2 = this.registerB("Chat", true);
        this.totemdisable = this.registerB("TotemDisable", true);
        this.health = this.registerD("Health", 13.0, 1.0, 36.0);
        this.crystalCheck = this.registerB("CrystalCheck", false);
        this.enemyRange = this.registerD("EnemyRange", 10.0, 1.0, 50.0);
        this.smart = this.registerB("Smart", false);
    }
    
    public void onEnable() {
        if (this.totemdisable.getValue()) {
            ModuleManager.getModuleByName("AutoTotem").disable();
        }
        if (this.announceUsage2.getValue()) {
            Wrapper.sendClientMessage("§aOffHandCrystal Enabled");
        }
    }
    
    public void onDisable() {
        if (this.totemdisable.getValue()) {
            ModuleManager.getModuleByName("AutoTotem").enable();
        }
        if (this.announceUsage2.getValue()) {
            Wrapper.sendClientMessage("§cOffHandCrystal Disabled");
        }
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        this.crystals = OffHandCrystal.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (OffHandCrystal.mc.currentScreen instanceof GuiContainer || OffHandCrystal.mc.world == null || OffHandCrystal.mc.player == null) {
            return;
        }
        if (!this.shouldTotem()) {
            if (OffHandCrystal.mc.player.getHeldItemOffhand() == ItemStack.EMPTY || OffHandCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                final int slot = (this.getCrystalSlot() < 9) ? (this.getCrystalSlot() + 36) : this.getCrystalSlot();
                if (this.getCrystalSlot() != -1) {
                    OffHandCrystal.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)OffHandCrystal.mc.player);
                    OffHandCrystal.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffHandCrystal.mc.player);
                    OffHandCrystal.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)OffHandCrystal.mc.player);
                }
            }
        }
        else if (OffHandCrystal.mc.player.getHeldItemOffhand() == ItemStack.EMPTY || OffHandCrystal.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            final int slot = (this.getTotemSlot() < 9) ? (this.getTotemSlot() + 36) : this.getTotemSlot();
            if (this.getTotemSlot() != -1) {
                OffHandCrystal.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)OffHandCrystal.mc.player);
                OffHandCrystal.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffHandCrystal.mc.player);
                OffHandCrystal.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)OffHandCrystal.mc.player);
            }
        }
    }
    
    private boolean nearPlayers() {
        return OffHandCrystal.mc.world.playerEntities.stream().anyMatch(e -> e != OffHandCrystal.mc.player && ((EntityPlayer)e).getEntityId() != -1488 && !Friends.isFriend(((EntityPlayer)e).getName()) && OffHandCrystal.mc.player.getDistance((Entity)e) <= this.enemyRange.getValue());
    }
    
    private boolean shouldTotem() {
        if (OffHandCrystal.mc.player != null) {
            return OffHandCrystal.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || !this.nearPlayers() || OffHandCrystal.mc.player.getHealth() + OffHandCrystal.mc.player.getAbsorptionAmount() <= this.health.getValue() || (this.crystalCheck.getValue() && !this.isCrystalsAABBEmpty()) || (this.smart.getValue() && AutoCrystal.target == null);
        }
        return OffHandCrystal.mc.player.getHealth() + OffHandCrystal.mc.player.getAbsorptionAmount() <= this.health.getValue() || OffHandCrystal.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || !this.nearPlayers() || (this.crystalCheck.getValue() && !this.isCrystalsAABBEmpty()) || (this.smart.getValue() && AutoCrystal.target == null);
    }
    
    private boolean isEmpty(final BlockPos pos) {
        return OffHandCrystal.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos)).stream().filter(e -> e instanceof EntityEnderCrystal).count() == 0L;
    }
    
    private boolean isCrystalsAABBEmpty() {
        return this.isEmpty(OffHandCrystal.mc.player.getPosition().add(1, 0, 0)) && this.isEmpty(OffHandCrystal.mc.player.getPosition().add(-1, 0, 0)) && this.isEmpty(OffHandCrystal.mc.player.getPosition().add(0, 0, 1)) && this.isEmpty(OffHandCrystal.mc.player.getPosition().add(0, 0, -1)) && this.isEmpty(OffHandCrystal.mc.player.getPosition());
    }
    
    int getCrystalSlot() {
        int crystalSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (OffHandCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                crystalSlot = i;
                break;
            }
        }
        return crystalSlot;
    }
    
    int getTotemSlot() {
        int totemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (OffHandCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }
        return totemSlot;
    }
    
    @Override
    public String getHudInfo() {
        return "§7[§f" + this.crystals + "§7]";
    }
}
