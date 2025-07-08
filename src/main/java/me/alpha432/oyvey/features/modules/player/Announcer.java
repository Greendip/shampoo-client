package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.models.Timer;

import java.text.DecimalFormat;
import java.util.Random;

public class Announcer extends Module {
    public Setting<Boolean> move = this.register(new Setting<>("Move", true));
    public Setting<Double> delay = this.register(new Setting<>("Delay", 10d, 1d, 30d));
    private double lastPositionX;
    private double lastPositionY;
    private double lastPositionZ;
    private int eaten;
    private int broken;
    private final Timer delayTimer = new Timer();
    public Announcer() {
        super("Announcer", "", Category.MISC, true, false, false);
    }
    @Override
    public void onEnable() {
        eaten = 0;
        broken = 0;
        delayTimer.reset();
    }

    @Override
    public void onUpdate() {
        double traveledX = lastPositionX - mc.player.lastRenderX;
        double traveledY = lastPositionY - mc.player.lastRenderY;
        double traveledZ = lastPositionZ - mc.player.lastRenderZ;

        double traveledDistance = Math.sqrt(traveledX * traveledX + traveledY * traveledY + traveledZ * traveledZ);

        if (move.getValue()
                && traveledDistance >= 1
                && traveledDistance <= 1000
                && delayTimer.passedS(delay.getValue())) {

            mc.player.networkHandler.sendChatMessage(getWalkMessage().replace("{blocks}", new DecimalFormat("0.00").format(traveledDistance)));

            lastPositionX = mc.player.lastRenderX;
            lastPositionY = mc.player.lastRenderY;
            lastPositionZ = mc.player.lastRenderZ;

            delayTimer.reset();
        }
    }
    private String getWalkMessage() {
        String[] walkMessage = {
                "I just flew over {blocks} blocks thanks to ShampooClient!",
                "Я только что пролетел над {blocks} блоками с помощью register!",
                "ShampooClient sayesinde {blocks} blok u\u00E7tum!",
                "\u6211\u521A\u521A\u7528 ShampooClient \u8D70\u4E86 {blocks} \u7C73!",
                "Dank ShampooClient bin ich gerade über {blocks} Blöcke geflogen!",
                "Jag hoppade precis över {blocks} blocks tack vare ShampooClient!",
                "Właśnie przeleciałem ponad {blocks} bloki dzięki ShampooClient!",
                "Es tikko nolidoju {blocks} blokus, paldies ShampooClient!",
                "Я щойно пролетів над {blocks} блоками завдяки ShampooClient!",
                "I just fwew ovew {blocks} bwoccs thanks to ShampooClient",
                "Ho appena camminato per {blocks} blocchi grazie a ShampooClient!",
                "עכשיו עפתי {blocks} הודות ל ShampooClient!",
                "Právě jsem proletěl {blocks} bloků díky ShampooClient!"
        };
        return walkMessage[new Random().nextInt(walkMessage.length)];
    }
}