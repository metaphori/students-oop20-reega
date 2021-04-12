package reega.viewcomponents;

import javafx.scene.control.Label;

public class WrappableLabel extends Label {

    public WrappableLabel() {
        super();
        this.setWrapText(true);
    }

    public WrappableLabel(String text) {
        super(text);
        this.setWrapText(true);
    }
}
