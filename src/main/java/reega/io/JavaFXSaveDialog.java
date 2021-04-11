package reega.io;

import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaFXSaveDialog implements SaveDialog {
    @Override
    public Optional<File> openSaveDialog(DialogExtensionFilter... extensions) {
        if (extensions.length == 0) {
            throw new IllegalArgumentException("You need to set at least one extension");
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data File");
        fileChooser.getExtensionFilters().addAll(Arrays.stream(extensions).map(ext -> new FileChooser.ExtensionFilter(ext.getExtensionDescription(), ext.getExtensions())).collect(Collectors.toList()));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("exported");
        File selectedFile = fileChooser.showSaveDialog(null);
        return Optional.ofNullable(selectedFile);
    }
}
