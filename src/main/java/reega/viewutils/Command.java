package reega.viewutils;

/**
 * Command used to execute commands on the view
 */
public interface Command {
    /**
     * Execute the command
     * @param args args to use
     */
    void execute(Object... args);

    /**
     * Get the command name
     * @return the name of this command
     */
    String getCommandName();
}
