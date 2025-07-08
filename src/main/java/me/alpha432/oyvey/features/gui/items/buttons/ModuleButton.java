package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.gui.Component;
import me.alpha432.oyvey.features.gui.items.Item;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.client.Sounds;
import me.alpha432.oyvey.features.settings.Bind;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.Animation;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton
        extends Button {
    private final Module module;
    private List<Item> items = new ArrayList<>();
    private boolean subOpen;
    private Sounds sounds = OyVey.moduleManager.getModuleByClass(Sounds.class);
    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;
        this.initSettings();
    }

    public void initSettings() {
        ArrayList<Item> newItems = new ArrayList<>();
        if (!this.module.getSettings().isEmpty()) {
            for (Setting<?> setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton((Setting<Boolean>) setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton((Setting<Bind>) setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    newItems.add(new StringButton((Setting<String>) setting));
                }
                if (setting.getValue() instanceof Color) {
                    newItems.add(new ColorPicker(setting));
                }
                if (setting.isNumberSetting() && setting.hasRestriction()) {
                    newItems.add(new Slider((Setting<Number>) setting));
                    continue;
                }
                if (!setting.isEnumSetting()) continue;
                newItems.add(new EnumButton((Setting<Enum<?>>) setting));
            }
        }
        newItems.add(new BindButton((Setting<Bind>) this.module.getSettingByName("Keybind")));
        this.items = newItems;
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(context, mouseX, mouseY, partialTicks);
        if (ClickGui.getInstance().gear.getValue()) drawString(this.subOpen ? "-" : "+" , this.x - 1.0f + (float) this.width - 8.0f, this.y + 4.0f,-1);
        if (!this.items.isEmpty()) {
            if (this.subOpen) {
                float height = 1.0f;
                for (Item item : this.items) {
                    Component.counter1[0] = Component.counter1[0] + 1;
                    if (!item.isHidden()) {
                        item.setLocation(this.x + 1.0f, this.y + (height += ClickGui.getInstance().getButtonHeight()));
                        item.setHeight(ClickGui.getInstance().getButtonHeight());
                        item.setWidth(this.width - 9);
                        item.drawScreen(context, mouseX, mouseY, partialTicks);
                        if (item instanceof ColorPicker && ((ColorPicker) item).setting.open) {
                            height += 110.0f;
                        }
                        if (item instanceof EnumButton && ((EnumButton) item).setting.open) {
                            height += ((EnumButton) item).setting.getValue().getClass().getEnumConstants().length * 12;
                        }
                    }
                    item.update();
                }
            }
        }
        if (ClickGui.getInstance().desk.getValue() && isHovering(mouseX,mouseY)){
            String description = Formatting.GRAY + module.getDescription();

            RenderUtil.rect(context.getMatrices(),0, mc.currentScreen.height - 11, mc.textRenderer.getWidth(description) + 2, mc.currentScreen.height, new Color(-1072689136).getRGB());

            assert mc.currentScreen != null;
            drawString(description, 2, mc.currentScreen.height - 10, -1);
        }
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                if (sounds.op.getValue()){
                    SoundEvent soundEvent = SoundEvent.of(Identifier.of("ive","open_sound"));
                    mc.player.playSound(soundEvent,1,1);
                }
                else {
                    mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
                }
            }
            if (this.subOpen) {
                for (Item item : this.items) {
                    if (item.isHidden()) continue;
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override public void onKeyPressed(int key) {
        super.onKeyPressed(key);
        if (!this.items.isEmpty() && this.subOpen) {
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                item.onKeyPressed(key);
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = ClickGui.getInstance().getButtonHeight() - 1;
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                height += item.getHeight() + 1;
                if (item instanceof ColorPicker colorButton && colorButton.setting.open) {
                    height += 110;
                }
                if (item instanceof EnumButton && ((EnumButton)item).setting.open) {
                    height += ((EnumButton) item).setting.getValue().getClass().getEnumConstants().length * 12;
                }
            }
            return height + 2;
        }
        return ClickGui.getInstance().getButtonHeight() - 1;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}