package reega.io;

import java.util.List;

/**
 * Filter for dialog extensions
 */
public final class DialogExtensionFilter {
    private final String extensionDescription;
    private final List<String> extensions;

    /**
     * Create a new {@link DialogExtensionFilter}
     * @param extensionDescription description of the extension filter such as "All Files", "CSV Files", "JSON Files", etc.
     * @param extensions extensions used by this filter, such as ".*", ".csv", ".json", etc.
     */
    public DialogExtensionFilter(String extensionDescription, List<String> extensions) {
        if (extensions.size() == 0) {
            throw new IllegalArgumentException("You have to set at least one extension");
        }
        this.extensionDescription = extensionDescription;
        this.extensions = extensions;
    }

    /**
     * Return the extension description
     * @return the extension description
     */
    public String getExtensionDescription() {
        return extensionDescription;
    }

    /**
     * Return the extensions
     * @return the extensions
     */
    public List<String> getExtensions() {
        return extensions;
    }
}
