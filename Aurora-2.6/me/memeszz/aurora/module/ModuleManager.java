package me.memeszz.aurora.module;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.Tessellator;
import me.memeszz.aurora.event.events.RenderEvent;
import net.minecraft.entity.Entity;
import me.memeszz.aurora.util.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.memeszz.aurora.module.modules.gui.PotionEffects;
import me.memeszz.aurora.module.modules.gui.TextRadar;
import me.memeszz.aurora.module.modules.gui.Pvpinfo;
import me.memeszz.aurora.module.modules.render.Chams;
import me.memeszz.aurora.module.modules.gui.Compass;
import me.memeszz.aurora.module.modules.gui.Hud;
import me.memeszz.aurora.module.modules.gui.DurabiltyWarning;
import me.memeszz.aurora.module.modules.gui.ClickGuiModule;
import me.memeszz.aurora.module.modules.gui.AuroraGang;
import me.memeszz.aurora.module.modules.render.OldChangeItemAnim;
import me.memeszz.aurora.module.modules.render.Trajectories;
import me.memeszz.aurora.module.modules.render.SkyColor;
import me.memeszz.aurora.module.modules.render.ShulkerPreview;
import me.memeszz.aurora.module.modules.render.NoRender;
import me.memeszz.aurora.module.modules.render.NoLag;
import me.memeszz.aurora.module.modules.render.NameTags;
import me.memeszz.aurora.module.modules.render.MobOwner;
import me.memeszz.aurora.module.modules.render.HoleESP;
import me.memeszz.aurora.module.modules.render.Fov;
import me.memeszz.aurora.module.modules.render.CameraClip;
import me.memeszz.aurora.module.modules.render.Brightness;
import me.memeszz.aurora.module.modules.render.BlockHighlight;
import me.memeszz.aurora.module.modules.render.AntiFog;
import me.memeszz.aurora.module.modules.world.Welcomer;
import me.memeszz.aurora.module.modules.world.VisualRange;
import me.memeszz.aurora.module.modules.misc.ToggleMsgs;
import me.memeszz.aurora.module.modules.world.str2detect;
import me.memeszz.aurora.module.modules.world.ChatTimeStamps;
import me.memeszz.aurora.module.modules.world.BetterChat;
import me.memeszz.aurora.module.modules.world.AutoGG;
import me.memeszz.aurora.module.modules.world.Announcer;
import me.memeszz.aurora.module.modules.misc.MiddleClickFriends;
import me.memeszz.aurora.module.modules.movement.GuiMove;
import me.memeszz.aurora.module.modules.misc.DonkeyAlert;
import me.memeszz.aurora.module.modules.misc.ChatSuffix;
import me.memeszz.aurora.module.modules.misc.FriendsTab;
import me.memeszz.aurora.module.modules.misc.AutoRespawn;
import me.memeszz.aurora.module.modules.movement.Velocity;
import me.memeszz.aurora.module.modules.player.FastWebFall;
import me.memeszz.aurora.module.modules.movement.Speed;
import me.memeszz.aurora.module.modules.movement.Sprint;
import me.memeszz.aurora.module.modules.movement.ReverseStep;
import me.memeszz.aurora.module.modules.movement.Step;
import me.memeszz.aurora.module.modules.movement.NoSlow;
import me.memeszz.aurora.module.modules.movement.IceSpeed;
import me.memeszz.aurora.module.modules.movement.FastSwim;
import me.memeszz.aurora.module.modules.player.SpeedMine;
import me.memeszz.aurora.module.modules.player.Reach;
import me.memeszz.aurora.module.modules.player.PortalGodMode;
import me.memeszz.aurora.module.modules.player.NoSwing;
import me.memeszz.aurora.module.modules.player.NoMiningTrace;
import me.memeszz.aurora.module.modules.player.FastUse;
import me.memeszz.aurora.module.modules.player.FakePlayer;
import me.memeszz.aurora.module.modules.player.Blink;
import me.memeszz.aurora.module.modules.player.AutoReplanish;
import me.memeszz.aurora.module.modules.player.AntiVoid;
import me.memeszz.aurora.module.modules.player.AutoBedCraft;
import me.memeszz.aurora.module.modules.combat.FootXp;
import me.memeszz.aurora.module.modules.combat.EzPearl;
import me.memeszz.aurora.module.modules.combat.AutoWeb;
import me.memeszz.aurora.module.modules.combat.HoleFill;
import me.memeszz.aurora.module.modules.combat.Surround;
import me.memeszz.aurora.module.modules.combat.MultiTask;
import me.memeszz.aurora.module.modules.combat.Criticals;
import me.memeszz.aurora.module.modules.combat.TotemPopCounter;
import me.memeszz.aurora.module.modules.combat.OffHandGap;
import me.memeszz.aurora.module.modules.combat.AutoEchest;
import me.memeszz.aurora.module.modules.combat.OffHandCrystal;
import me.memeszz.aurora.module.modules.combat.KillAura;
import me.memeszz.aurora.module.modules.combat.AutoArmour;
import me.memeszz.aurora.module.modules.combat.AutoTotem;
import me.memeszz.aurora.module.modules.combat.AutoTrap;
import me.memeszz.aurora.module.modules.combat.AutoCrystal;
import java.util.ArrayList;

public class ModuleManager
{
    public static ArrayList<Module> modules;
    
    public ModuleManager() {
        ModuleManager.modules = new ArrayList<Module>();
        addMod(new AutoCrystal());
        addMod(new AutoTrap());
        addMod(new AutoTotem());
        addMod(new AutoArmour());
        addMod(new KillAura());
        addMod(new OffHandCrystal());
        addMod(new AutoEchest());
        addMod(new OffHandGap());
        addMod(new TotemPopCounter());
        addMod(new Criticals());
        addMod(new MultiTask());
        addMod(new Surround());
        addMod(new HoleFill());
        addMod(new AutoWeb());
        addMod(new EzPearl());
        addMod(new FootXp());
        addMod(new AutoBedCraft());
        addMod(new AntiVoid());
        addMod(new AutoReplanish());
        addMod(new Blink());
        addMod(new FakePlayer());
        addMod(new FastUse());
        addMod(new NoMiningTrace());
        addMod(new NoSwing());
        addMod(new PortalGodMode());
        addMod(new Reach());
        addMod(new SpeedMine());
        addMod(new FastSwim());
        addMod(new IceSpeed());
        addMod(new NoSlow());
        addMod(new Step());
        addMod(new ReverseStep());
        addMod(new Sprint());
        addMod(new Speed());
        addMod(new FastWebFall());
        addMod(new Velocity());
        addMod(new AutoRespawn());
        addMod(new FriendsTab());
        addMod(new ChatSuffix());
        addMod(new DonkeyAlert());
        addMod(new GuiMove());
        addMod(new MiddleClickFriends());
        addMod(new Announcer());
        addMod(new AutoGG());
        addMod(new BetterChat());
        addMod(new ChatTimeStamps());
        addMod(new str2detect());
        addMod(new ToggleMsgs());
        addMod(new VisualRange());
        addMod(new Welcomer());
        addMod(new AntiFog());
        addMod(new BlockHighlight());
        addMod(new Brightness());
        addMod(new CameraClip());
        addMod(new Fov());
        addMod(new HoleESP());
        addMod(new MobOwner());
        addMod(new NameTags());
        addMod(new NoLag());
        addMod(new NoRender());
        addMod(new ShulkerPreview());
        addMod(new SkyColor());
        addMod(new Trajectories());
        addMod(new OldChangeItemAnim());
        addMod(new AuroraGang());
        addMod(new ClickGuiModule());
        addMod(new DurabiltyWarning());
        addMod(new Hud());
        addMod(new Compass());
        addMod(new Chams());
        addMod(new Pvpinfo());
        addMod(new TextRadar());
        addMod(new PotionEffects());
    }
    
    public static void addMod(final Module m) {
        ModuleManager.modules.add(m);
    }
    
    public static void onUpdate() {
        ModuleManager.modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
    }
    
    public static void onRender() {
        ModuleManager.modules.stream().filter(Module::isEnabled).forEach(Module::onRender);
    }
    
    public static void onWorldRender(final RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("aurora");
        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        final Vec3d renderPos = RenderUtil.getInterpolatedPos((Entity)Minecraft.getMinecraft().player, event.getPartialTicks());
        final RenderEvent e = new RenderEvent(RenderUtil.INSTANCE, renderPos, event.getPartialTicks());
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();
        final RenderEvent event2;
        ModuleManager.modules.stream().filter(Module::isEnabled).forEach(module -> {
            Minecraft.getMinecraft().profiler.startSection(module.getName());
            module.onWorldRender(event2);
            Minecraft.getMinecraft().profiler.endSection();
            return;
        });
        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        RenderUtil.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
        Minecraft.getMinecraft().profiler.endSection();
    }
    
    public static ArrayList<Module> getModules() {
        return ModuleManager.modules;
    }
    
    public static ArrayList<Module> getModulesInCategory(final Module.Category c) {
        return getModules().stream().filter(m -> m.getCategory().equals(c)).collect((Collector<? super Object, ?, ArrayList<Module>>)Collectors.toList());
    }
    
    public static void onBind(final int key) {
        if (key == 0) {
            return;
        }
        ModuleManager.modules.forEach(module -> {
            if (module.getBind() == key) {
                module.toggle();
            }
        });
    }
    
    public static Module getModuleByName(final String name) {
        return getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public static boolean isModuleEnabled(final String name) {
        final Module m = getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        assert m != null;
        return m.isEnabled();
    }
    
    public static boolean isModuleEnabled(final Module m) {
        return m.isEnabled();
    }
}
