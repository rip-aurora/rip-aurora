
package me.memeszz.aurora.module.modules.world;

import java.text.DecimalFormat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import java.util.function.Predicate;
import me.memeszz.aurora.util.Wrapper;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import java.util.concurrent.ThreadLocalRandom;
import me.memeszz.aurora.event.events.PlayerJumpEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.memeszz.aurora.event.events.DestroyBlockEvent;
import me.memeszz.aurora.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import me.zero.alpine.listener.Listener;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.module.Module;

public class Announcer extends Module
{
    public static int blockBrokeDelay;
    static int blockPlacedDelay;
    static int jumpDelay;
    static int attackDelay;
    static int eattingDelay;
    static long lastPositionUpdate;
    static double lastPositionX;
    static double lastPositionY;
    static double lastPositionZ;
    private static double speed;
    String heldItem;
    int blocksPlaced;
    int blocksBroken;
    int eaten;
    public Setting.b clientSide;
    private Setting.b walk;
    private Setting.b place;
    private Setting.b jump;
    private Setting.b breaking;
    private Setting.b attack;
    private Setting.b eat;
    public Setting.b clickGui;
    private Setting.d delay;
    public static String walkMessage;
    public static String placeMessage;
    public static String jumpMessage;
    public static String breakMessage;
    public static String attackMessage;
    public static String eatMessage;
    @EventHandler
    private final Listener<LivingEntityUseItemEvent.Finish> eatListener;
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener;
    @EventHandler
    private final Listener<DestroyBlockEvent> destroyListener;
    @EventHandler
    private final Listener<AttackEntityEvent> attackListener;
    @EventHandler
    private final Listener<PlayerJumpEvent> jumpListener;
    
    public Announcer() {
        super("Announcer", Category.WORLD, "Announces what you do in chat");
        this.heldItem = "";
        this.blocksPlaced = 0;
        this.blocksBroken = 0;
        this.eaten = 0;
        final int randomNum;
        this.eatListener = new Listener<LivingEntityUseItemEvent.Finish>(event -> {
            randomNum = ThreadLocalRandom.current().nextInt(1, 11);
            if (event.getEntity() == Announcer.mc.player && (event.getItem().getItem() instanceof ItemFood || event.getItem().getItem() instanceof ItemAppleGold)) {
                ++this.eaten;
                if (Announcer.eattingDelay >= 300.0 * this.delay.getValue() && this.eat.getValue() && this.eaten > randomNum) {
                    if (this.clientSide.getValue()) {
                        Wrapper.sendClientMessage(Announcer.eatMessage.replace("{amount}", this.eaten + "").replace("{name}", Announcer.mc.player.getHeldItemMainhand().getDisplayName()));
                    }
                    else {
                        Announcer.mc.player.sendChatMessage(Announcer.eatMessage.replace("{amount}", this.eaten + "").replace("{name}", Announcer.mc.player.getHeldItemMainhand().getDisplayName()));
                    }
                    this.eaten = 0;
                    Announcer.eattingDelay = 0;
                }
            }
            return;
        }, (Predicate<LivingEntityUseItemEvent.Finish>[])new Predicate[0]);
        int randomNum2;
        String msg;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && Announcer.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) {
                ++this.blocksPlaced;
                randomNum2 = ThreadLocalRandom.current().nextInt(1, 11);
                if (Announcer.blockPlacedDelay >= 150.0 * this.delay.getValue() && this.place.getValue() && this.blocksPlaced > randomNum2) {
                    msg = Announcer.placeMessage.replace("{amount}", this.blocksPlaced + "").replace("{name}", Announcer.mc.player.getHeldItemMainhand().getDisplayName());
                    if (this.clientSide.getValue()) {
                        Wrapper.sendClientMessage(msg);
                    }
                    else {
                        Announcer.mc.player.sendChatMessage(msg);
                    }
                    this.blocksPlaced = 0;
                    Announcer.blockPlacedDelay = 0;
                }
            }
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        final int randomNum3;
        String msg2;
        this.destroyListener = new Listener<DestroyBlockEvent>(event -> {
            ++this.blocksBroken;
            randomNum3 = ThreadLocalRandom.current().nextInt(1, 11);
            if (Announcer.blockBrokeDelay >= 300.0 * this.delay.getValue() && this.breaking.getValue() && this.blocksBroken > randomNum3) {
                msg2 = Announcer.breakMessage.replace("{amount}", this.blocksBroken + "").replace("{name}", Announcer.mc.world.getBlockState(event.getBlockPos()).getBlock().getLocalizedName());
                if (this.clientSide.getValue()) {
                    Wrapper.sendClientMessage(msg2);
                }
                else {
                    Announcer.mc.player.sendChatMessage(msg2);
                }
                this.blocksBroken = 0;
                Announcer.blockBrokeDelay = 0;
            }
            return;
        }, (Predicate<DestroyBlockEvent>[])new Predicate[0]);
        String msg3;
        this.attackListener = new Listener<AttackEntityEvent>(event -> {
            if (this.attack.getValue() && !(event.getTarget() instanceof EntityEnderCrystal) && Announcer.attackDelay >= 300.0 * this.delay.getValue()) {
                msg3 = Announcer.attackMessage.replace("{name}", event.getTarget().getName()).replace("{item}", Announcer.mc.player.getHeldItemMainhand().getDisplayName());
                if (this.clientSide.getValue()) {
                    Wrapper.sendClientMessage(msg3);
                }
                else {
                    Announcer.mc.player.sendChatMessage(msg3);
                }
                Announcer.attackDelay = 0;
            }
            return;
        }, (Predicate<AttackEntityEvent>[])new Predicate[0]);
        this.jumpListener = new Listener<PlayerJumpEvent>(event -> {
            if (this.jump.getValue() && Announcer.jumpDelay >= 300.0 * this.delay.getValue()) {
                if (this.clientSide.getValue()) {
                    Wrapper.sendClientMessage(Announcer.jumpMessage);
                }
                else {
                    Announcer.mc.player.sendChatMessage(Announcer.jumpMessage);
                }
                Announcer.jumpDelay = 0;
            }
        }, (Predicate<PlayerJumpEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        this.clientSide = this.registerB("ClientSide", false);
        this.walk = this.registerB("Walk", true);
        this.place = this.registerB("BlockPlace", false);
        this.jump = this.registerB("Jump", true);
        this.breaking = this.registerB("BlockBreak", false);
        this.attack = this.registerB("AttackEntity", false);
        this.eat = this.registerB("Eat", true);
        this.delay = this.registerD("DelayMultiplier", 1.0, 1.0, 20.0);
    }
    
    @Override
    public void onUpdate() {
        ++Announcer.blockBrokeDelay;
        ++Announcer.blockPlacedDelay;
        ++Announcer.jumpDelay;
        ++Announcer.attackDelay;
        ++Announcer.eattingDelay;
        this.heldItem = Announcer.mc.player.getHeldItemMainhand().getDisplayName();
        if (this.walk.getValue() && Announcer.lastPositionUpdate + 5000.0 * this.delay.getValue() < System.currentTimeMillis()) {
            final double d0 = Announcer.lastPositionX - Announcer.mc.player.lastTickPosX;
            final double d2 = Announcer.lastPositionY - Announcer.mc.player.lastTickPosY;
            final double d3 = Announcer.lastPositionZ - Announcer.mc.player.lastTickPosZ;
            Announcer.speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
            if (Announcer.speed > 1.0 && Announcer.speed <= 5000.0) {
                final String walkAmount = new DecimalFormat("0").format(Announcer.speed);
                if (this.clientSide.getValue()) {
                    Wrapper.sendClientMessage(Announcer.walkMessage.replace("{blocks}", walkAmount));
                }
                else {
                    Announcer.mc.player.sendChatMessage(Announcer.walkMessage.replace("{blocks}", walkAmount));
                }
                Announcer.lastPositionUpdate = System.currentTimeMillis();
                Announcer.lastPositionX = Announcer.mc.player.lastTickPosX;
                Announcer.lastPositionY = Announcer.mc.player.lastTickPosY;
                Announcer.lastPositionZ = Announcer.mc.player.lastTickPosZ;
            }
        }
    }
    
    public void onEnable() {
        this.blocksPlaced = 0;
        this.blocksBroken = 0;
        this.eaten = 0;
        Announcer.speed = 0.0;
        Announcer.blockBrokeDelay = 0;
        Announcer.blockPlacedDelay = 0;
        Announcer.jumpDelay = 0;
        Announcer.attackDelay = 0;
        Announcer.eattingDelay = 0;
    }
    
    static {
        Announcer.blockBrokeDelay = 0;
        Announcer.blockPlacedDelay = 0;
        Announcer.jumpDelay = 0;
        Announcer.attackDelay = 0;
        Announcer.eattingDelay = 0;
        Announcer.walkMessage = "I just fucking flew {blocks} blocks thanks to the power of my sexy bald head Aurora!";
        Announcer.placeMessage = "I just placed {amount} {name} thanks to power of my sexy bald head Aurora!";
        Announcer.jumpMessage = "I just jumped thanks to the power of this big black head Aurora!";
        Announcer.breakMessage = "I just broke {amount} {name} thanks to the power of my big sexy head Aurora!";
        Announcer.attackMessage = "I just attacked {name} with a {item} thanks to my big bald sexy head Aurora!";
        Announcer.eatMessage = "I just ate {amount} {name} thanks to the power of my sexy bald head nigga Aurora!";
    }
}
