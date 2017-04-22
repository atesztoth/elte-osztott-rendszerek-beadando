package domino;

import domino.Entity.DominoClientMessageEntity;
import domino.Exception.FakeClientMessageException;

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
    private final DominoFileWriter dominoFileWriter;
    private String clientName;
    private boolean debug = false;
    private boolean isTesterOn = false;
    private DominoConfigProvider dominoConfigProvider = DominoConfigProvider.getInstance();

    // In order to achive communicaiton:
    private PrintWriter printWriter; // For sending messages to the client.
    private Scanner scanner; // For reading input from the client.

    public DominoServerClientHandler(Socket client, int threadId, DominoServerThreadHelper threadHelper, DominoServerParamBag dominoServerParamBag, String initialPack, DominoFileWriter dominoFileWriter) {
        this.client = client;
        this.threadId = threadId;
        this.threadHelper = threadHelper;
        this.dominoServerParamBag = dominoServerParamBag;
        this.initialPack = initialPack;
        this.dominoFileWriter = dominoFileWriter;

        debug = (Boolean) dominoConfigProvider.getValueOf("debug");
        isTesterOn = (Boolean) dominoConfigProvider.getValueOf("trigger_testing_mode");

        try {
            printWriter = new PrintWriter(client.getOutputStream(), true);
            scanner = new Scanner(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Nem sikerült létrehozni a klienssel kommunikáló objektumokat.");
            e.printStackTrace();
            exit(1);
        }
    }

    @Override
    public void run() {
        // Accept client's name
        clientName = scanner.nextLine();
//        dominoFileWriter.writeToFile(clientName + ": " + clientName, true);

        if (debug) {
            System.out.println("Thread " + threadId + " is running! Player: " + clientName);
        }

        // Actually we can do one thing before even thinking about what the thread should do,
        // whatever other things. To send the initial pack of dominos. This is an important things.
        // Without dominos, you can't play the game of domino. Simple as that.
        printWriter.println(initialPack);

        try {
            if (debug) {
                System.out.println("Thread with id: " + threadId + " is entering sleep mode.");
            }

            threadHelper.waitForMyTurn(threadId);

            if (debug) {
                System.out.println("Thread with id: " + threadId + " is rock n rollin!");
            }
        } catch (InterruptedException e) {
            System.out.println("Failed to wait!");
            e.printStackTrace();
            exit(1);
        }

        // If this is the first thread, and start has not been sent yet...
        boolean isStartRound = true;
        if (0 == threadId) {
            // Send start signal:
            printWriter.println("START");

            if (debug) {
                System.out.println("Start signal sent!");
            }

            // Sending a message invokes a response, so lets wait for that:
            // The first client is going to send a side of a domino, you can be 100% sure about that it is valid.
            DominoClientMessageEntity messageEntity = new DominoClientMessageEntity(scanner.nextLine(), clientName);
            dominoServerParamBag.setDominoClientMessageEntity(messageEntity);

            // In the first round it is a sure thing that a domino comes in.
            dominoServerParamBag.setHasAnyoneSentDominoIn(true);

            // Must write the message of the client to a file:
            dominoFileWriter.writeToFile(clientName + ": " + messageEntity.getLastMessage(), true);

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

            // GameOverAction: when no one could send a single domino in
            if (0 == threadId && !dominoServerParamBag.getHasAnyoneSentDominoIn() && !isStartRound && 1 > dominoServerParamBag.countTalon()) {
                // So when it's the 0 id-ed thread again, no one has sent in a domino, and is not a start round, and we have no
                // remaining elements in the talon.

                // Notify all the threads about that the game is over:
                dominoServerParamBag.setGameOn(false);
            }

            // This is common for all the threads: They must end their processes, since there is no game happening.
            if (!dominoServerParamBag.isGameOn()) {
                printWriter.println("VEGE");
                threadHelper.switchTurns();
                break;
            }

            // We must keep track of the players actions, especially if they send a domino.
            // We have to know, if a round without a domino sent in has happened. That means the end of the game.
            // Set the flag to false whenever a new round begins:
            // If, and only IF this thread is the beginner, the starter thread, it has the right to set
            // the flag to false. Why? Because I am doing this by the following logic:
            // If the started thread gets a domino, flag turns true, that okay...
            // But what if it does not? It sets the flag to false, every other thread can only do one thing:
            // set it to true. But if those, like the starter thread, do not get a domino, the flag gonna stay
            // on "false" till the new round begins, which means no client has sent a domino in = end of the game.
            if (0 == threadId && !isStartRound) {
                dominoServerParamBag.setHasAnyoneSentDominoIn(false);
            }

            // Ticking the flag of start round.
            isStartRound = false;

            // Sending last message for the client:
            printWriter.println(dominoServerParamBag.getDominoClientMessageEntity().getLastMessage());

            // Handling response:
            String response = scanner.nextLine();

            // See if it is a domino:
            if (response.matches("[0-9]+")) {
                // Nice, we've got a domino back, lets store it:
                dominoServerParamBag.setDominoClientMessageEntity(new DominoClientMessageEntity(response, clientName));

                // Tick the flag, let's show that in this round there was a domino sent in:
                dominoServerParamBag.setHasAnyoneSentDominoIn(true);

                // Switching turns.
                threadHelper.switchTurns();
            } else {
                boolean breakWhile = false;

                System.out.println(clientName + ": " + response);

                switch (response) {
                    case "UJ":
                        sendNewDominoFromTalon();
                        threadHelper.switchTurns();
                        break;
                    case "NYERTEM":
                        if (debug) {
                            System.out.println("A nyertes: " + clientName);
                        }
                        winningAction();
                        breakWhile = true;
                        threadHelper.switchTurns();
                        break;
                    default:
                        try {
                            throw new FakeClientMessageException("Nem létező dolgot küldött a kliens: \"" + response + "\" .");
                        } catch (FakeClientMessageException e) {
                            e.printStackTrace();
                            exit(1);
                        }
                }

                // This stops while cycle of the thread.
                if (breakWhile) {
                    break;
                }
            }
        }
    }

    private void sendNewDominoFromTalon() {
        Domino d = null;

        // if we did not get null back from getOneFromTalon, we can send this to the client.
        if (null != (d = dominoServerParamBag.getOneFromTalon())) {
            printWriter.println(d.convertToText());
        } else {
            printWriter.println("NINCS");
        }
    }

    private void winningAction() {
        // Send everyone except the winner: "VEGE".
        threadHelper.triggerWinningAction(threadId);

        // Notifying player about changed game state:
        dominoServerParamBag.setGameOn(false);
    }
}
