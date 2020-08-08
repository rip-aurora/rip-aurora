
package me.memeszz.aurora.module.modules.combat;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.Entity;
import me.memeszz.aurora.util.friends.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class OffHandGap extends Module
{
    Setting.b crystalCheck;
    Setting.b totemdisable;
    Setting.d health;
    Setting.d enemyRange;
    
    public OffHandGap() {
        super("OffHandGap", Category.COMBAT, "Attacks nearby players");
    }
    
    @Override
    public void setup() {
        this.totemdisable = this.registerB("TotemDisable", true);
        this.health = this.registerD("Health", 13.0, 1.0, 36.0);
        this.crystalCheck = this.registerB("CrystalCheck", false);
        this.enemyRange = this.registerD("EnemyRange", 10.0, 1.0, 50.0);
    }
    
    public void onEnable() {
        if (this.totemdisable.getValue()) {
            ModuleManager.getModuleByName("AutoTotem").disable();
        }
    }
    
    public void onDisable() {
        if (this.totemdisable.getValue()) {
            ModuleManager.getModuleByName("AutoTotem").enable();
        }
    }
    
    @Override
    public void onUpdate() {
        if (OffHandGap.mc.currentScreen instanceof GuiContainer || OffHandGap.mc.world == null || OffHandGap.mc.player == null) {
            return;
        }
        if (!this.shouldTotem()) {
            if (OffHandGap.mc.player.getHeldItemOffhand() == ItemStack.EMPTY || OffHandGap.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
                final int slot = (this.getGapSlot() < 9) ? (this.getGapSlot() + 36) : this.getGapSlot();
                if (this.getGapSlot() != -1) {
                    OffHandGap.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)OffHandGap.mc.player);
                    OffHandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffHandGap.mc.player);
                    OffHandGap.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)OffHandGap.mc.player);
                }
            }
        }
        else if (OffHandGap.mc.player.getHeldItemOffhand() == ItemStack.EMPTY || OffHandGap.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            final int slot = (this.getTotemSlot() < 9) ? (this.getTotemSlot() + 36) : this.getTotemSlot();
            if (this.getTotemSlot() != -1) {
                OffHandGap.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)OffHandGap.mc.player);
                OffHandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffHandGap.mc.player);
                OffHandGap.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)OffHandGap.mc.player);
            }
        }
    }
    
    private boolean nearPlayers() {
        return OffHandGap.mc.world.playerEntities.stream().anyMatch(e -> e != OffHandGap.mc.player && ((EntityPlayer)e).getEntityId() != -1488 && !Friends.isFriend(((EntityPlayer)e).getName()) && OffHandGap.mc.player.getDistance((Entity)e) <= this.enemyRange.getValue());
    }
    
    private boolean shouldTotem() {
        if (OffHandGap.mc.player != null) {
            return OffHandGap.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || !this.nearPlayers() || OffHandGap.mc.player.getHealth() + OffHandGap.mc.player.getAbsorptionAmount() <= this.health.getValue() || (this.crystalCheck.getValue() && !this.isGapplesAABBEmpty());
        }
        return OffHandGap.mc.player.getHealth() + OffHandGap.mc.player.getAbsorptionAmount() <= this.health.getValue() || OffHandGap.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || !this.nearPlayers() || (this.crystalCheck.getValue() && !this.isGapplesAABBEmpty());
    }
    
    private boolean isEmpty(final BlockPos pos) {
        return OffHandGap.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos)).stream().filter(e -> e instanceof EntityEnderCrystal).count() == 0L;
    }
    
    private boolean isGapplesAABBEmpty() {
        return this.isEmpty(OffHandGap.mc.player.getPosition().add(1, 0, 0)) && this.isEmpty(OffHandGap.mc.player.getPosition().add(-1, 0, 0)) && this.isEmpty(OffHandGap.mc.player.getPosition().add(0, 0, 1)) && this.isEmpty(OffHandGap.mc.player.getPosition().add(0, 0, -1)) && this.isEmpty(OffHandGap.mc.player.getPosition());
    }
    
    int getGapSlot() {
        int gapSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (OffHandGap.mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                gapSlot = i;
                break;
            }
        }
        return gapSlot;
    }
    
    int getTotemSlot() {
        int totemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (OffHandGap.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }
        return totemSlot;
    }
}
