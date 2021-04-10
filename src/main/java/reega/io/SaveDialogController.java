package reega.io;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Controller for saving a file
 */
public interface SaveDialogController {
    /**
     * Open a save dialog to save a file and return an {@link Optional} filled in with a {@link File} if it has been chosen, otherwise return an empty {@link Optional}
     * @param extensions list of {@link DialogExtensionFilter} to use as filters
     * @return a filled in {@link Optional} if a {@link File} has been chosen, otherwise an empty {@link Optional}
     */
    Optional<File> openSaveDialog(DialogExtensionFilter... extensions);

    /**
     * Open a save dialog with one {@link DialogExtensionFilter}
     * @see #openSaveDialog(DialogExtensionFilter...)
     * @param extensionDescription extension description of the {@link DialogExtensionFilter}
     * @param extensions extensions of the {@link DialogExtensionFilter}
     * @return a filled in {@link Optional} if a {@link File} has been chosen, otherwise an empty {@link Optional}
     */
    default Optional<File> openSaveDialog(String extensionDescription, String... extensions) {
        return this.openSaveDialog(new DialogExtensionFilter(extensionDescription, List.of(extensions)));
    }

}
