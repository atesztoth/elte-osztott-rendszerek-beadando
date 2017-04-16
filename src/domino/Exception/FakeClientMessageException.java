package domino.Exception;

/**
 * Get thrown whenever a client gives an unusual response.
 * Created by Attila on 2017. 04. 16..
 */
public class FakeClientMessageException extends Exception {
    public FakeClientMessageException(String message) {
        super(message);
    }

    public FakeClientMessageException() {
        super();
    }
}
