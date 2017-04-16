package domino;

import domino.Exception.BadDominoException;
import domino.Exception.FakeCommandException;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * The class representing a DominoClient.
 * Created by atesztoth on 2017. 04. 05..
 */
public class DominoClient {

    private String userName = "";
    ArrayList<Domino> dominos = new ArrayList<>();

    private DominoFileWriter dominoFileWriter = null;
    private Socket socket = null;

    // We have to use command line args :(
    // Should look like this: DominoClient.java userName
    public static void main(String[] args) throws Exception {
        if (1 > args.length) {
            throw new Exception("Bad call! Egy argumentumot várok, mely a nevem.");
        }

        DominoClient dominoClient = new DominoClient(args[0]);

        System.out.printf("My name is: " + dominoClient.getUserName() + "\n");

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

    public void connectToServer() {
        // Getting props of the server from the config provider of this project:
        DominoConfigProvider config = DominoConfigProvider.getInstance(); // so we have the config

        /**
         * Real code is gonna be under this comment:
         */

        System.out.printf("Trying to connect to the server...");
        try {
            socket = new Socket("localhost", (Integer) config.getValueOf("server_port"));

            // Here we gonna store our dominos based on server's answer...

            System.out.printf(userName + ": kapcsolódtam.");

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
     * @throws FakeCommandException, IOException
     */
    private void processServerCommand(String command) throws FakeCommandException, IOException {
        boolean basicCommand = true;

        switch (command) {
            case "START":
                doTheStaterAction();
                break;
            case "VEGE":
                String msg = userName + ": VEGE" + System.getProperty("line.separator");
                System.out.printf(msg);
                dominoFileWriter.writeToFile(msg, true, true);

                // Give a chance for the server to end the game properly...
                socket.close();
                break;
            case "NINCS":
                System.out.printf(userName + ": NINCS " + System.getProperty("line.separator"));
                dominoFileWriter.writeToFile(userName + ": NINCS " + System.getProperty("line.separator"), true, true);
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
     * @param dominoNum
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
                    dominoFileWriter.writeToFile(msg, true, true);
                } else {
                    // Send the first side back
                    msg = userName + ": " + dominoNum + d.getSide1() + System.getProperty("line.separator");
                    System.out.printf(msg);
                    dominoFileWriter.writeToFile(msg, true, true);
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
            dominoFileWriter.writeToFile(msg, true, true);
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

        String msg = userName + ": START " + first.toString() + System.getProperty("line.separator");
        System.out.printf(msg);
        dominoFileWriter.writeToFile(msg, true, true);
    }

    /**
     * This method is going to process one domino in the following form: "firstSide secondSide".
     *
     * @param domino
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
