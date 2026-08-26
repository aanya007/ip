package murphy.task;

/**
 * Represents one task in Murphy's task list.
 */
public class Task {
    /** The text describing the task. */
    protected final String description;

    /** Whether the task has been completed. */
    private boolean isDone;

    /**
     * Creates an incomplete task.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
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

    /**
     * Returns whether this task has been completed.
     *
     * @return {@code true} if this task is completed
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns this task in Murphy's save-file format.
     *
     * @return a pipe-separated representation of this task
     */
    public String toDataString() {
        return "T | " + (isDone() ? "1" : "0") + " | " + escapeDataField(description);
    }

    /** Escapes characters that have a special meaning in Murphy's save-file format. */
    protected static String escapeDataField(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }

    /** Returns the common task text, including its type and completion state. */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }
}
