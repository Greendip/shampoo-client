package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.util.Identifier;

public class Capes extends Module {
    private static Capes INSTANCE = new Capes();
    private final Identifier capeTexture;
    public Capes(){
        super("Capes","",Category.CLIENT,true,false,false);
        this.setInstance();
        this.capeTexture = Identifier.of("ive","textures/memes.png");
    }

    public static Capes getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Capes();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
    public Identifier getCapeTexture(){
        return capeTexture;
    }
}
