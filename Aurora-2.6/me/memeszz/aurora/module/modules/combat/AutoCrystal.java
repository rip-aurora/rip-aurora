package me.memeszz.aurora.module.modules.combat;

import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import java.util.Collection;
import java.util.function.Predicate;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketSpawnObject;
import me.memeszz.aurora.Aurora;
import net.minecraft.client.renderer.GlStateManager;
import me.memeszz.aurora.util.render.RenderUtil;
import java.awt.Color;
import me.memeszz.aurora.event.events.RenderEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.memeszz.aurora.util.friends.Friends;
import net.minecraft.init.Items;
import me.memeszz.aurora.util.math.MathUtil;
import net.minecraft.util.math.Vec3d;
import me.memeszz.aurora.event.EventStageable;
import java.util.Comparator;
import me.memeszz.aurora.event.events.UpdateEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.memeszz.aurora.event.events.PacketEvent;
import java.util.List;
import java.util.ArrayList;
import me.memeszz.aurora.util.math.StopwatchUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class AutoCrystal extends Module
{
    private Setting.b autoSwitch;
    private Setting.b spoofRotations;
    private Setting.d walls;
    private Setting.b place;
    private Setting.d range;
    private Setting.d facePlace;
    private Setting.d maxSelfDmg;
    public static Setting.d minDmg;
    private Setting.d placeRange;
    private Setting.i aps;
    private Setting.b renderDamage;
    private Setting.b rainbow;
    private Setting.b targetGlow;
    private Setting.b rotate;
    private Setting.b antiWeakness;
    private Setting.b explode;
    private Setting.b multiPlace;
    private Setting.d enemyRange;
    Setting.i espR;
    Setting.i espG;
    Setting.i espB;
    Setting.i espA;
    Setting.mode mode;
    public static Entity target;
    private BlockPos render;
    private boolean switchCooldown;
    private boolean isAttacking;
    private Entity crystal;
    private int newSlot;
    public static double d;
    public static String damageText;
    private int oldSlot;
    private final StopwatchUtil sp;
    
    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT, "Attacks nearby players");
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.sp = new StopwatchUtil();
    }
    
    @Override
    public void setup() {
        final ArrayList<String> placeModes = new ArrayList<String>();
        placeModes.add("Normal");
        placeModes.add("1.13");
        this.mode = this.registerMode("PlaceMode", placeModes, "Normal");
        this.place = this.registerB("Place", true);
        this.enemyRange = this.registerD("EnemyRange", 10.0, 1.0, 15.0);
        this.explode = this.registerB("Explode", true);
        this.rotate = this.registerB("Rotate", true);
        this.autoSwitch = this.registerB("AutoSwitch", true);
        this.spoofRotations = this.registerB("SpoofRotations", true);
        this.range = this.registerD("HitRange", 5.0, 0.0, 10.0);
        this.multiPlace = this.registerB("Multiplace", false);
        this.placeRange = this.registerD("PlaceRange", 5.0, 0.0, 10.0);
        this.walls = this.registerD("WallsRange", 3.5, 0.0, 5.0);
        this.aps = this.registerI("APS", 20, 1, 20);
        this.antiWeakness = this.registerB("AntiWeakness", true);
        AutoCrystal.minDmg = this.registerD("MinDMG", 4.0, 0.0, 50.0);
        this.maxSelfDmg = this.registerD("MaxSelfDmg", 6.0, 0.0, 36.0);
        this.facePlace = this.registerD("FacePlaceHP", 5.0, 0.0, 36.0);
        this.espR = this.registerI("Red", 255, 0, 255);
        this.espG = this.registerI("Green", 255, 0, 255);
        this.espB = this.registerI("Blue", 255, 0, 255);
        this.espA = this.registerI("Alpha", 26, 0, 255);
        this.rainbow = this.registerB("Rainbow", false);
        this.renderDamage = this.registerB("RenderDamage", true);
    }
    
    @Listener
    public void onPacketRecieve(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0) {
                        e.setDead();
                    }
                }
            }
        }
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        if (AutoCrystal.mc.world == null || AutoCrystal.mc.player == null) {
            return;
        }
        this.crystal = (Entity)AutoCrystal.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> AutoCrystal.mc.player.getDistance(c))).orElse(null);
        if (this.crystal != null && AutoCrystal.mc.player.getDistance(this.crystal) <= this.range.getValue()) {
            if (event.getStage() == EventStageable.EventStage.PRE) {
                final float[] rots = MathUtil.calcAngle(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(this.render.getX() + 0.5, this.render.getY() + 1.0, this.render.getZ() + 0.5));
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            }
            else if (StopwatchUtil.hasCompleted(1000 / this.aps.getValue()) && this.nearPlayers()) {
                this.attackCrystal();
                this.sp.reset();
                if (!this.multiPlace.getValue() && !StopwatchUtil.hasCompleted(50L)) {
                    return;
                }
            }
        }
        int crystalSlot = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystal.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (AutoCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        BlockPos q = null;
        double damage = 0.5;
        for (final EntityPlayer entity2 : (List)AutoCrystal.mc.world.playerEntities.stream().filter(p -> !p.equals((Object)AutoCrystal.mc.player)).filter(p -> AutoCrystal.mc.player.getDistance(p) <= this.enemyRange.getValue()).filter(p -> p.getHealth() > 0.0f).filter(p -> !p.isDead).filter(p -> !Friends.isFriend(p.getName())).sorted(Comparator.comparing(p -> this.enemyRange.getValue() > AutoCrystal.mc.player.getDistance(p))).collect(Collectors.toList())) {
            for (final BlockPos blockPos : this.findCrystalBlocks()) {
                AutoCrystal.d = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, (Entity)entity2);
                final double self = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, (Entity)AutoCrystal.mc.player);
                if (AutoCrystal.d > damage) {
                    if (AutoCrystal.d < AutoCrystal.minDmg.getValue() && entity2.getHealth() + entity2.getAbsorptionAmount() > this.facePlace.getValue()) {
                        continue;
                    }
                    if (self > this.maxSelfDmg.getValue()) {
                        continue;
                    }
                    damage = AutoCrystal.d;
                    q = blockPos;
                    AutoCrystal.target = (Entity)entity2;
                }
            }
        }
        if (damage == 0.5) {
            this.render = null;
            return;
        }
        if (this.place.getValue()) {
            if (!offhand && AutoCrystal.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    AutoCrystal.mc.player.inventory.currentItem = crystalSlot;
                    this.switchCooldown = true;
                }
                return;
            }
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            if (this.place.getValue()) {
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                this.render = q;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null && AutoCrystal.target != null) {
            final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f };
            final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
            final int r = rgb >> 16 & 0xFF;
            final int g = rgb >> 8 & 0xFF;
            final int b = rgb & 0xFF;
            RenderUtil.prepare(7);
            if (this.rainbow.getValue()) {
                RenderUtil.drawBox(this.render, r, g, b, this.espA.getValue(), 63);
                RenderUtil.release();
                RenderUtil.prepare(7);
                RenderUtil.drawBoundingBoxBlockPos(this.render, 1.0f, r, g, b, 255);
            }
            else {
                RenderUtil.drawBox(this.render, this.espR.getValue(), this.espG.getValue(), this.espB.getValue(), this.espA.getValue(), 63);
                RenderUtil.release();
                RenderUtil.prepare(7);
                RenderUtil.drawBoundingBoxBlockPos(this.render, 1.0f, this.espR.getValue(), this.espG.getValue(), this.espB.getValue(), 255);
            }
            RenderUtil.release();
        }
        if (this.renderDamage.getValue() && this.render != null && AutoCrystal.target != null) {
            GlStateManager.pushMatrix();
            RenderUtil.glBillboardDistanceScaled(this.render.getX() + 0.5f, this.render.getY() + 0.5f, this.render.getZ() + 0.5f, (EntityPlayer)AutoCrystal.mc.player, 1.0f);
            final double d = this.calculateDamage(this.render.getX() + 0.5, this.render.getY() + 1, this.render.getZ() + 0.5, AutoCrystal.target);
            AutoCrystal.damageText = ((Math.floor(d) == d) ? Integer.valueOf((int)d) : String.format("%.1f", d)) + "";
            GlStateManager.disableDepth();
            GlStateManager.translate(-(AutoCrystal.mc.fontRenderer.getStringWidth(AutoCrystal.damageText) / 2.0), 0.0, 0.0);
            Aurora.caviarFont.drawString(AutoCrystal.damageText, 0.0f, 0.0f, -1);
            GlStateManager.popMatrix();
        }
    }
    
    @Listener
    public void packet(final PacketEvent event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packetSpawnObject = (SPacketSpawnObject)event.getPacket();
            if (event.getStage() == EventStageable.EventStage.PRE && packetSpawnObject.getType() == 51 && this.spoofRotations.getValue() && !AutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                event.setCanceled(true);
                final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), new Vec3d(packetSpawnObject.getX() + 0.5, packetSpawnObject.getY() + 0.5, packetSpawnObject.getZ() + 0.5));
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], angle[1], AutoCrystal.mc.player.onGround));
            }
        }
    }
    
    private boolean nearPlayers() {
        return AutoCrystal.mc.world.playerEntities.stream().anyMatch(e -> e != AutoCrystal.mc.player && ((EntityPlayer)e).getEntityId() != -1488 && !Friends.isFriend(((EntityPlayer)e).getName()) && AutoCrystal.mc.player.getDistance((Entity)e) <= this.enemyRange.getValue());
    }
    
    private void attackCrystal() {
        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(this.crystal));
        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
    }
    
    private void antiWeakness() {
        if (!this.isAttacking) {
            this.oldSlot = AutoCrystal.mc.player.inventory.currentItem;
            this.isAttacking = true;
        }
        this.newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoCrystal.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemSword) {
                    this.newSlot = i;
                    break;
                }
                if (stack.getItem() instanceof ItemTool) {
                    this.newSlot = i;
                    break;
                }
            }
        }
        if (this.newSlot != -1) {
            AutoCrystal.mc.player.inventory.currentItem = this.newSlot;
            this.switchCooldown = true;
        }
        if (this.oldSlot != -1) {
            AutoCrystal.mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoCrystal.mc.player.posX), Math.floor(AutoCrystal.mc.player.posY), Math.floor(AutoCrystal.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)this.placeRange.getValue(), (int)this.placeRange.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return this.mode.getValue().equalsIgnoreCase("Normal") ? ((AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AutoCrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty()) : ((AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty());
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
    
    private float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public void onEnable() {
        AutoCrystal.target = null;
    }
    
    public void onDisable() {
        this.render = null;
        AutoCrystal.target = null;
    }
    
    @Override
    public String getHudInfo() {
        if (AutoCrystal.target != null) {
            return "§7[§a" + AutoCrystal.target.getName() + "§7]";
        }
        return "§7[§cNo target!§7]";
    }
}
