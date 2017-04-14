package domino;

import domino.Exception.BadDominoException;

import java.util.ArrayList;

/**
 * Created by atesztoth on 2017. 04. 05..
 */
public class DominoClient {

    private String userName = "";
    ArrayList<Domino> dominos = new ArrayList<>();

    // For testing
    public static void main(String[] args) {

    }

    public DominoClient(String userName) {
        this.userName = userName;
    }

    public void connectToServer() {
        // Getting props of the server from the config provider of this project:

        DominoConfigProvider config = DominoConfigProvider.getInstance(); // so we have the config

    }

    /**
     * This method is going to process one domino in the following form: "firstSide secondSide".
     * @param domino
     */
    private void processDomino(String domino) throws BadDominoException {
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
