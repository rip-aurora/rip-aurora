
package me.memeszz.aurora.module.modules.world;

import java.util.ArrayList;
import me.memeszz.aurora.setting.Setting;
import java.util.List;
import me.memeszz.aurora.module.Module;

public class Spammer extends Module
{
    public static List<String> text;
    int waitCounter;
    Setting.i delay;
    int i;
    
    public Spammer() {
        super("Spammer", Category.WORLD);
        this.i = -1;
        Spammer.text = new ArrayList<String>();
    }
    
    @Override
    public void setup() {
        this.delay = this.registerI("Delay", 5, 1, 100);
    }
    
    @Override
    public void onUpdate() {
        if (Spammer.text.size() <= 0 || Spammer.text.isEmpty()) {
            Spammer.mc.player.sendChatMessage("You are so fucked up look in a mirror retard I didn't dox you and aurora hasn't doxed in 5 months so Idk what your talking about.");
            Spammer.mc.player.sendChatMessage("Thinking your funny for saying you don't have a dead dad is worse then doxxing. so shutup faggot and bad pvper.");
            Spammer.mc.player.sendChatMessage("Did your dad not give you enough attention so you have to cope by saying fucked up shit? you should jump off a bridge sad kid.");
            this.disable();
        }
        if (this.waitCounter < this.delay.getValue() * 100) {
            ++this.waitCounter;
            return;
        }
        this.waitCounter = 0;
        ++this.i;
        if (this.i + 1 <= Spammer.text.size()) {
            Spammer.mc.player.sendChatMessage((String)Spammer.text.get(this.i));
        }
        else {
            this.i = -1;
        }
    }
}
