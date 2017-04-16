package domino;

import domino.DominoProvider.DominoProvider;
import domino.Exception.InvalidPlayerNumberException;
import domino.Exception.NotEnoughDominoException;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.System.exit;

/**
 * The DominoServer class. Implements behaviour expected from our server.
 * Created by atesztoth on 2017. 04. 05..
 */
public class DominoServer extends AbstractServer {

    private int numberOfPlayers;
    private String dominoFile;
    private String logFile;
    private DominoConfigProvider dominoConfigProvider = DominoConfigProvider.getInstance();
    private DominoFileWriter dominoFileWriter;

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

        try {
            dominoServer.createServer();
            dominoServer.startGame();
            dominoServer.stopServer();

        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public DominoServer(int port, int numberOfPlayers, String dominoFile, String logFile) {
        super(port);

        this.numberOfPlayers = numberOfPlayers;
        this.dominoFile = dominoFile;
        this.logFile = logFile;
        this.dominoFileWriter = new DominoFileWriter(logFile);
    }


    /**
     * Starts the whole game.
     */
    public void startGame() {
        // Wait for players to join:
        // To every client I have to give a new thread.

        // First, let's get those clients:
        ArrayList<Domino> dominos = null;

        try {
            dominos = getDominos();
        } catch (NotEnoughDominoException e) {
            e.printStackTrace();
            exit(1);
        }

        ArrayList<Socket> clients = new ArrayList<>();
        ArrayList<DominoServerClientHandler> threads = new ArrayList<>();

        System.out.printf("Waiting for connections... " + System.getProperty("line.separator"));

        // Create a shared object that is gonna control threads work:
        // "ThreadController"
        DominoServerThreadHelper threadController = new DominoServerThreadHelper(1, numberOfPlayers);
        threadController.blockThreads(); // Let no one do anything!

        // A shared parameter bag:
        DominoServerParamBag dominoServerParamBag = new DominoServerParamBag();

        // Create the fileWriter
        dominoFileWriter = new DominoFileWriter(logFile);

        for (int i = 0; i < numberOfPlayers; i++) {
            try {
                // Adding the connection to a list of mine:
                clients.add(serverSocket.accept());

                // Building initial pack of dominos for the client:
                StringBuilder initialPack = new StringBuilder();
                int counter = 0;
                for (Iterator<Domino> it = dominos.iterator(); it.hasNext();) {
                    Domino d = it.next();

                    initialPack.append(d.convertToText()).append(counter < 6 ? (String) dominoConfigProvider.getValueOf("domino_string_domino_separator") : "");
                    ++counter;

                    // We should remove the domino from our list.
                    it.remove();

                    if(7 == counter) {
                        break;
                    }
                }

                // UPDATE our parameter bag right now to be up to date before a new thread could start:
                dominoServerParamBag.setTalon(dominos); // Current state of dominos array is always going to be the talon.

                // Lets give it a thread, and correctly instantiate a handler object:
                threads.add(new DominoServerClientHandler(clients.get(i), i, threadController, dominoServerParamBag, initialPack.toString(), dominoFileWriter));
                threads.get(i).start();

                System.out.printf("Client joined!" + System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Now everyone has connected. Let it roll!
        threadController.letItRoll();
        System.out.println("a-a-a, who goes after letitroll: " + threadController.getWhoGoes() + System.getProperty("line.separator"));

        // Before terminating the server, lets wait for all threads to finish:
        for (DominoServerClientHandler t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.printf("A join failed!");
                e.printStackTrace();
            }
        }

        System.out.printf("Closing connections...");
        for (Socket s : clients) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets dominos through DominoProvider. Also counts if there is enough dominos.
     *
     * @return ArrayList of Dominos
     * @throws NotEnoughDominoException
     */
    private ArrayList<Domino> getDominos() throws NotEnoughDominoException {
        DominoProvider dominoProvider = new DominoProvider((String) dominoConfigProvider.getValueOf("domino_file"));
        ArrayList<Domino> dominos = dominoProvider.getDominos();

        if (dominos.size() < numberOfPlayers * 7) {
            throw new NotEnoughDominoException("Nincs elég dominó a játék kezdéséhez!");
        }

        return dominos;
    }

}
