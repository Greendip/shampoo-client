package me.alpha432.oyvey.util.discord;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;
import me.alpha432.oyvey.util.discord.callbacks.*;

public class DiscordEventHandlers extends Structure {
    public DisconnectedCallback disconnected;
    public JoinRequestCallback joinRequest;
    public SpectateGameCallback spectateGame;
    public ReadyCallback ready;
    public ErroredCallback errored;
    public JoinGameCallback joinGame;
    
    protected List<String> getFieldOrder() {
        return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
    }
    
    public static class Builder {
        private final DiscordEventHandlers handlers;
        
        public Builder() {
            this.handlers = new DiscordEventHandlers();
        }
        
        public DiscordEventHandlers build() {
            return this.handlers;
        }
        
        public Builder disconnected(final DisconnectedCallback disconnected) {
            this.handlers.disconnected = disconnected;
            return this;
        }
        
        public Builder errored(final ErroredCallback errored) {
            this.handlers.errored = errored;
            return this;
        }
        
        public Builder ready(final ReadyCallback ready) {
            this.handlers.ready = ready;
            return this;
        }
        
        public Builder joinRequest(final JoinRequestCallback joinRequest) {
            this.handlers.joinRequest = joinRequest;
            return this;
        }
        
        public Builder joinGame(final JoinGameCallback joinGame) {
            this.handlers.joinGame = joinGame;
            return this;
        }
        
        public Builder spectateGame(final SpectateGameCallback spectateGame) {
            this.handlers.spectateGame = spectateGame;
            return this;
        }
    }
}
