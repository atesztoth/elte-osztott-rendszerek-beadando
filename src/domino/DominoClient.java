package domino;

import domino.Exception.BadDominoException;
import domino.Exception.FakeCommandException;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Created by atesztoth on 2017. 04. 05..
 */
public class DominoClient {

    private String userName = "";
    ArrayList<Domino> dominos = new ArrayList<>();

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
    }

    public String getUserName() {
        return userName;
    }

    public void connectToServer() {
        // Getting props of the server from the config provider of this project:

        DominoConfigProvider config = DominoConfigProvider.getInstance(); // so we have the config

        // Getting the dominos. Remember, 7 reads!
        // simulation:
        ArrayList<String> simulation = new ArrayList<>();

        simulation.add((new Random()).nextInt(20) + " " + (new Random()).nextInt(20));
        simulation.add((new Random()).nextInt(20) + " " + (new Random()).nextInt(20));
        simulation.add((new Random()).nextInt(20) + " " + (new Random()).nextInt(20));
        simulation.add((new Random()).nextInt(20) + " " + (new Random()).nextInt(20));
        simulation.add((new Random()).nextInt(20) + " " + (new Random()).nextInt(20));
        simulation.add((new Random()).nextInt(20) + " " + (new Random()).nextInt(20));
        simulation.add((new Random()).nextInt(20) + " " + (new Random()).nextInt(20));

        for (String dominoString : simulation) {
            System.out.printf("Generated domino: " + dominoString + " \n");

            try {
                saveDomino(dominoString);
            } catch (BadDominoException e) {
                e.printStackTrace();
            }
        }

        // Simulating start action:
        try {
            processServerCommand("START");
        } catch (FakeCommandException e) {
            e.printStackTrace();
        }

        System.out.printf("Dominos in dominos ArrayList: \n");
        for (Domino d : dominos) {
            System.out.println(d.toString() + " \n");
        }

        /**
         * Real code is gonna be under this comment:
         */


//        if (null != socket) {
//            try {
//                socket = new Socket("localhost", (Integer) config.getValueOf("server_port"));
//
//                // Here we gonna store our dominos based on server's answer...
//
//            } catch (IOException e) {
//                System.out.printf("Nem tudtam kapcsolódni a szerverhez!");
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * NOTE: exceuting an Action works like this:
     * processServerCommand gives the method a reference of the connection for sending data & things,
     * in return, THAT METHOD is gonna listen for the server's response, and recall itself if there is an answer.
     */

    /**
     * This method is responsible for "inside class routing" under which I mean invoking the correct methods
     * of the class, and throwing exceptions if neccessary.
     *
     * @param command
     * @throws FakeCommandException
     */
    private void processServerCommand(String command) throws FakeCommandException {
        switch (command) {
            case "START":
                doTheStaterAction();
                break;
            case "VEGE":
                System.out.printf("Vesztettem :(");
                break;
            default:
                throw new FakeCommandException("Nem létező parancsot kaptam.");
        }
    }

    private void doTheStaterAction() {
        // Getting the first domino, and sending this to the server
        Domino first = dominos.get(0);

        // removing this:
        dominos.remove(0);

        System.out.println("Simulate sending: " + first.getSide1());
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
