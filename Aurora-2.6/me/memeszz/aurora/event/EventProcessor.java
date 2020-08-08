package me.memeszz.aurora.event;

import me.memeszz.aurora.util.macro.Macro;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.common.MinecraftForge;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import java.util.Iterator;
import me.memeszz.aurora.event.events.PlayerLeaveEvent;
import me.memeszz.aurora.event.events.PlayerJoinEvent;
import me.memeszz.aurora.mixin.accessor.IMinecraft;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import me.memeszz.aurora.event.events.PacketEvent;
import me.memeszz.aurora.util.Wrapper;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.command.Command;
import net.minecraftforge.client.event.ClientChatEvent;
import org.lwjgl.input.Mouse;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import me.memeszz.aurora.module.modules.render.ShulkerPreview;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.memeszz.aurora.Aurora;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import me.memeszz.aurora.module.ModuleManager;
import me.memeszz.aurora.event.events.UpdateEvent;
import com.google.common.collect.Maps;
import java.util.Map;
import me.memeszz.aurora.command.CommandManager;
import net.minecraft.client.Minecraft;
import java.awt.Color;

public class EventProcessor
{
    public static EventProcessor INSTANCE;
    public Color c;
    Minecraft mc;
    CommandManager commandManager;
    public static float hue;
    int rgb;
    int speed;
    private final Map<String, String> uuidNameCache;
    
    public EventProcessor() {
        this.mc = Minecraft.getMinecraft();
        this.commandManager = new CommandManager();
        this.speed = 2;
        this.uuidNameCache = (Map<String, String>)Maps.newConcurrentMap();
        EventProcessor.INSTANCE = this;
    }
    
    public int getRgb() {
        return this.rgb;
    }
    
    public Color getC() {
        return this.c;
    }
    
    public void setRainbowSpeed(final int s) {
        this.speed = s;
    }
    
    public int getRainbowSpeed() {
        return this.speed;
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        this.c = Color.getHSBColor(EventProcessor.hue, 1.0f, 1.0f);
        this.rgb = Color.HSBtoRGB(EventProcessor.hue, 1.0f, 1.0f);
        EventProcessor.hue += this.speed / 2000.0f;
        if (this.mc.player != null || this.mc.world != null) {
            ModuleManager.onUpdate();
        }
    }
    
    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Aurora.saveConfig();
        System.out.println("Saved Aurora config!");
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        ModuleManager.onWorldRender(event);
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        Aurora.getInstance().getEventManager().dispatchEvent(event);
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            ModuleManager.onRender();
        }
    }
    
    @SubscribeEvent
    public void onGuiRenderScreen(final GuiScreenEvent event) {
        if (ShulkerPreview.active || ShulkerPreview.pinned) {
            final NonNullList<ItemStack> nonnulllist = (NonNullList<ItemStack>)NonNullList.withSize(27, (Object)ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(ShulkerPreview.nbt, (NonNullList)nonnulllist);
            GlStateManager.enableBlend();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.mc.getRenderItem().zLevel = 300.0f;
            final int oldX = ShulkerPreview.drawX;
            final int oldY = ShulkerPreview.drawY;
            Gui.drawRect(oldX + 9, oldY - 14, oldX + 173, oldY + 52, -1441722095);
            this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/shulker_box.png"));
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.ingameGUI.drawTexturedModalRect(oldX + 10, oldY - 4, 7, 17, 162, 54);
            this.mc.fontRenderer.drawString(ShulkerPreview.itemStack.getDisplayName(), oldX + 12, oldY - 12, 16777215);
            GlStateManager.enableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final int iX = oldX + i % 9 * 18 + 11;
                final int iY = oldY + i / 9 * 18 - 11 + 8;
                final ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, iX, iY);
                this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRenderer, itemStack, iX, iY, (String)null);
                if (ShulkerPreview.pinned && this.isPointInRegion(iX, iY, 18, 18, ShulkerPreview.mouseX, ShulkerPreview.mouseY)) {
                    final FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
                    GuiUtils.preItemToolTip(itemStack);
                    event.getGui().drawHoveringText(event.getGui().getItemToolTip(itemStack), iX, iY);
                    GuiUtils.postItemToolTip();
                }
            }
            RenderHelper.disableStandardItemLighting();
            this.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            ShulkerPreview.active = false;
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 0 || Keyboard.getEventKey() == 0) {
                return;
            }
            ModuleManager.onBind(Keyboard.getEventKey());
            Aurora.getInstance().macroManager.getMacros().forEach(m -> {
                if (m.getKey() == Keyboard.getEventKey()) {
                    m.onMacro();
                }
            });
        }
    }
    
    @SubscribeEvent
    public void onMouseInput(final InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            Aurora.getInstance().getEventManager().dispatchEvent(event);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getPrefix())) {
            event.setCanceled(true);
            try {
                this.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                this.commandManager.callCommand(event.getMessage().substring(1));
            }
            catch (Exception e) {
                e.printStackTrace();
                Wrapper.sendClientMessage(ChatFormatting.DARK_RED + "Error: " + e.getMessage());
            }
        }
    }
    
    @Listener
    public void onPacketRecieve(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerListItem) {
            final SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (packet.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                for (final SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                    if (playerData.getProfile().getId() != ((IMinecraft)this.mc).getSession().getProfile().getId()) {
                        final String name;
                        new Thread(() -> {
                            name = this.resolveName(playerData.getProfile().getId().toString());
                            if (name != null && this.mc.player != null && this.mc.player.ticksExisted >= 1000) {
                                Aurora.getInstance().getEventManager().dispatchEvent(new PlayerJoinEvent(name));
                            }
                            return;
                        }).start();
                    }
                }
            }
            if (packet.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                for (final SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                    if (playerData.getProfile().getId() != ((IMinecraft)this.mc).getSession().getProfile().getId()) {
                        final SPacketPlayerListItem.AddPlayerData playerData2;
                        final String name2;
                        new Thread(() -> {
                            name2 = this.resolveName(playerData2.getProfile().getId().toString());
                            if (name2 != null && this.mc.player != null && this.mc.player.ticksExisted >= 1000) {
                                Aurora.getInstance().getEventManager().dispatchEvent(new PlayerLeaveEvent(name2));
                            }
                        }).start();
                    }
                }
            }
        }
    }
    
    public String resolveName(String uuid) {
        uuid = uuid.replace("-", "");
        if (this.uuidNameCache.containsKey(uuid)) {
            return this.uuidNameCache.get(uuid);
        }
        final String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
        try {
            final String nameJson = IOUtils.toString(new URL(url));
            if (nameJson != null && nameJson.length() > 0) {
                final JSONArray jsonArray = (JSONArray)JSONValue.parseWithException(nameJson);
                if (jsonArray != null) {
                    final JSONObject latestName = jsonArray.get(jsonArray.size() - 1);
                    if (latestName != null) {
                        return latestName.get("name").toString();
                    }
                }
            }
        }
        catch (IOException | ParseException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
        return null;
    }
    
    public void init() {
        Aurora.getInstance().getEventManager().addEventListener(this);
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    protected boolean isPointInRegion(final int rectX, final int rectY, final int rectWidth, final int rectHeight, int pointX, int pointY) {
        final int i = ShulkerPreview.guiLeft;
        final int j = ShulkerPreview.guiTop;
        pointX -= i;
        pointY -= j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }
    
    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        Aurora.getInstance().getEventManager().dispatchEvent(event);
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        Aurora.getInstance().getEventManager().dispatchEvent(event);
    }
    
    static {
        EventProcessor.hue = 0.0f;
    }
}
