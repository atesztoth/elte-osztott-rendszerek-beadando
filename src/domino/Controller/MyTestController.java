package domino.Controller;

import domino.ConfigProvider.ConfigProviderInterface;
import domino.DominoClient;
import domino.DominoConfigProvider;
import domino.DominoServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atesztoth on 2017. 04. 14..
 */
public class MyTestController {

    public static void main(String[] args) {
        // Creating a new server:

        Thread server = new Thread() {
            @Override
            public void run() {
                super.run();

                // Getting the configProvider:
                DominoConfigProvider config = DominoConfigProvider.getInstance();
                DominoServer dominoServer = new DominoServer((Integer) config.getValueOf("server_port"), 2, "dominos.txt", "serverlog.log");

                // Start the server:
                try {
                    dominoServer.createServer();
                    dominoServer.startGame();
                    dominoServer.stopServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread client = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DominoClient dc = new DominoClient("Frist Client");
                dc.connectToServer();
            }
        };

        server.start();
        client.start();
    }

}
