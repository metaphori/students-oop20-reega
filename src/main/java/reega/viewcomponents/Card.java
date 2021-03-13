/**
 *
 */
package reega.viewcomponents;

import javafx.scene.layout.VBox;

/**
 * @author Marco
 *
 */
public class Card extends VBox {

    public Card() {
        this.getStylesheets().add("css/Card.css");
        this.getStyleClass().add("card");
    }

}
