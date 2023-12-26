package app.commandHandle;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Builds the output message within the CommandRunner class.
 * To avoid the need to create a method for each type of result,
 * I used generics.
 * @param <T> the type of result.
 */
public class OutputBuilder<T> {
    private String message;
    private ArrayList<String> results;
    private List<T> result;
    private ObjectMapper objectMapper;
    private CommandInput cmdInput;
    private T stats;
    private ObjectNode objectNode;
    private Boolean swap;
    private String resultFieldName = "result";

    public OutputBuilder(final CommandInput commandInput) {
        objectMapper = new ObjectMapper();
        cmdInput = commandInput;
        this.objectNode = objectMapper.createObjectNode();
        swap = false;
    }

    /**
     * Sets the output message.
     * @param msg the output message
     * @return
     */
    public OutputBuilder<T> withMessage(final String msg) {
        this.message = msg;
        return this;
    }

    /**
     * Sets the output results of type String.
     * @param resultsOut the vector of results
     * @return
     */
    public OutputBuilder<T> withResults(final ArrayList<String> resultsOut) {
        this.results = resultsOut;
        return this;
    }

    /**
     * Sets the output results for any type of object.
     * @param res the vector of results
     * @return
     */
    public OutputBuilder<T> withResult(final List<T> res) {
        this.result = res;
        return this;
    }

    /**
     * Sets the custom field name for the result.
     * @param fieldName the custom field name
     * @return
     */
    public OutputBuilder<T> withResultFieldName(final String fieldName) {
        this.resultFieldName = fieldName;
        return this;
    }

    /**
     * Sets the player's statistics.
     * @param statsOut
     * @return
     */
    public OutputBuilder<T> withStats(final T statsOut) {
        this.stats = statsOut;
        return this;
    }

    /**
     * For the command "printCurrentPage".
     * Note: Even if it's not relevant, it visually confuses me when comparing
     * the ref with my output.
     * @param change
     * @return
     */
    public OutputBuilder<T> withSwap(final Boolean change) {
        this.swap = change;
        return this;
    }

    /**
     * Builds the output message based on previous constructions.
     * @return
     */
    public ObjectNode build() {
        if (!swap) {
            if (cmdInput.getCommand() != null) {
                objectNode.put("command", cmdInput.getCommand());
            }

            if (cmdInput.getUsername() != null) {
                objectNode.put("user", cmdInput.getUsername());
            }
        } else {
            if (cmdInput.getUsername() != null) {
                objectNode.put("user", cmdInput.getUsername());
            }

            if (cmdInput.getCommand() != null) {
                objectNode.put("command", cmdInput.getCommand());
            }
        }

        if (cmdInput.getTimestamp() != 0) {
            objectNode.put("timestamp", cmdInput.getTimestamp());
        }

        if (message != null) {
            objectNode.put("message", message);
        }

        if (result != null) {
            objectNode.put(resultFieldName, objectMapper.valueToTree(result));
        }

        if (results != null) {
            objectNode.put("results", objectMapper.valueToTree(results));
        }

        if (stats != null) {
            objectNode.put("stats", objectMapper.valueToTree(stats));
        }

        return objectNode;
    }
}
