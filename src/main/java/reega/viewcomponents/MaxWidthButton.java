package reega.viewcomponents;

import javafx.scene.control.Button;

public class MaxWidthButton extends Button {
    public MaxWidthButton() {
        this.setMaxWidth(Double.MAX_VALUE);
    }

    public MaxWidthButton(String text) {
        super(text);
        this.setMaxWidth(Double.MAX_VALUE);
    }
}
