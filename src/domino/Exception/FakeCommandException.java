package domino.Exception;

/**
 * Created by atesztoth on 2017. 04. 14..
 */
public class FakeCommandException extends Exception {

    public FakeCommandException(String message) {
        super(message);
    }

    public FakeCommandException() {
        super();
    }
}
