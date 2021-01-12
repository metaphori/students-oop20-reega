/**
 *
 */
package reega.viewutils;

import java.util.Optional;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Control for handling a variable control in it
 *
 * @author Marco
 *
 */
public class ContentControl extends VBox {

    private ObjectProperty<Object> objProperty;

    /**
     * Get the current content that is bounded to a view
     *
     * @return
     */
    public Object getContent() {
        return this.objectProperty().get();
    }

    /**
     * Set a new content that is bounded to a view
     *
     * @param value new content
     */
    public void setContent(final Object value) {
        this.objectProperty().set(value);
    }

    /**
     * Get the content property
     *
     * @return the content property
     */
    @SuppressWarnings("unchecked")
    public ObjectProperty<Object> objectProperty() {
        if (this.objProperty == null) {
            this.objProperty = new SimpleObjectProperty<>();
            this.objectProperty().addListener((observable, oldValue, newValue) -> {
                // Get the template for the new content
                final Optional<DataTemplate<?>> template = DataTemplateManager.instance
                        .getTemplate(newValue.getClass());
                // Generate the element, or create a VBox if the template manager cannot find a
                // Control factory associated with the class of the new value
                final Parent newElement = template
                        .map(dTemplate -> (Parent) ((DataTemplate<Object>) dTemplate).getControlFactory(newValue).get())
                        .orElse(new VBox());
                // Needed check to create or update the only child of this control
                if (this.getChildren().size() == 0) {
                    this.getChildren().add(newElement);
                } else {
                    this.getChildren().set(0, newElement);
                }
                VBox.setVgrow(this.getChildren().get(0), Priority.ALWAYS);
            });
        }
        return this.objProperty;
    }

}
