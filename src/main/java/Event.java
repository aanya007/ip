/** A task with a specified start and end date or time. */
public class Event extends Task {
    /** The event start and end text, kept as entered by the user. */
    private final String from;
    private final String to;

    /** Creates an incomplete event task. */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + from + " to: " + to + ")";
    }
}
