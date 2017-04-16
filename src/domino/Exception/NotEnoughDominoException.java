package domino.Exception;

/**
 * Created by atesztoth on 2017. 04. 16..
 */
public class NotEnoughDominoException extends Exception {
    public NotEnoughDominoException(String message) {
        super(message);
    }

    public NotEnoughDominoException() {
        super();
    }
}
