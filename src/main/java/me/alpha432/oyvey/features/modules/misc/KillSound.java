package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class KillSound extends Module {
    public Setting<Mod> mode = this.register(new Setting<>("Mode", Mod.Thunder));

    public enum Mod {
        Thunder, Xp, Mace
    }

    public KillSound() {
        super("KillSound", "", Category.MISC, true, false, false);
    }

    @Override
    public void onDeath(PlayerEntity player) {
        if (mode.getValue() == Mod.Thunder) player.getEntityWorld().playSound(player, player.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 3F, 2F);
        if(mode.getValue() ==Mod.Xp) player.getEntityWorld().playSound(player, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 3F, 2F);
        if (mode.getValue() == Mod.Mace) player.getEntityWorld().playSound(player, player.getBlockPos(), SoundEvents.ITEM_MACE_SMASH_GROUND, SoundCategory.PLAYERS, 3F, 2F);
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
    
