package me.alpha432.oyvey.features.modules.render;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.HeldItemRendererEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;

public class ViewModel extends Module {

    public Setting<Float> positionMainX = this.register(new Setting<>("PositionMainX", 0f, -3.0f, 3f));
    public Setting<Float> positionMainY = this.register(new Setting<>("PositionMainY", 0f, -3.0f, 3f));
    public Setting<Float> positionMainZ = this.register(new Setting<>("PositionMainZ", 0f, -3.0f, 3f));

    public Setting<Float> scaleMain = this.register(new Setting<>("ScaleMain", 1f, 0.1f, 5f));

    public Setting<Float> rotationMainX = this.register(new Setting<>("RotationMainX", 0f, -180.0f, 180f));
    public Setting<Float> rotationMainY = this.register(new Setting<>("RotationMainY", 0f, -180.0f, 180f));
    public Setting<Float> rotationMainZ = this.register(new Setting<>("RotationMainZ", 0f, -180.0f, 180f));

    public Setting<Float> positionOffX = this.register(new Setting<>("PositionOffX", 0f, -3.0f, 3f));
    public Setting<Float> positionOffY = this.register(new Setting<>("PositionOffY", 0f, -3.0f, 3f));
    public Setting<Float> positionOffZ = this.register(new Setting<>("PositionOffZ", 0f, -3.0f, 3f));

    public Setting<Float> scaleOff = this.register(new Setting<>("ScaleOff", 1f, 0.1f, 5f));

    public Setting<Float> rotationOffX = this.register(new Setting<>("RotationOffX", 0f, -180.0f, 180f));
    public Setting<Float> rotationOffY = this.register(new Setting<>("RotationOffY", 0f, -180.0f, 180f));
    public Setting<Float> rotationOffZ = this.register(new Setting<>("RotationOffZ", 0f, -180.0f, 180f));

    public final Setting<Boolean> animateMainX =  this.register(new Setting<>("animateMainX", false));
    public final Setting<Boolean> animateMainY =  this.register(new Setting<>("animateMainY", false));
    public final Setting<Boolean> animateMainZ =  this.register(new Setting<>("animateMainZ", false));
    public final Setting<Float> speedAnimateMain =  this.register(new Setting<>("speedAnimateMain", 1f, 1f, 5f));

    public final Setting<Boolean> animateOffX = this.register(new Setting<>("animateOffX", false));
    public final Setting<Boolean> animateOffY = this.register(new Setting<>("animateOffY", false));
    public final Setting<Boolean> animateOffZ = this.register(new Setting<>("animateOffZ", false));
    public final Setting<Float> speedAnimateOff = this.register(new Setting<>("speedAnimateOff", 1f, 1f, 5f));

    public final Setting<Float> eatX = this.register(new Setting<>("EatX", 1f, -1f, 2f));
    public final Setting<Float> eatY = this.register(new Setting<>("EatY", 1f, -1f, 2f));

    public Setting<Boolean> eatAnimation = this.register(new Setting<>("EatAnimation",true));
    public ViewModel(){
        super("ViewModel","",Category.RENDER,true,false,false);
    }
    private float prevMainX, prevMainY, prevMainZ, prevOffX, prevOffY, prevOffZ;

    private float rotate(float value, float speed) {
        return value - speed <= 180 && value - speed > -180 ? value - speed : 180;
    }
    @Override
    public void onUpdate() {
        prevMainX = rotationMainX.getValue();
        prevMainY = rotationMainY.getValue();
        prevMainZ = rotationMainZ.getValue();
        prevOffX = rotationOffX.getValue();
        prevOffY = rotationOffY.getValue();
        prevOffZ = rotationOffZ.getValue();

        if (animateMainX.getValue())
            rotationMainX.setValue(rotate(rotationMainX.getValue(), speedAnimateMain.getValue()));

        if (animateMainY.getValue())
            rotationMainY.setValue(rotate(rotationMainY.getValue(), speedAnimateMain.getValue()));

        if (animateMainZ.getValue())
            rotationMainZ.setValue(rotate(rotationMainZ.getValue(), speedAnimateMain.getValue()));

        if (animateOffX.getValue())
            rotationOffX.setValue(rotate(rotationOffX.getValue(), speedAnimateOff.getValue()));

        if (animateOffY.getValue())
            rotationOffY.setValue(rotate(rotationOffY.getValue(), speedAnimateOff.getValue()));

        if (animateOffZ.getValue())
            rotationOffZ.setValue(rotate(rotationOffZ.getValue(), speedAnimateOff.getValue()));
    }

    @Subscribe
    private void onHeldItemRender(HeldItemRendererEvent event) {
        if (event.getHand() == Hand.MAIN_HAND) {
            event.getStack().translate(positionMainX.getValue(), positionMainY.getValue(), positionMainZ.getValue());
            event.getStack().scale(scaleMain.getValue(), scaleMain.getValue(), scaleMain.getValue());
            event.getStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(interpolateFloat(prevMainX, rotationMainX.getValue(), getTickDelta())));
            event.getStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(interpolateFloat(prevMainY, rotationMainY.getValue(), getTickDelta())));
            event.getStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(interpolateFloat(prevMainZ, rotationMainZ.getValue(), getTickDelta())));
        } else {
            event.getStack().translate(-positionOffX.getValue(), positionOffY.getValue(), positionOffZ.getValue());
            event.getStack().scale(scaleOff.getValue(), scaleOff.getValue(), scaleOff.getValue());
            event.getStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(interpolateFloat(prevOffX, rotationOffX.getValue(), getTickDelta())));
            event.getStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(interpolateFloat(prevOffY, rotationOffY.getValue(), getTickDelta())));
            event.getStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(interpolateFloat(prevOffZ, rotationOffZ.getValue(), getTickDelta())));
        }
    }
    private float getTickDelta(){
        return mc.getRenderTickCounter().getTickDelta(true);
    }
    public static double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return (float) interpolate(oldValue, newValue, (float) interpolationValue);
    }
}
