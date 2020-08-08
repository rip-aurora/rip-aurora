
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemAppleGold;
import me.memeszz.aurora.util.font.FontUtils;
import me.memeszz.aurora.module.modules.gui.ClickGuiModule;
import net.minecraft.client.renderer.GlStateManager;
import me.memeszz.aurora.util.render.RenderUtil;
import java.awt.Color;
import me.memeszz.aurora.event.events.RenderEvent;
import java.util.List;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.EntityLivingBase;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.memeszz.aurora.util.friends.Friends;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.init.Items;
import java.util.Comparator;
import java.util.Iterator;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import me.memeszz.aurora.util.math.MathUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.init.MobEffects;
import me.memeszz.aurora.event.AuroraEvent;
import net.minecraft.network.play.server.SPacketSpawnObject;
import java.util.function.Predicate;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.zero.alpine.listener.EventHandler;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.util.math.StopwatchUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class CrystalAura extends Module
{
    private Setting.b autoSwitch;
    private Setting.b spoofRotations;
    private Setting.d walls;
    private Setting.b place;
    private Setting.d range;
    private Setting.d facePlace;
    private Setting.d maxSelfDmg;
    private Setting.i aps;
    private Setting.d minDmg;
    private Setting.d placeRange;
    private Setting.b renderDamage;
    private Setting.b rainbow;
    private Setting.b antiWeakness;
    private Setting.b explode;
    private Setting.b endcrystal;
    Setting.i espR;
    Setting.i espG;
    Setting.i espB;
    Setting.i espA;
    private BlockPos render;
    private Entity renderEnt;
    private boolean switchCooldown;
    private int oldSlot;
    private final StopwatchUtil sp;
    @EventHandler
    private final Listener<PacketEvent.Receive> packetReceiveListener;
    @EventHandler
    private final Listener<PacketEvent> listener;
    
    public CrystalAura() {
        super("CrystalAura", Category.COMBAT, "Attacks nearby players");
        this.switchCooldown = false;
        this.oldSlot = -1;
        this.sp = new StopwatchUtil();
        SPacketSoundEffect packet;
        final Iterator<Entity> iterator;
        Entity e;
        this.packetReceiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketSoundEffect) {
                packet = (SPacketSoundEffect)event.getPacket();
                if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    Minecraft.getMinecraft().world.loadedEntityList.iterator();
                    while (iterator.hasNext()) {
                        e = iterator.next();
                        if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0) {
                            e.setDead();
                        }
                    }
                }
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        SPacketSpawnObject packetSpawnObject;
        CPacketUseEntity packetUseEntity;
        float[] angle;
        this.listener = new Listener<PacketEvent>(event -> {
            if (event.getPacket() instanceof SPacketSpawnObject) {
                packetSpawnObject = (SPacketSpawnObject)event.getPacket();
                if (event.getEra() == AuroraEvent.Era.PRE && packetSpawnObject.getType() == 51 && this.spoofRotations.getValue() && !CrystalAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    event.cancel();
                    packetUseEntity = new CPacketUseEntity();
                    angle = MathUtil.calcAngle(CrystalAura.mc.player.getPositionEyes(CrystalAura.mc.getRenderPartialTicks()), new Vec3d(packetSpawnObject.getX() + 0.5, packetSpawnObject.getY() + 0.5, packetSpawnObject.getZ() + 0.5));
                    CrystalAura.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    CrystalAura.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], angle[1], CrystalAura.mc.player.onGround));
                    CrystalAura.mc.player.connection.sendPacket((Packet)packetUseEntity);
                }
            }
        }, (Predicate<PacketEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.place = this.registerB("Place", true);
        this.explode = this.registerB("Explode", true);
        this.autoSwitch = this.registerB("AutoSwitch", true);
        this.spoofRotations = this.registerB("SpoofRotations", true);
        this.endcrystal = this.registerB("1.13", true);
        this.range = this.registerD("HitRange", 5.0, 0.0, 10.0);
        this.placeRange = this.registerD("PlaceRange", 5.0, 0.0, 10.0);
        this.walls = this.registerD("WallsRange", 3.5, 0.0, 5.0);
        this.aps = this.registerI("Aps", 20, 1, 20);
        this.antiWeakness = this.registerB("AntiWeakness", true);
        this.minDmg = this.registerD("MinDMG", 4.0, 0.0, 36.0);
        this.maxSelfDmg = this.registerD("MaxSelfDmg", 6.0, 0.0, 36.0);
        this.facePlace = this.registerD("FacePlaceHP", 5.0, 0.0, 36.0);
        this.espR = this.registerI("Red", 255, 0, 255);
        this.espG = this.registerI("Green", 255, 0, 255);
        this.espB = this.registerI("Blue", 255, 0, 255);
        this.espA = this.registerI("Alpha", 40, 0, 255);
        this.rainbow = this.registerB("Rainbow", false);
        this.renderDamage = this.registerB("RenderDamage", true);
    }
    
    @Override
    public void onUpdate() {
        if (CrystalAura.mc.player != null || CrystalAura.mc.world != null) {
            final EntityEnderCrystal crystal = (EntityEnderCrystal)CrystalAura.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(e -> CrystalAura.mc.player.getDistance(e) <= this.range.getValue()).map(entity -> entity).min(Comparator.comparing(c -> CrystalAura.mc.player.getDistance(c))).orElse(null);
            if (!this.explode.getValue() || crystal == null) {
                if (this.oldSlot != -1) {
                    CrystalAura.mc.player.inventory.currentItem = this.oldSlot;
                    this.oldSlot = -1;
                }
                int crystalSlot = (CrystalAura.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? CrystalAura.mc.player.inventory.currentItem : -1;
                if (crystalSlot == -1) {
                    for (int l = 0; l < 9; ++l) {
                        if (CrystalAura.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                            crystalSlot = l;
                            break;
                        }
                    }
                }
                boolean offhand = false;
                if (CrystalAura.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                    offhand = true;
                }
                else if (crystalSlot == -1) {
                    return;
                }
                final List<BlockPos> blocks = this.findCrystalBlocks();
                final List<Entity> entities = new ArrayList<Entity>();
                entities.addAll((Collection<? extends Entity>)CrystalAura.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).sorted(Comparator.comparing(e -> CrystalAura.mc.player.getDistance(e))).collect(Collectors.toList()));
                BlockPos q = null;
                double damage = 0.5;
                for (final Entity entity2 : entities) {
                    if (entity2 == CrystalAura.mc.player) {
                        continue;
                    }
                    if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                        continue;
                    }
                    for (final BlockPos blockPos : blocks) {
                        final double b = entity2.getDistanceSq(blockPos);
                        if (b >= 56.2) {
                            continue;
                        }
                        final double d = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, entity2);
                        if (d <= damage || (d < this.minDmg.getValue() && ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() > this.facePlace.getValue())) {
                            continue;
                        }
                        final double self = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, (Entity)CrystalAura.mc.player);
                        if (self > this.maxSelfDmg.getValue()) {
                            continue;
                        }
                        if (self > d) {
                            continue;
                        }
                        damage = d;
                        q = blockPos;
                        this.renderEnt = entity2;
                    }
                }
                if (damage == 0.5) {
                    this.render = null;
                    this.renderEnt = null;
                    return;
                }
                this.render = q;
                if (this.place.getValue()) {
                    if (CrystalAura.mc.player == null) {
                        return;
                    }
                    if (!offhand && CrystalAura.mc.player.inventory.currentItem != crystalSlot) {
                        if (this.autoSwitch.getValue()) {
                            if (this.isEatingGap()) {
                                return;
                            }
                            CrystalAura.mc.player.inventory.currentItem = crystalSlot;
                            this.switchCooldown = true;
                        }
                        return;
                    }
                    if (this.switchCooldown) {
                        this.switchCooldown = false;
                        return;
                    }
                    CrystalAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
            }
            else if (CrystalAura.mc.player.canEntityBeSeen((Entity)crystal) || CrystalAura.mc.player.getDistance((Entity)crystal) <= this.walls.getValue()) {
                if (StopwatchUtil.hasCompleted(1000 / this.aps.getValue())) {
                    CrystalAura.mc.playerController.attackEntity((EntityPlayer)CrystalAura.mc.player, (Entity)crystal);
                    CrystalAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.sp.reset();
                }
                if (this.antiWeakness.getValue() && CrystalAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    int newSlot = -1;
                    for (int crystalSlot = 0; crystalSlot < 9; ++crystalSlot) {
                        final ItemStack stack = CrystalAura.mc.player.inventory.getStackInSlot(crystalSlot);
                        if (stack != ItemStack.EMPTY) {
                            if (stack.getItem() instanceof ItemSword) {
                                newSlot = crystalSlot;
                                break;
                            }
                            if (stack.getItem() instanceof ItemTool) {
                                newSlot = crystalSlot;
                                break;
                            }
                        }
                    }
                    if (newSlot != -1) {
                        CrystalAura.mc.player.inventory.currentItem = newSlot;
                        this.switchCooldown = true;
                    }
                }
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
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
        if (this.renderDamage.getValue() && this.render != null && this.renderEnt != null && CrystalAura.mc.player != null) {
            GlStateManager.pushMatrix();
            RenderUtil.glBillboardDistanceScaled(this.render.getX() + 0.5f, this.render.getY() + 0.5f, this.render.getZ() + 0.5f, (EntityPlayer)CrystalAura.mc.player, 1.0f);
            final double d = this.calculateDamage(this.render.getX() + 0.5, this.render.getY() + 1, this.render.getZ() + 0.5, this.renderEnt);
            final String damageText = ((Math.floor(d) == d) ? Integer.valueOf((int)d) : String.format("%.1f", d)) + "";
            GlStateManager.disableDepth();
            GlStateManager.translate(-(CrystalAura.mc.fontRenderer.getStringWidth(damageText) / 2.0), 0.0, 0.0);
            FontUtils.drawStringWithShadow(ClickGuiModule.customFont.getValue(), damageText, 0, 0, -1);
            GlStateManager.popMatrix();
        }
    }
    
    private boolean isEatingGap() {
        return CrystalAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && CrystalAura.mc.player.isHandActive();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAura.mc.player.posX), Math.floor(CrystalAura.mc.player.posY), Math.floor(CrystalAura.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        if (this.endcrystal.getValue()) {
            positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)this.placeRange.getValue(), (int)this.placeRange.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystalThirteen).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
        else {
            positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)this.placeRange.getValue(), (int)this.placeRange.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
        return (List<BlockPos>)positions;
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && CrystalAura.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && CrystalAura.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && CrystalAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && CrystalAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    private boolean canPlaceCrystalThirteen(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        return (CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && CrystalAura.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && CrystalAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty();
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
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)CrystalAura.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
        final int diff = CrystalAura.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public void onDisable() {
        this.render = null;
        this.renderEnt = null;
    }
}
