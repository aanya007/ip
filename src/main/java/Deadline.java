/** A task that must be completed by a specified date or time. */
public class Deadline extends Task {
    /** The deadline text, kept as entered by the user. */
    private final String by;

    /** Creates an incomplete deadline task. */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone() ? "1" : "0") + " | " + escapeDataField(description)
                + " | " + escapeDataField(by);
    }

    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }
}
