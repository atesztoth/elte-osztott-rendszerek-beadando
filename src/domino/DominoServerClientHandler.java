package domino;

import domino.Entity.DominoClientMessageEntity;

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
    private String clientName;

    // In order to achive communicaiton:
    private PrintWriter printWriter; // For sending messages to the client.
    private Scanner scanner; // For reading input from the client.
    private boolean startSignalSent = false;

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

    public void workOfThread() {
        for (int i = 0; i < 10; i++) {
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

        // Accept client's name
        clientName = scanner.nextLine();

        System.out.println("Thread " + threadId + " is running! Player: " + clientName + System.getProperty("line.separator"));

        // Actually we can do one thing before even thinking about what the thread should do,
        // whatever other things. To send the initial pack of dominos. This is an important things.
        // Without dominos, you can't play the game of domino. Simple as that.
        printWriter.println(initialPack);

        try {
            threadHelper.waitForMyTurn(threadId);

            System.out.println("Niga, thread with player: " + clientName + " is rock n rollin!");
        } catch (InterruptedException e) {
            System.out.println("Failed to wait!");
            e.printStackTrace();
            exit(1);
        }

        // If this is the first thread, and start has not been sent yet...
        if (0 == threadId && !startSignalSent) {
            // Send start signal:
            printWriter.println("START");
            startSignalSent = true;

            System.out.println("Start signal sent!");

            // Sending a message invokes a response, so lets wait for that:
            // The first client is going to send a side of a domino, you can be 100% sure about that it is valid.
            DominoClientMessageEntity messageEntity = new DominoClientMessageEntity(scanner.nextLine(), clientName);
            dominoServerParamBag.setDominoClientMessageEntity(messageEntity);

            // We can pass the control to the next thread now:
            threadHelper.switchTurns();
        }

        while (true) {
            try {
                threadHelper.waitForMyTurn(threadId);
            } catch (InterruptedException e) {
                e.printStackTrace();
                exit(1);
            }

            if (!dominoServerParamBag.isGameOn()) {
                break;
            }
        }
    }
}
