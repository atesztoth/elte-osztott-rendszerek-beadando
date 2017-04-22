package domino;

import domino.Exception.BadDominoException;
import domino.Exception.BadInitialDominoStringException;
import domino.Exception.FakeCommandException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * The class representing a DominoClient.
 * Created by atesztoth on 2017. 04. 05..
 */
public class DominoClient {

    private String userName = "";
    ArrayList<Domino> dominos = new ArrayList<>();

    private DominoFileWriter dominoFileWriter = null;
    private Socket socket = null;

    // Communication
    private Scanner scanner;
    private PrintWriter printWriter;
    private boolean gotMyInitial = false;

    // We have to use command line args :(
    // Should look like this: DominoClient.java userName
    public static void main(String[] args) throws Exception {
        if (1 > args.length) {
            throw new Exception("Bad call! Egy argumentumot várok, mely a nevem.");
        }

        DominoClient dominoClient = new DominoClient(args[0]);

        // Let's play:
        dominoClient.connectToServer();
    }

    public DominoClient(String userName) {
        this.userName = userName;
        dominoFileWriter = new DominoFileWriter(userName + ".txt");
    }

    public String getUserName() {
        return userName;
    }

    /**
     * The following method connects to the server and by that, starts the whole program.
     */
    public void connectToServer() {
        // Getting props of the server from the config provider of this project:
        DominoConfigProvider config = DominoConfigProvider.getInstance(); // so we have the config

        System.out.printf("Trying to connect to the server..." + System.getProperty("line.separator"));
        try {
            socket = new Socket("localhost", (Integer) config.getValueOf("server_port"));
            scanner = new Scanner(socket.getInputStream());
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            // Here we gonna store our dominos based on server's answer...
            System.out.printf(userName + ": kapcsolódtam." + System.getProperty("line.separator"));

            // Sending my name...
            printWriter.println(userName);

            while (true) {
                String serverMessage = scanner.nextLine();
                System.out.println("! Server sent: " + serverMessage);

                try {
                    processServerCommand(serverMessage);
                } catch (FakeCommandException e) {
                    // This should be caught right here.
                    System.out.println("Hibás utasítást kaptam! " + System.getProperty("line.separator"));
                    e.printStackTrace();
                } catch (BadInitialDominoStringException e) {
                    e.printStackTrace();
                    exit(1);
                }

                if (serverMessage.equals("VEGE")) {
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            System.out.printf("Nem tudtam kapcsolódni a szerverhez!");
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for "inside class routing" under which I mean invoking the correct methods
     * of the class, and throwing exceptions if neccessary.
     *
     * @param command Command sent by the server.
     * @throws FakeCommandException, IOException, BadInitialDominoStringException
     */
    private void processServerCommand(String command) throws FakeCommandException, IOException, BadInitialDominoStringException {
        boolean basicCommand = true;

        if (!gotMyInitial) {
            // 65 34--6 9--10 12--31 25--14 5--15 6--6 8

            // Then we are waiting for the initials doing nothing else:
            String initialDominoRegexp = "^(([0-9]+ [0-9]+)--)+([0-9]+ [0-9]+)$";

            if (!command.matches(initialDominoRegexp)) {
                throw new BadInitialDominoStringException("Hibás init domino stringet kaptam!");
            }

            // Process initial pack of dominos:
            String[] individualDominos = command.split("--");

            for (String dominoString : individualDominos) {
                try {
                    saveDomino(dominoString);
                } catch (BadDominoException e) {
                    e.printStackTrace();
                    exit(1);
                }
            }

            // Oh:
            gotMyInitial = true; // ;)
            return; // End this cycle.
        }

        switch (command) {
            case "START":
                doTheStaterAction();
                break;
            case "VEGE":
                String msg = "Server command: VEGE";
                System.out.printf(msg + System.getProperty("line.separator"));
                dominoFileWriter.writeToFile(msg, true);

                // Give a chance for the server to end the game properly...
                // socket.close(); // Gonna be handled in the method that calls this.
                break;
            case "NINCS":
                System.out.printf("Server command: NINCS");
                dominoFileWriter.writeToFile("Server command: NINCS " + System.getProperty("line.separator"), true);
                break;
            default:
                basicCommand = false;
        }

        if (!basicCommand) {
            // Then it must be either a domino, or just one number.
            // Let's see if it is a new domino by checking if it matches a particular regexp:
            String regexp = "[0-9]+ [0-9]+";

            if (command.matches(regexp)) {
                // Then this is a domino!
                try {
                    saveDomino(command);
                } catch (BadDominoException e) {
                    System.out.printf("Nem tudtam menteni egy dominót, mert hiba volt vele." + System.getProperty("line.separator"));
                }
            } else {
                switch (Integer.parseInt(command)) {
                    case 0:
                        throw new FakeCommandException("Nem létező parancsot kaptam.");
                    default:
                        handleDominoNumberAction(Integer.parseInt(command));
                }
            }
        }
    }

    /**
     * Handling the scenario when we get a number from the server.
     *
     * @param dominoNum The domino we got sent.
     */
    private void handleDominoNumberAction(int dominoNum) {
        // Lets look for a domino that matches:
        boolean match = false;
        String msg = "";

        for (Domino d : dominos) {
            boolean firstSideMatch = d.getSide1() == dominoNum;
            boolean secondSideMatch = d.getSide2() == dominoNum;
            match = firstSideMatch && secondSideMatch;

            if (match) {
                // Then this domino does match.
                System.out.printf("Dominio MATCHED! " + System.getProperty("line.separator"));

                if (firstSideMatch) {
                    // Send the second side back
                    msg = userName + ": " + dominoNum + d.getSide2() + System.getProperty("line.separator");
                    System.out.printf(msg);
                    dominoFileWriter.writeToFile(msg, true);
                    printWriter.println(d.getSide2());
                } else {
                    // Send the first side back
                    msg = userName + ": " + dominoNum + d.getSide1() + System.getProperty("line.separator");
                    System.out.printf(msg);
                    dominoFileWriter.writeToFile(msg, true);
                    printWriter.println(d.getSide1());
                }

                // Remove from our dominos.
                dominos.remove(d);
                break; // Gimme' a break. Actually this is a must. I am modifying the ArrayList in a loop.
                // Should have used iterators? Maybe, but not today.
            }
        }

        if (!match) {
            // So when we couldn't find a domino that would match, we have to tell this to the server:
            msg = userName + ": UJ . NO MATCH!" + System.getProperty("line.separator");
            System.out.printf(msg);
            dominoFileWriter.writeToFile(msg, true);
            printWriter.println("UJ");
        }
    }

    /**
     * Method that gets called whenever this client is the one that starts playin'.
     */
    private void doTheStaterAction() {
        // Getting the first domino, and sending this to the server
        Domino first = dominos.get(0);

        // removing this:
        dominos.remove(0);

        // Send the domino to the server:
        printWriter.println(first.getSide1());

        String msg = userName + ": START " + first.toString();
        System.out.printf(msg + System.getProperty("line.separator"));
        dominoFileWriter.writeToFile(msg, true);
    }

    /**
     * This method is going to process one domino in the following form: "firstSide secondSide".
     *
     * @param domino The domino that should be saved.
     */
    private void saveDomino(String domino) throws BadDominoException {
        String[] sides = domino.split(" ");

        int side1;
        int side2;

        try {
            side1 = Integer.parseInt(sides[0]);
            side2 = Integer.parseInt(sides[1]);
        } catch (Exception e) {
            throw new BadDominoException("Hibás volt valamelyik oldal.");
        }


        dominos.add((new Domino(side1, side2)));
    }

}
