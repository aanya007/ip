/**
 * Represents an input error that Murphy can explain to the user.
 */
public class MurphyException extends Exception {
    /** Creates an input error with the message Murphy should display. */
    public MurphyException(String message) {
        super(message);
    }
}
