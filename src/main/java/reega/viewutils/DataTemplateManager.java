/**
 *
 */
package reega.viewutils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manager for all the {@link DataTemplate} of this application
 *
 * @author Marco
 *
 */
public final class DataTemplateManager {

    private DataTemplateManager() {
    }

    /**
     * Instance of a {@link DataTemplateManager}
     */
    public static DataTemplateManager instance = new DataTemplateManager();

    private final ConcurrentMap<Class<?>, DataTemplate<?>> templates = new ConcurrentHashMap<>();

    /**
     * Add a data template
     *
     * @param template template to add
     */
    public void addTemplate(final DataTemplate<?> template) {
        Objects.requireNonNull(template);
        this.templates.put(template.getDataObjectClass(), template);
    }

    /**
     * Remove a template
     *
     * @param template template to remove
     */
    public void removeTemplate(final DataTemplate<?> template) {
        Objects.requireNonNull(template);
        this.templates.remove(template.getDataObjectClass());
    }

    /**
     * Remove a template based on its {@link DataTemplate#getDataObjectClass()}
     *
     * @param dataObjectClass data object class to remove
     */
    public void removeTemplate(final Class<?> dataObjectClass) {
        Objects.requireNonNull(dataObjectClass);
        this.templates.remove(dataObjectClass);
    }

    /**
     * Get the the template associated with {@code dataObjectClass}
     *
     * @param dataObjectClass data object class to search
     * @return an Optional filled in with a {@link DataTemplate} if it has been found, an empty Optional otherwise
     */
    public Optional<DataTemplate<?>> getTemplate(final Class<?> dataObjectClass) {
        Objects.requireNonNull(dataObjectClass);
        return Optional.ofNullable(this.templates.get(dataObjectClass));
    }
}
