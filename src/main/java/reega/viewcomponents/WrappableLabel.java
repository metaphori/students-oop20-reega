package reega.viewcomponents;

import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class WrappableLabel extends Label {

    public WrappableLabel() {
        this("");
    }

    public WrappableLabel(String text) {
        super(text);
        /* it should already be set like this but for some reason it works */
        this.setMinHeight(USE_PREF_SIZE);
        this.setPrefHeight(USE_COMPUTED_SIZE);
        this.setWrapText(true);
    }
}
