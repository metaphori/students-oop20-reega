/**
 *
 */
package reega.io;

/**
 *
 */
public final class IOControllerFactory {

    private IOControllerFactory() {
    }

    /**
     * Get the default {@link IOController}.
     *
     * @return the default {@link IOController}
     */
    public static IOController getDefaultIOController() {
        return IOControllerImpl.getInstance();
    }

    /**
     * Get the default {@link TokenIOController}.
     *
     * @return the default {@link TokenIOController}
     */
    public static TokenIOController getDefaultTokenIOController() {
        return new TokenIOControllerImpl(IOControllerFactory.getDefaultIOController());
    }

    /**
     * Create a {@link TokenIOController} with {@code ioController} as the controller.
     *
     * @param ioController {@link IOController} to use
     * @return a new {@link TokenIOController} that uses {@code ioController} as its controller
     */
    public static TokenIOController createTokenIOController(final IOController ioController) {
        return new TokenIOControllerImpl(ioController);
    }
}
