package me.memeszz.aurora.module.modules.render;

import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;
import org.lwjgl.input.Keyboard;
import me.memeszz.aurora.event.events.UpdateEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import me.memeszz.aurora.module.Module;

public class ShulkerPreview extends Module
{
    public static boolean pinned;
    public static int drawX;
    public static int drawY;
    public static NBTTagCompound nbt;
    public static ItemStack itemStack;
    public static boolean active;
    public static int mouseX;
    public static int mouseY;
    public static int guiLeft;
    public static int guiTop;
    
    public ShulkerPreview() {
        super("ShulkerPreview", Category.RENDER, "Show shulker contents when you hover over them");
    }
    
    @Listener
    public void onUpdate(final UpdateEvent event) {
        if (!Keyboard.isKeyDown(42)) {
            ShulkerPreview.pinned = false;
        }
    }
    
    static {
        ShulkerPreview.pinned = false;
        ShulkerPreview.drawX = 0;
        ShulkerPreview.drawY = 0;
        ShulkerPreview.mouseX = 0;
        ShulkerPreview.mouseY = 0;
        ShulkerPreview.guiLeft = 0;
        ShulkerPreview.guiTop = 0;
    }
}
