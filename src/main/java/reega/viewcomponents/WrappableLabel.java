package reega.viewcomponents;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * Label that wraps text correctly.
 */
public class WrappableLabel extends Label {

    public WrappableLabel() {
        this("");
    }

    public WrappableLabel(final String text) {
        super(text);
        /* it should already be set like this but for some reason it works */
        this.setMinHeight(Region.USE_PREF_SIZE);
        this.setPrefHeight(Region.USE_COMPUTED_SIZE);
        this.setWrapText(true);
    }
}
