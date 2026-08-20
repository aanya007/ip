/**
 * Represents one task in Murphy's task list.
 */
public class Task {
    /** The categories of task Murphy can store. */
    public enum Type {
        TODO("T"), DEADLINE("D"), EVENT("E");

        private final String symbol;

        Type(String symbol) {
            this.symbol = symbol;
        }

        /** @return the short symbol used in the task list */
        public String getSymbol() {
            return symbol;
        }
    }

    /** The text describing the task. */
    private final String description;

    /** The task category. */
    private final Type type;

    /** The optional deadline, or event start and end text. */
    private final String from;
    private final String to;

    /** Whether the task has been completed. */
    private boolean isDone;

    /**
     * Creates an incomplete task.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this(Type.TODO, description, null, null);
    }

    /** Creates a task with its category and optional date/time details. */
    public Task(Type type, String description, String from, String to) {
        this.type = type;
        this.description = description;
        this.from = from;
        this.to = to;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the icon used when displaying this task.
     *
     * @return {@code X} for a completed task, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /** @return the task category */
    public Type getType() {
        return type;
    }

    /** @return a display-ready description including date/time details */
    public String getDisplayDescription() {
        if (type == Type.DEADLINE) {
            return description + " (by: " + to + ")";
        }
        if (type == Type.EVENT) {
            return description + " (from: " + from + " to: " + to + ")";
        }
        return description;
    }
}
