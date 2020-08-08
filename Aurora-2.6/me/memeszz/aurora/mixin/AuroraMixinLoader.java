package me.memeszz.aurora.mixin;

import java.util.Map;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import me.memeszz.aurora.Aurora;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class AuroraMixinLoader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public AuroraMixinLoader() {
        Aurora.log.info("Aurora mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.aurora.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        Aurora.log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    @Nullable
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        AuroraMixinLoader.isObfuscatedEnvironment = data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        AuroraMixinLoader.isObfuscatedEnvironment = false;
    }
}
