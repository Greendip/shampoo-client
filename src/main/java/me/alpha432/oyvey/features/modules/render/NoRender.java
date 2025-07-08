package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.BlockBreakingInfo;


public class NoRender extends Module {
    public Setting<Boolean> fire = bool("FireOverlay", true);
    public Setting<Boolean> particles = bool("Parcticles", true);
    public Setting<Boolean> armour = bool("Armour", true);
    public Setting<Boolean> blockoverlay = bool("BlockOverlay", true);
    public Setting<Boolean> crystals = bool("Crystals", true);
    public Setting<Boolean> wateroverlay = bool("WaterOverlay", true);
    public Setting<Boolean> portaloverlay = bool("PortalOverlay", true);
    public Setting<Boolean> hurtcamera = bool("HurtCamera", true);
    public Setting<Boolean> liquidOverlay = bool("LiquidOverlay", true);
    public Setting<Boolean> totemanimation = bool("TotemAnimation", true);
    public Setting<Boolean> pumkinOverlay = bool("PumkinOverlay", true);
    public Setting<Boolean> potions = bool("Potions", true);
    public Setting<Boolean> fog = bool("Fog", true);
    public Setting<Boolean> scoarboard = bool("Scoarboard", true);
    public Setting<Boolean> beacon = bool("Beacon", true);
    public Setting<Boolean> bossbar = bool("Bossbar", true);
    public Setting<Boolean> explosion = bool("Explosion", true);
    public Setting<Boolean> eatparticle = bool("EatParticles", true);
    public Setting<Boolean> gui = bool("Gui", true);
    public Setting<Boolean> weather = bool("Weather", true);
    public Setting<Boolean> spawnerEntity = bool("SpawnerEntity", true);
    public Setting<Boolean> hotbaritemname = bool("HotbatItemName", true);


    public NoRender() {
        super("NoRender", "", Category.RENDER, true,false,false);
    }
    @Override
    public void onUpdate() {
     mc.player.removeStatusEffect(StatusEffects.NAUSEA);
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
