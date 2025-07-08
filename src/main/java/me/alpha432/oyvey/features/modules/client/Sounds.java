package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.models.Timer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Sounds extends Module {
    private Setting<Mode> deathMode = mode("DeathSound",Mode.Bell);
    private Setting<Mode> logMode = mode("LogoutSound",Mode.NeverLose);
    private Setting<Mode> loginMode = mode("LoginSound",Mode.None);
    public Setting<Boolean> en = bool("EnableSound",true);
    public Setting<Boolean> op = bool("OpenSound",true);
    public Setting<Boolean> di = bool("DisableSound",true);
    private enum Mode {
        Bell,
        Stalker,
        CS,
        NeverLose,
        None
    }

    private Timer timer = new Timer();
    public Sounds(){
        super("Sounds","Sounds on events",Category.CLIENT,true,false,false);
        setToggled(true);
    }

    private void setToggled(boolean b) {

    }

    @Override
    public void onDeath(PlayerEntity player){
        if (player == null || player == mc.player || player.getHealth() > 0.0f || mc.player.isDead() || nullCheck() || fullNullCheck()) return;
        if (timer.passedMs(1500L)) {
            if (deathMode.getValue() != Mode.None) {
                SoundEvent sound = getDeathSound();
                if (sound != null) {
                    mc.player.playSound(sound, 1.0f, 1.0f);
                }
            }
            timer.reset();
        }
    }

    @Override
    public void onLogin(){
        if (fullNullCheck()) return;
        if (loginMode.getValue() != Mode.None){
            SoundEvent soundEvent = getLoginSound();
            if (soundEvent != null){
                mc.player.playSound(soundEvent,1f,1f);
            }
        }
    }

    @Override
    public void onLogout() {
        if (fullNullCheck()) return;
        if (logMode.getValue() != Mode.None){
            SoundEvent soundEvent = getLogoutSound();
            if (soundEvent != null){
                mc.player.playSound(soundEvent,1f,1f);
            }
        }
    }

    private SoundEvent getDeathSound() {
        switch (deathMode.getValue()) {
            case CS: return SoundEvent.of(Identifier.of("ive", "kill_sound_cs"));
            case NeverLose: return SoundEvent.of(Identifier.of("ive", "kill_sound_nl"));
            case Bell: return SoundEvent.of(Identifier.of("ive","bell_sound"));
            case Stalker: return SoundEvent.of(Identifier.of("ive","kill_sound"));
            default: return null;
        }
    }
    private SoundEvent getLoginSound(){
        switch (loginMode.getValue()){
            case CS: return SoundEvent.of(Identifier.of("ive", "kill_sound_cs"));
            case NeverLose: return SoundEvent.of(Identifier.of("ive", "kill_sound_nl"));
            case Bell: return SoundEvent.of(Identifier.of("ive","bell_sound"));
            case Stalker: return SoundEvent.of(Identifier.of("ive","kill_sound"));
            default: return null;
        }
    }
    private SoundEvent getLogoutSound(){
        switch (logMode.getValue()){
            case CS: return SoundEvent.of(Identifier.of("ive", "kill_sound_cs"));
            case NeverLose: return SoundEvent.of(Identifier.of("ive", "kill_sound_nl"));
            case Bell: return SoundEvent.of(Identifier.of("ive","bell_sound"));
            case Stalker: return SoundEvent.of(Identifier.of("ive","kill_sound"));
            default: return null;
        }
    }
}
