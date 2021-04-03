package reega.viewutils;

import javafx.scene.Node;

public final class ViewUtils {
    private ViewUtils(){}

    public static <T extends Node> T wrapNodeWithStyleClasses(T node, String... classes) {
        node.getStyleClass().addAll(classes);
        return node;
    }
}
