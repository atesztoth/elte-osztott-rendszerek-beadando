package domino.Exception;

/** This exception will be thrown if the server sends a wrong pack of initial dominos.
 * Created by atesztoth on 2017. 04. 16..
 */
public class BadInitialDominoStringException extends Exception {

    public BadInitialDominoStringException() {
    }

    public BadInitialDominoStringException(String message) {
        super(message);
    }
}
