package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.gui.Component;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.gui.items.Item;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.client.Sounds;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class Button extends Item {
    private boolean state;
    private Sounds sounds = OyVey.moduleManager.getModuleByClass(Sounds.class);

    public Button(String name) {
        super(name);
        this.height = ClickGui.getInstance().getButtonHeight();
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        //RenderUtil.rect(context.getMatrices(), this.x, this.y, this.x + (float) this.width, this.y + (float) this.height - 0.8f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).color.getValue().getAlpha()) : OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        drawString(this.getName(), this.x + 2.3f, this.y - 2.0f - (float) OyVeyGui.getClickGui().getTextOffset(), this.getState() ? OyVey.colorManager.getColorWithAlpha(255) : -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        if (sounds.isEnabled() && sounds.en.getValue() && state == true){
            SoundEvent soundEvent = SoundEvent.of(Identifier.of("ive","enable_sound"));
            mc.player.playSound(soundEvent,1f,1f);
        }

        if (sounds.isEnabled() && sounds.di.getValue() && state == false){
            SoundEvent soundEvent = SoundEvent.of(Identifier.of("ive","disable_sound"));
            mc.player.playSound(soundEvent,1f,1f);
        }

        else {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
        }
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return ClickGui.getInstance().getButtonHeight() + 2;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : OyVeyGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }
}