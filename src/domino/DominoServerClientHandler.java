package domino;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import static java.lang.System.exit;
/**
 * ! Thread implements Runnable interface!
 * Created by atesztoth on 2017. 04. 14..
 */
public class DominoServerClientHandler extends Thread {

    private Socket client;
    private final int threadId;
    private final DominoServerThreadHelper threadHelper;
    private final DominoServerParamBag dominoServerParamBag;
    private final String initialPack;

    // In order to achive communicaiton:
    private PrintWriter printWriter; // For sending messages to the client.
    private Scanner scanner; // For reading input from the client.

    public DominoServerClientHandler(Socket client, int threadId, DominoServerThreadHelper threadHelper, DominoServerParamBag dominoServerParamBag, String initialPack) {
        this.client = client;
        this.threadId = threadId;
        this.threadHelper = threadHelper;
        this.dominoServerParamBag = dominoServerParamBag;
        this.initialPack = initialPack;

        try {
            printWriter = new PrintWriter(client.getOutputStream(), true);
            scanner = new Scanner(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Nem sikerült létrehozni a klienssel kommunikáló objektumokat.");
            e.printStackTrace();
            exit(1);
        }
    }

    /**
     * Sends initial pack of dominos for the client.
     * @param dominosForClient
     */
    public void sendDominosToClient(String dominosForClient) {
        printWriter.println(initialPack);
    }


    public void workOfThread() {
        for(int i = 0; i < 10; i++) {
            try {
                threadHelper.waitForMyTurn(threadId);
                System.out.println("This is A and B ");
                threadHelper.switchTurns();
            } catch (InterruptedException ex) {
                // Handle it...
            }
        }
    }

    @Override
    public void run() {
        super.run();

        workOfThread();
    }
}
