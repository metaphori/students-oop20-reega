package reega.viewutils;

import java.util.function.Consumer;

public class LabeledCommand implements Command{

    private String commandName;
    private Consumer<Object[]> executorFunction;

    public LabeledCommand(final String commandName, Consumer<Object[]> executorFunction) {
        this.commandName = commandName;
        this.executorFunction = executorFunction;
    }

    @Override
    public void execute(Object... args) {
        this.executorFunction.accept(args);
    }

    @Override
    public String getCommandName() {
        return this.commandName;
    }
}
