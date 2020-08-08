package me.memeszz.aurora.module;

import java.util.List;
import me.memeszz.aurora.setting.Setting;
import me.memeszz.aurora.util.Wrapper;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.Aurora;
import me.memeszz.aurora.event.events.RenderEvent;
import net.minecraft.client.Minecraft;

public class Module
{
    protected static final Minecraft mc;
    String name;
    Category category;
    int bind;
    boolean enabled;
    boolean drawn;
    String description;
    
    public Module(final String n, final Category c) {
        this.name = n;
        this.category = c;
        this.bind = 0;
        this.enabled = false;
        this.drawn = true;
        this.description = "No description";
        this.setup();
    }
    
    public Module(final String n, final Category c, final String desc) {
        this.name = n;
        this.category = c;
        this.bind = 0;
        this.enabled = false;
        this.drawn = true;
        this.description = desc;
        this.setup();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String n) {
        this.name = n;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(final Category c) {
        this.category = c;
    }
    
    public int getBind() {
        return this.bind;
    }
    
    public void setBind(final int b) {
        this.bind = b;
    }
    
    protected void onEnable() {
    }
    
    protected void onDisable() {
    }
    
    public void onRender() {
    }
    
    public void onUpdate() {
    }
    
    public void onWorldRender(final RenderEvent event) {
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean e) {
        this.enabled = e;
    }
    
    public void enable() {
        Aurora.getInstance().getEventManager().addEventListener(this);
        this.setEnabled(true);
        if (ModuleManager.isModuleEnabled("ToggleMsgs") && !this.getName().equalsIgnoreCase("ClickGUI")) {
            Wrapper.sendClientMessage(ChatFormatting.YELLOW + this.getName() + ChatFormatting.GREEN + " Enabled.");
        }
        this.onEnable();
    }
    
    public void disable() {
        this.setEnabled(false);
        if (ModuleManager.isModuleEnabled("ToggleMsgs") && !this.getName().equalsIgnoreCase("ClickGUI")) {
            Wrapper.sendClientMessage(ChatFormatting.YELLOW + this.getName() + ChatFormatting.RED + " Disabled.");
        }
        this.onDisable();
        Aurora.getInstance().getEventManager().removeEventListener(this);
    }
    
    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        }
        else if (!this.isEnabled()) {
            this.enable();
        }
    }
    
    protected Setting.i registerI(final String name, final int value, final int min, final int max) {
        final Setting.i s = new Setting.i(name, this, value, min, max);
        Aurora.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    protected Setting.d registerD(final String name, final double value, final double min, final double max) {
        final Setting.d s = new Setting.d(name, this, value, min, max);
        Aurora.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    protected Setting.b registerB(final String name, final boolean value) {
        final Setting.b s = new Setting.b(name, this, value);
        Aurora.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    protected Setting.s registerS(final String name, final String value) {
        final Setting.s s = new Setting.s(name, this, value);
        Aurora.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    protected Setting.mode registerMode(final String name, final List<String> modes, final String value) {
        final Setting.mode s = new Setting.mode(name, this, modes, value);
        Aurora.getInstance().settingsManager.addSetting(s);
        return s;
    }
    
    public String getHudInfo() {
        return "";
    }
    
    public void setup() {
    }
    
    public boolean isDrawn() {
        return this.drawn;
    }
    
    public void setDrawn(final boolean d) {
        this.drawn = d;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String desc) {
        this.description = desc;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public enum Category
    {
        COMBAT, 
        PLAYER, 
        MOVEMENT, 
        MISC, 
        WORLD, 
        RENDER, 
        GUI;
    }
}
