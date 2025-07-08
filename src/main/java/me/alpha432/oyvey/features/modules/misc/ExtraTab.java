package me.alpha432.oyvey.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.PlayerListNameEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ExtraTab extends Module {
    public Setting<Boolean> self = bool("Self", false);
    public Setting<Boolean> friends = bool("Friends", false);
    public ExtraTab() {
        super("ExtraTab", "", Category.MISC, true, false, false);
    }
    @Subscribe
    public void onPlayerListName(PlayerListNameEvent event)
    {
        String[] names = event.getPlayerName().getString().split(" ");
        if (self.getValue())
        {
            for (String s : names)
            {
                String name1 = stripControlCodes(s);
                if (name1.equals(mc.getGameProfile().getName()))
                {
                    event.cancel();
                    event.setPlayerName(Text.of((Formatting.RED + event.getPlayerName().getString())));
                    return;
                }
            }
        }
        if (friends.getValue() )
        {
            for (String s : names)
            {
                String name1 = stripControlCodes(s);
                if (OyVey.friendManager.isFriend(name1))
                {
                    event.cancel();
                    event.setPlayerName(Text.of(Formatting.AQUA + event.getPlayerName().getString()));
                    break;
                }
            }
        }
    }
    private String stripControlCodes(String string)
    {
        StringBuilder builder = new StringBuilder();
        boolean skip = false;
        for (char c : string.toCharArray())
        {
            if (c == Formatting.FORMATTING_CODE_PREFIX)
            {
                skip = true;
                continue;
            }
            if (skip)
            {
                skip = false;
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
    }

}