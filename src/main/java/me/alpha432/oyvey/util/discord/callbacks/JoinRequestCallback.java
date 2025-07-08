package me.alpha432.oyvey.util.discord.callbacks;

import com.sun.jna.Callback;
import me.alpha432.oyvey.util.discord.DiscordUser;

public interface JoinRequestCallback extends Callback {
    void apply(final DiscordUser p0);
}
