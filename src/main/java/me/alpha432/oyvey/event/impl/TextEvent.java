package me.alpha432.oyvey.event.impl;

import me.alpha432.oyvey.event.Event;

public class TextEvent extends Event {
    private String text;
    public TextEvent(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
