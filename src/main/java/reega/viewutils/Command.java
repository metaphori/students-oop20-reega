package reega.viewutils;

/**
 * Command used to execute commands on the view
 */
@FunctionalInterface
public interface Command {
    /**
     * Execute the command
     * @param args args to use
     */
    void execute(Object... args);
}
