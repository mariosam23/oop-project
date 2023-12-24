package app.commandHandle;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for executing all commands.
 */
@Getter
public final class CommandExecution {
    private final Map<String, CommandType> commandMap = new HashMap<>();

    public CommandExecution() {
        addCommands();
    }

    private void addCommands() {
        for (CommandType command : CommandType.values()) {
            String commandName = enumNameToCamelCase(command.name());
            commandMap.put(commandName, command);
        }
    }

    /**
     * Receives the command from the dictionary and executes it.
     * @param commandInput
     * @return the result of the command
     */
    public ObjectNode executeCommand(final CommandInput commandInput) {
        CommandType command = commandMap.get(commandInput.getCommand());
        if (command != null) {
            return command.execute(commandInput);
        }

        return null;
    }

    private String enumNameToCamelCase(final String str) {
        String[] parts = str.toLowerCase().split("_");
        StringBuilder camelCaseString = new StringBuilder(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(parts[i].substring(0, 1).toUpperCase())
                    .append(parts[i].substring(1));
        }

        return camelCaseString.toString();
    }
}
