
package me.memeszz.aurora.util;

import me.memeszz.aurora.setting.Setting;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.memeszz.aurora.event.EventProcessor;
import me.memeszz.aurora.module.modules.world.AutoGG;
import me.memeszz.aurora.command.Command;
import me.memeszz.aurora.util.enemy.Enemy;
import me.memeszz.aurora.util.enemy.Enemies;
import me.memeszz.aurora.util.friends.Friend;
import me.memeszz.aurora.util.friends.Friends;
import me.memeszz.aurora.module.modules.world.Spammer;
import me.memeszz.aurora.module.modules.world.Announcer;
import me.memeszz.aurora.util.macro.Macro;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import me.memeszz.aurora.module.Module;
import me.memeszz.aurora.module.ModuleManager;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import me.memeszz.aurora.Aurora;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import net.minecraft.client.Minecraft;

public class ConfigUtils
{
    Minecraft mc;
    public File Aurora;
    public File Settings;
    
    public ConfigUtils() {
        this.mc = Minecraft.getMinecraft();
        this.Aurora = new File(this.mc.gameDir + File.separator + "Aurora");
        if (!this.Aurora.exists()) {
            this.Aurora.mkdirs();
        }
        this.Settings = new File(this.mc.gameDir + File.separator + "Aurora" + File.separator + "Settings");
        if (!this.Settings.exists()) {
            this.Settings.mkdirs();
        }
        this.loadMods();
        this.loadDrawn();
        this.loadSettingsList();
        this.loadBinds();
        this.loadFriends();
        this.loadPrefix();
        this.loadRainbow();
        this.loadMacros();
        this.loadMsgs();
        this.loadAutoGG();
        this.loadSpammer();
        this.loadAnnouncer();
        this.loadEnemies();
        this.loadClientname();
    }
    
    public void saveClientname() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "ClientName.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(me.memeszz.aurora.Aurora.MODNAME);
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadClientname() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "ClientName.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                me.memeszz.aurora.Aurora.MODNAME = line;
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    
    public void saveBinds() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Binds.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : ModuleManager.getModules()) {
                out.write(module.getName() + ":" + Keyboard.getKeyName(module.getBind()));
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadBinds() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Binds.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String bind = curLine.split(":")[1];
                for (final Module m : ModuleManager.getModules()) {
                    if (m != null && m.getName().equalsIgnoreCase(name)) {
                        m.setBind(Keyboard.getKeyIndex(bind));
                    }
                }
            }
            br.close();
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
    }
    
    public void saveMacros() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Macros.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Macro m : me.memeszz.aurora.Aurora.getInstance().macroManager.getMacros()) {
                out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue().replace(" ", "_"));
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadMacros() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Macros.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String bind = curLine.split(":")[0];
                final String value = curLine.split(":")[1];
                me.memeszz.aurora.Aurora.getInstance().macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value.replace("_", " ")));
            }
            br.close();
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
    }
    
    public void saveAnnouncer() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Announcer.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write("Walk:" + Announcer.walkMessage);
            out.write("\r\n");
            out.write("Place:" + Announcer.placeMessage);
            out.write("\r\n");
            out.write("Jump:" + Announcer.jumpMessage);
            out.write("\r\n");
            out.write("Break:" + Announcer.breakMessage);
            out.write("\r\n");
            out.write("Attack:" + Announcer.attackMessage);
            out.write("\r\n");
            out.write("Eat:" + Announcer.eatMessage);
            out.write("\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadAnnouncer() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Announcer.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String message = curLine.split(":")[1];
                if (name.equalsIgnoreCase("Walk")) {
                    Announcer.walkMessage = message;
                }
                if (name.equalsIgnoreCase("Place")) {
                    Announcer.placeMessage = message;
                }
                if (name.equalsIgnoreCase("Jump")) {
                    Announcer.jumpMessage = message;
                }
                if (name.equalsIgnoreCase("Break")) {
                    Announcer.breakMessage = message;
                }
                if (name.equalsIgnoreCase("Attack")) {
                    Announcer.attackMessage = message;
                }
                if (name.equalsIgnoreCase("Eat")) {
                    Announcer.eatMessage = message;
                }
            }
            br.close();
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
    }
    
    public void saveSpammer() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Spammer.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final String s : Spammer.text) {
                out.write(s);
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadSpammer() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Spammer.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                Spammer.text.add(line);
            }
            br.close();
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
    }
    
    public void saveMods() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "EnabledModules.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : ModuleManager.getModules()) {
                if (module.isEnabled()) {
                    out.write(module.getName());
                    out.write("\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void saveFriends() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Friends.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Friend f : Friends.getFriends()) {
                out.write(f.getName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadFriends() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Friends.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Friends.friends.clear();
            String line;
            while ((line = br.readLine()) != null) {
                me.memeszz.aurora.Aurora.getInstance().friends.addFriend(line);
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    
    public void saveEnemies() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Enemies.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Enemy e : Enemies.getEnemies()) {
                out.write(e.getName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadEnemies() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Enemies.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Enemies.enemies.clear();
            String line;
            while ((line = br.readLine()) != null) {
                Enemies.addEnemy(line);
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    
    public void savePrefix() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "CommandPrefix.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Command.getPrefix());
            out.write("\r\n");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadPrefix() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "CommandPrefix.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                Command.setPrefix(line);
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    
    public void saveAutoGG() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "AutoGgMessage.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final String s : AutoGG.getAutoGgMessages()) {
                out.write(s);
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadAutoGG() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "AutoGgMessage.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                AutoGG.addAutoGgMessage(line);
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    
    public void saveRainbow() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "RainbowSpeed.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(EventProcessor.INSTANCE.getRainbowSpeed() + "");
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadRainbow() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "RainbowSpeed.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                EventProcessor.INSTANCE.setRainbowSpeed(Integer.parseInt(line));
            }
            br.close();
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    
    public void saveMsgs() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "ClientMessages.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Command.MsgWaterMark + "");
            out.write(",");
            out.write(Command.cf.getName());
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadMsgs() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "ClientMessages.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String watermark = curLine.split(",")[0];
                final String color = curLine.split(",")[1];
                final boolean w = Boolean.parseBoolean(watermark);
                final ChatFormatting c = Command.cf = ChatFormatting.getByName(color);
                Command.MsgWaterMark = w;
            }
            br.close();
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
    }
    
    public void saveDrawn() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Drawn.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Module module : ModuleManager.getModules()) {
                out.write(module.getName() + ":" + module.isDrawn());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadDrawn() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "Drawn.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String isOn = curLine.split(":")[1];
                final boolean drawn = Boolean.parseBoolean(isOn);
                for (final Module m : ModuleManager.getModules()) {
                    if (m.getName().equalsIgnoreCase(name)) {
                        m.setDrawn(drawn);
                    }
                }
            }
            br.close();
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
    }
    
    public void writeCrash(final String alah) {
        try {
            final SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
            final Date date = new Date();
            final File file = new File(this.Aurora.getAbsolutePath(), "crashlog-".concat(format.format(date)).concat(".bruh"));
            final BufferedWriter outWrite = new BufferedWriter(new FileWriter(file));
            outWrite.write(alah);
            outWrite.close();
        }
        catch (Exception ex) {}
    }
    
    public void loadMods() {
        try {
            final File file = new File(this.Aurora.getAbsolutePath(), "EnabledModules.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                for (final Module m : ModuleManager.getModules()) {
                    if (m.getName().equals(line)) {
                        m.enable();
                    }
                }
            }
            br.close();
        }
        catch (Exception var7) {
            var7.printStackTrace();
        }
    }
    
    public void saveSettingsList() {
        try {
            final File file = new File(this.Settings.getAbsolutePath(), "Number.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Setting i : me.memeszz.aurora.Aurora.getInstance().settingsManager.getSettings()) {
                if (i.getType() == Setting.Type.DOUBLE) {
                    out.write(i.getName() + ":" + ((Setting.d)i).getValue() + ":" + i.getParent().getName() + "\r\n");
                }
                if (i.getType() == Setting.Type.INT) {
                    out.write(i.getName() + ":" + ((Setting.i)i).getValue() + ":" + i.getParent().getName() + "\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex) {}
        try {
            final File file = new File(this.Settings.getAbsolutePath(), "Boolean.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Setting i : me.memeszz.aurora.Aurora.getInstance().settingsManager.getSettings()) {
                if (i.getType() == Setting.Type.BOOLEAN) {
                    out.write(i.getName() + ":" + ((Setting.b)i).getValue() + ":" + i.getParent().getName() + "\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex2) {}
        try {
            final File file = new File(this.Settings.getAbsolutePath(), "String.txt");
            final BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (final Setting i : me.memeszz.aurora.Aurora.getInstance().settingsManager.getSettings()) {
                if (i.getType() == Setting.Type.MODE) {
                    out.write(i.getName() + ":" + ((Setting.mode)i).getValue() + ":" + i.getParent().getName() + "\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex3) {}
    }
    
    public void loadSettingsList() {
        try {
            final File file = new File(this.Settings.getAbsolutePath(), "Number.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String isOn = curLine.split(":")[1];
                final String m = curLine.split(":")[2];
                for (final Module mm : ModuleManager.getModules()) {
                    if (mm != null && mm.getName().equalsIgnoreCase(m)) {
                        final Setting mod = me.memeszz.aurora.Aurora.getInstance().settingsManager.getSettingByNameAndMod(name, mm);
                        if (mod instanceof Setting.i) {
                            ((Setting.i)mod).setValue(Integer.parseInt(isOn));
                        }
                        else {
                            if (!(mod instanceof Setting.d)) {
                                continue;
                            }
                            ((Setting.d)mod).setValue(Double.parseDouble(isOn));
                        }
                    }
                }
            }
            br.close();
        }
        catch (Exception var13) {
            var13.printStackTrace();
        }
        try {
            final File file = new File(this.Settings.getAbsolutePath(), "Boolean.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String isOn = curLine.split(":")[1];
                final String m = curLine.split(":")[2];
                for (final Module mm : ModuleManager.getModules()) {
                    if (mm != null && mm.getName().equalsIgnoreCase(m)) {
                        final Setting mod = me.memeszz.aurora.Aurora.getInstance().settingsManager.getSettingByNameAndMod(name, mm);
                        ((Setting.b)mod).setValue(Boolean.parseBoolean(isOn));
                    }
                }
            }
            br.close();
        }
        catch (Exception var14) {
            var14.printStackTrace();
        }
        try {
            final File file = new File(this.Settings.getAbsolutePath(), "String.txt");
            final FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                final String isOn = curLine.split(":")[1];
                final String m = curLine.split(":")[2];
                for (final Module mm : ModuleManager.getModules()) {
                    if (mm != null && mm.getName().equalsIgnoreCase(m)) {
                        final Setting mod = me.memeszz.aurora.Aurora.getInstance().settingsManager.getSettingByNameAndMod(name, mm);
                        ((Setting.mode)mod).setValue(isOn);
                    }
                }
            }
            br.close();
        }
        catch (Exception var15) {
            var15.printStackTrace();
        }
    }
}
