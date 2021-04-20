/**
 *
 */
package reega.viewcomponents;

import javafx.scene.layout.VBox;

/**
 * Card component.
 */
public class Card extends VBox {

    public Card() {
        this.getStylesheets().add("css/Card.css");
        this.getStyleClass().add("card");
    }

}
