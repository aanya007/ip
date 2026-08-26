package murphy;

/** Generates friendly replies for messages received by the JavaFX client. */
public class Duke {
    /** Returns a concise response to a user's message. */
    public String getResponse(String input) {
        if (input.isBlank()) {
            return "A blank message? Even Murphy needs a little input to work with.";
        }
        return "Murphy heard: " + input;
    }
}
