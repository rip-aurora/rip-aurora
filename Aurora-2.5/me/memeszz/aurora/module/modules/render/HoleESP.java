
package me.memeszz.aurora.module.modules.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import net.minecraft.util.NonNullList;
import net.minecraft.init.Blocks;
import me.memeszz.aurora.util.entity.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import me.memeszz.aurora.util.render.RenderUtil;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import me.memeszz.aurora.event.events.RenderEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class HoleESP extends Module
{
    Setting.d range;
    Setting.i r;
    Setting.i g;
    Setting.i b;
    Setting.i a;
    Setting.i r2;
    Setting.i g2;
    Setting.i b2;
    Setting.i width;
    Setting.b highlight;
    Setting.b box;
    Setting.b bottomBox;
    Setting.b obbyChroma;
    Setting.b bRockChroma;
    Setting.b bottom;
    Setting.d chromaSpeed;
    Setting.i a2;
    private final BlockPos[] surroundOffset;
    private BlockPos render;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    
    public HoleESP() {
        super("HoleESP", Category.RENDER, "Shows holes nigga");
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
    }
    
    @Override
    public void setup() {
        this.highlight = this.registerB("BoxFill", true);
        this.box = this.registerB("BoxOutLine", true);
        this.bottom = this.registerB("BottomFill", true);
        this.bottomBox = this.registerB("BottomOutLine", true);
        this.obbyChroma = this.registerB("ObbyChroma", false);
        this.bRockChroma = this.registerB("BRockChroma", false);
        this.range = this.registerD("Range", 8.0, 0.0, 20.0);
        this.chromaSpeed = this.registerD("ChromaSpeed", 8.0, 0.0, 20.0);
        this.r = this.registerI("Red", 255, 0, 255);
        this.g = this.registerI("Green", 255, 0, 255);
        this.b = this.registerI("Blue", 255, 0, 255);
        this.r2 = this.registerI("RedBedrock", 255, 0, 255);
        this.g2 = this.registerI("GreenBedrock", 255, 0, 255);
        this.b2 = this.registerI("BlueBedrock", 255, 0, 255);
        this.a = this.registerI("Alpha", 26, 0, 255);
        this.a2 = this.registerI("AlphaBounding", 255, 0, 255);
        this.width = this.registerI("LineWidth", 3, 1, 10);
    }
    
    @Override
    public void onUpdate() {
        final List<BlockPos> bRockHoles = this.findBRockHoles();
        final List<BlockPos> obbyHoles = this.findObbyHoles();
        BlockPos q = null;
        Iterator<BlockPos> iterator = bRockHoles.iterator();
        while (iterator.hasNext()) {
            final BlockPos blockPos = q = iterator.next();
        }
        iterator = obbyHoles.iterator();
        while (iterator.hasNext()) {
            final BlockPos blockPos = q = iterator.next();
        }
        this.render = q;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final float[] hue = { (float)(System.currentTimeMillis() % 11520L / 11520.0f * this.chromaSpeed.getValue()) };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int red = rgb >> 16 & 0xFF;
        final int green = rgb >> 8 & 0xFF;
        final int blue = rgb & 0xFF;
        GL11.glEnable(2884);
        if (this.render != null) {
            for (final BlockPos hole : this.findObbyHoles()) {
                if (this.obbyChroma.getValue()) {
                    if (this.highlight.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBox(hole, red, green, blue, this.a.getValue(), 63);
                        RenderUtil.release();
                    }
                    if (this.bottom.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBoxBottom(hole, red, green, blue, this.a.getValue());
                        RenderUtil.release();
                    }
                    if (this.box.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBoundingBoxBlockPos(hole, (float)this.width.getValue(), red, green, blue, this.a2.getValue());
                        RenderUtil.release();
                    }
                    if (!this.bottomBox.getValue()) {
                        continue;
                    }
                    RenderUtil.prepare(7);
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float)this.width.getValue(), red, green, blue, this.a2.getValue());
                    RenderUtil.release();
                }
                else {
                    if (this.highlight.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBox(hole, this.r.getValue(), this.g.getValue(), this.b.getValue(), this.a.getValue(), 63);
                        RenderUtil.release();
                    }
                    if (this.box.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBoundingBoxBlockPos(hole, (float)this.width.getValue(), this.r.getValue(), this.g.getValue(), this.b.getValue(), this.a2.getValue());
                        RenderUtil.release();
                    }
                    if (this.bottom.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBoxBottom(hole, this.r.getValue(), this.g.getValue(), this.b.getValue(), this.a.getValue());
                        RenderUtil.release();
                    }
                    if (!this.bottomBox.getValue()) {
                        continue;
                    }
                    RenderUtil.prepare(7);
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float)this.width.getValue(), this.r.getValue(), this.g.getValue(), this.b.getValue(), this.a2.getValue());
                    RenderUtil.release();
                }
            }
            for (final BlockPos hole : this.findBRockHoles()) {
                if (this.bRockChroma.getValue()) {
                    if (this.highlight.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBox(hole, red, green, blue, this.a.getValue(), 63);
                        RenderUtil.release();
                    }
                    if (this.box.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBoundingBoxBlockPos(hole, (float)this.width.getValue(), red, green, blue, this.a2.getValue());
                        RenderUtil.release();
                    }
                    if (this.bottom.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBoxBottom(hole, red, green, blue, this.a.getValue());
                        RenderUtil.release();
                    }
                    if (!this.bottomBox.getValue()) {
                        continue;
                    }
                    RenderUtil.prepare(7);
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float)this.width.getValue(), red, green, blue, this.a2.getValue());
                    RenderUtil.release();
                }
                else {
                    if (this.highlight.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBox(hole, this.r2.getValue(), this.g2.getValue(), this.b2.getValue(), this.a.getValue(), 63);
                        RenderUtil.release();
                    }
                    if (this.bottom.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBoxBottom(hole, this.r2.getValue(), this.g2.getValue(), this.b2.getValue(), this.a.getValue());
                        RenderUtil.release();
                    }
                    if (this.box.getValue()) {
                        RenderUtil.prepare(7);
                        RenderUtil.drawBoundingBoxBlockPos(hole, (float)this.width.getValue(), this.r2.getValue(), this.g2.getValue(), this.b2.getValue(), this.a2.getValue());
                        RenderUtil.release();
                    }
                    if (!this.bottomBox.getValue()) {
                        continue;
                    }
                    RenderUtil.prepare(7);
                    RenderUtil.drawBoundingBoxBottomBlockPos(hole, (float)this.width.getValue(), this.r2.getValue(), this.g2.getValue(), this.b2.getValue(), this.a2.getValue());
                    RenderUtil.release();
                }
            }
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean IsObbyHole(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 0, 0);
        final BlockPos boost3 = blockPos.add(0, 0, -1);
        final BlockPos boost4 = blockPos.add(1, 0, 0);
        final BlockPos boost5 = blockPos.add(-1, 0, 0);
        final BlockPos boost6 = blockPos.add(0, 0, 1);
        final BlockPos boost7 = blockPos.add(0, 2, 0);
        final BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        final BlockPos boost9 = blockPos.add(0, -1, 0);
        return HoleESP.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && !this.IsBRockHole(blockPos) && HoleESP.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && (HoleESP.mc.world.getBlockState(boost3).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(boost4).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(boost5).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK) && (HoleESP.mc.world.getBlockState(boost6).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK) && HoleESP.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && (HoleESP.mc.world.getBlockState(boost9).getBlock() == Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK);
    }
    
    private boolean IsBRockHole(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 0, 0);
        final BlockPos boost3 = blockPos.add(0, 0, -1);
        final BlockPos boost4 = blockPos.add(1, 0, 0);
        final BlockPos boost5 = blockPos.add(-1, 0, 0);
        final BlockPos boost6 = blockPos.add(0, 0, 1);
        final BlockPos boost7 = blockPos.add(0, 2, 0);
        final BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        final BlockPos boost9 = blockPos.add(0, -1, 0);
        return HoleESP.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK;
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(HoleESP.mc.player.posX), Math.floor(HoleESP.mc.player.posY), Math.floor(HoleESP.mc.player.posZ));
    }
    
    private List<BlockPos> findObbyHoles() {
        final NonNullList positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)this.range.getValue(), (int)this.range.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::IsObbyHole).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    private List<BlockPos> findBRockHoles() {
        final NonNullList positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)this.range.getValue(), (int)this.range.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::IsBRockHole).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f = sphere ? (cy + r) : ((float)(cy + h));
                    if (y >= f) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        HoleESP.yaw = yaw1;
        HoleESP.pitch = pitch1;
        HoleESP.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (HoleESP.isSpoofingAngles) {
            HoleESP.yaw = HoleESP.mc.player.rotationYaw;
            HoleESP.pitch = HoleESP.mc.player.rotationPitch;
            HoleESP.isSpoofingAngles = false;
        }
    }
    
    public void onDisable() {
        this.render = null;
        resetRotation();
    }
}
