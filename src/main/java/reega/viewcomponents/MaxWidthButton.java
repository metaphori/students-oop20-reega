package reega.viewcomponents;

import javafx.scene.control.Button;

/**
 * Button that fits all the panes correctly.
 */
public class MaxWidthButton extends Button {
    public MaxWidthButton() {
        this.setMaxWidth(Double.MAX_VALUE);
    }

    public MaxWidthButton(final String text) {
        super(text);
        this.setMaxWidth(Double.MAX_VALUE);
    }
}
