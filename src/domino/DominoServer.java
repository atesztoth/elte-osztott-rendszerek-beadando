package domino;

import domino.DominoProvider.DominoProvider;
import domino.Exception.InvalidPlayerNumberException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The DominoServer class. Implements behaviour expected from our server.
 * Created by atesztoth on 2017. 04. 05..
 */
public class DominoServer extends AbstractServer {

    private int numberOfPlayers;
    private String dominoFile;
    private String logFile;

    public static void main(String[] args) throws Exception {
        // numberOfPlayas dominoFile logFile

        if (3 != args.length) {
            throw new Exception("Bad call! 3 argumentumot várok!");
        }

        int n = Integer.parseInt(args[0]);

        if (n < 2 || n > 4) {
            throw new InvalidPlayerNumberException("2 és 4 között kell lennie a játékosok számának!");
        }

        DominoConfigProvider dominoConfigProvider = DominoConfigProvider.getInstance();

        DominoServer dominoServer = new DominoServer((Integer) dominoConfigProvider.getValueOf("server_port"), n, args[1], args[2]);

        dominoServer.startGame();
    }

    public DominoServer(int port, int numberOfPlayers, String dominoFile, String logFile) {
        super(port);

        this.numberOfPlayers = numberOfPlayers;
        this.dominoFile = dominoFile;
        this.logFile = logFile;
    }


    public void startGame() {
        // Wait for players to join:
        // Every client I have to give a new thread.
        for (int i = 0; i < numberOfPlayers; i++) {

        }
    }

}
