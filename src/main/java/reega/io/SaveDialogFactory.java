package reega.io;

public final class SaveDialogFactory {
    private SaveDialogFactory(){}

    public static SaveDialog getDefaultSaveDialog() {
        return new JavaFXSaveDialog();
    }
}
