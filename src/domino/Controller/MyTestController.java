package domino.Controller;

import domino.DominoConfigProvider;
import domino.DominoServer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by atesztoth on 2017. 04. 14..
 */
public class MyTestController {

    public static void main(String[] args) {
        // Creating a new server:
        DominoConfigProvider config = new DominoConfigProvider();

        DominoServer dominoServer = new DominoServer((Integer) config.getValueOf("server_port"));

    }

}
