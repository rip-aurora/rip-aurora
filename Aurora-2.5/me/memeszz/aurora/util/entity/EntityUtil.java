
package me.memeszz.aurora.util.entity;

import java.io.IOException;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import me.memeszz.aurora.Aurora;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.text.DecimalFormat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.math.Vec3d;
import me.memeszz.aurora.util.Wrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class EntityUtil
{
    public static Minecraft mc;
    
    public static boolean mobTypeSettings(final Entity e, final Boolean mobs, final Boolean passive, final Boolean neutral, final Boolean hostile) {
        return mobs && ((passive && isPassiveMob(e)) || (neutral && isCurrentlyNeutral(e)) || (hostile && isMobAggressive(e)));
    }
    
    public static boolean isPassiveMob(final Entity e) {
        return e instanceof EntityAnimal || e instanceof EntityAgeable || e instanceof EntityTameable || e instanceof EntityAmbientCreature || e instanceof EntitySquid;
    }
    
    public static boolean isCurrentlyNeutral(final Entity entity) {
        return isNeutralMob(entity) && !isMobAggressive(entity);
    }
    
    public static boolean isPassive(final Entity e) {
        return (!(e instanceof EntityWolf) || !((EntityWolf)e).isAngry()) && (e instanceof EntityAnimal || e instanceof EntityAgeable || e instanceof EntityTameable || e instanceof EntityAmbientCreature || e instanceof EntitySquid || (e instanceof EntityIronGolem && ((EntityIronGolem)e).getRevengeTarget() == null));
    }
    
    public static int GetRecursiveItemSlot(final Item input) {
        if (EntityUtil.mc.player == null) {
            return 0;
        }
        for (int i = EntityUtil.mc.player.inventoryContainer.getInventory().size() - 1; i > 0; --i) {
            if (i != 0 && i != 5 && i != 6 && i != 7) {
                if (i != 8) {
                    final ItemStack s = (ItemStack)EntityUtil.mc.player.inventoryContainer.getInventory().get(i);
                    if (!s.isEmpty()) {
                        if (s.getItem() == input) {
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public static int GetItemSlotNotHotbar(final Item input) {
        if (EntityUtil.mc.player == null) {
            return 0;
        }
        for (int i = 9; i < 36; ++i) {
            final Item item = EntityUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                return i;
            }
        }
        return -1;
    }
    
    public static float GetHealthWithAbsorption() {
        return EntityUtil.mc.player.getHealth() + EntityUtil.mc.player.getAbsorptionAmount();
    }
    
    public static boolean isLiving(final Entity e) {
        return e instanceof EntityLivingBase;
    }
    
    public static boolean isFakeLocalPlayer(final Entity entity) {
        return entity != null && entity.getEntityId() == -100 && Wrapper.getPlayer() != entity;
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double x, final double y, final double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final Vec3d vec) {
        return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }
    
    public static boolean isMobAggressive(final Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry()) {
                return true;
            }
        }
        else {
            if (entity instanceof EntityWolf) {
                return ((EntityWolf)entity).isAngry() && !Wrapper.getPlayer().equals((Object)((EntityWolf)entity).getOwner());
            }
            if (entity instanceof EntityEnderman) {
                return ((EntityEnderman)entity).isScreaming();
            }
        }
        return isHostileMob(entity);
    }
    
    public static boolean isNeutralMob(final Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }
    
    public static boolean isFriendlyMob(final Entity entity) {
        return (entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity)) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof EntityVillager || entity instanceof EntityIronGolem || (isNeutralMob(entity) && !isMobAggressive(entity));
    }
    
    public static boolean isHostileMob(final Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity);
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }
    
    public static Vec3d getInterpolatedRenderPos(final Entity entity, final float ticks) {
        return getInterpolatedPos(entity, ticks).subtract(Wrapper.getMinecraft().getRenderManager().renderPosX, Wrapper.getMinecraft().getRenderManager().renderPosY, Wrapper.getMinecraft().getRenderManager().renderPosZ);
    }
    
    public static Vec3d getInterpolatedRenderPos(final Vec3d vec) {
        return new Vec3d(vec.x, vec.y, vec.z).subtract(Wrapper.getMinecraft().getRenderManager().renderPosX, Wrapper.getMinecraft().getRenderManager().renderPosY, Wrapper.getMinecraft().getRenderManager().renderPosZ);
    }
    
    public static boolean isInWater(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY + 0.01;
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, (int)y, z);
                if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isDrivenByPlayer(final Entity entityIn) {
        return Wrapper.getPlayer() != null && entityIn != null && entityIn.equals((Object)Wrapper.getPlayer().getRidingEntity());
    }
    
    public static boolean isAboveWater(final Entity entity) {
        return isAboveWater(entity, false);
    }
    
    public static boolean isAboveWater(final Entity entity, final boolean packet) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY - (packet ? 0.03 : (isPlayer(entity) ? 0.2 : 0.5));
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }
    
    public static int GetItemSlot(final Item input) {
        if (EntityUtil.mc.player == null) {
            return 0;
        }
        for (int i = 0; i < EntityUtil.mc.player.inventoryContainer.getInventory().size(); ++i) {
            if (i != 0 && i != 5 && i != 6 && i != 7) {
                if (i != 8) {
                    final ItemStack s = (ItemStack)EntityUtil.mc.player.inventoryContainer.getInventory().get(i);
                    if (!s.isEmpty()) {
                        if (s.getItem() == input) {
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public static boolean isPlayer(final Entity entity) {
        return entity instanceof EntityPlayer;
    }
    
    public static double getRelativeX(final float yaw) {
        return MathHelper.sin(-yaw * 0.017453292f);
    }
    
    public static double getRelativeZ(final float yaw) {
        return MathHelper.cos(yaw * 0.017453292f);
    }
    
    public static boolean isPlayerMovingKeybind() {
        return EntityUtil.mc.gameSettings.keyBindForward.isKeyDown() || EntityUtil.mc.gameSettings.keyBindBack.isKeyDown() || EntityUtil.mc.gameSettings.keyBindLeft.isKeyDown() || EntityUtil.mc.gameSettings.keyBindRight.isKeyDown();
    }
    
    public static boolean isPlayerMovingMomentum() {
        return EntityUtil.mc.player.moveForward > 0.0f || EntityUtil.mc.player.moveStrafing > 0.0f || EntityUtil.mc.player.moveForward < 0.0f || EntityUtil.mc.player.moveStrafing < 0.0f;
    }
    
    public static boolean isPlayerMovingLegit() {
        return EntityUtil.mc.gameSettings.keyBindForward.isKeyDown();
    }
    
    public static void getPlayerSpeed() {
        final DecimalFormat df = new DecimalFormat("#.#");
        final double deltaX = EntityUtil.mc.player.posX - EntityUtil.mc.player.prevPosX;
        final double deltaY = EntityUtil.mc.player.posY - EntityUtil.mc.player.prevPosY;
    }
    
    public static int getPing() {
        if (EntityUtil.mc.world == null || EntityUtil.mc.player == null || EntityUtil.mc.player.getUniqueID() == null) {
            return 0;
        }
        final NetworkPlayerInfo debugInfo = EntityUtil.mc.getConnection().getPlayerInfo(EntityUtil.mc.player.getUniqueID());
        if (debugInfo == null) {
            return 0;
        }
        return debugInfo.getResponseTime();
    }
    
    public static String getNameFromUUID(final String uuid) {
        try {
            Aurora.log.info("Attempting to get name from UUID: " + uuid);
            final String jsonUrl = IOUtils.toString(new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names"));
            final JsonParser parser = new JsonParser();
            return parser.parse(jsonUrl).getAsJsonArray().get(parser.parse(jsonUrl).getAsJsonArray().size() - 1).getAsJsonObject().get("name").toString();
        }
        catch (IOException ex) {
            Aurora.log.error((Object)ex.getStackTrace());
            Aurora.log.error("Failed to get username from UUID due to an exception. Maybe your internet is being the big gay? Somehow?");
            return null;
        }
    }
    
    static {
        EntityUtil.mc = Minecraft.getMinecraft();
    }
}
