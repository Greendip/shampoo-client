package me.alpha432.oyvey.features.modules.render;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;


public class CrystalModel extends Module
{
    private static CrystalModel INSTANCE;


    public Setting<Float> spin = num("Spin",  1.0f, 0.0f, 10.0f);
    public Setting<Float> scale = num("Scale", 1.00f,  0.10f, 1.50f);
    public Setting<Float> bounce = num("Bounce",  1.0f, 0.0f, 10.0f);

    public CrystalModel()
    {
        super("CrystalModel", "Renders the crystal model", Category.RENDER,true,false,false);
        INSTANCE = this;
    }

    public static CrystalModel getInstance()
    {
        return INSTANCE;
    }


}
