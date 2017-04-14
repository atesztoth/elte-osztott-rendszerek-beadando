package domino;

/**
 * Represents a domino. Used package-wide.
 * Created by atesztoth on 2017. 04. 05..
 */
public class Domino {

    private int side1;
    private int side2;

    /**
     * @param side1
     * @param side2
     */
    public Domino(int side1, int side2) {
        this.side1 = side1;
        this.side2 = side2;
    }

    public int getSide1() {
        return side1;
    }

    public int getSide2() {
        return side2;
    }

    public String convertToText() {
        // toString() v2 lol
        return side1 + " " + side2;
    }

    @Override
    public String toString() {
        return "Domino{" +
                "side1=" + side1 +
                ", side2=" + side2 +
                '}';
    }
}
