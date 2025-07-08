package me.alpha432.oyvey.util;

import net.minecraft.util.StringIdentifiable;

public class ChatUtil {
    public static StringIdentifiable getClient() {
        return FormattingUtils.getFormatting("client");
    }

    public static StringIdentifiable getRainbow() {
        return FormattingUtils.getFormatting("rainbow");
    }
}