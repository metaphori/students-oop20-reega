package reega.viewutils;

import java.util.function.Consumer;

/**
 * Simple implementation of a {@link Command}.
 */
public class LabeledCommand implements Command {

    private final String commandName;
    private final Consumer<Object[]> executorFunction;

    public LabeledCommand(final String commandName, final Consumer<Object[]> executorFunction) {
        this.commandName = commandName;
        this.executorFunction = executorFunction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Object... args) {
        this.executorFunction.accept(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCommandName() {
        return this.commandName;
    }
}
