package domino.DominoProvider;

import domino.Domino;

import java.util.ArrayList;

/**
 * Created by atesztoth on 2017. 04. 14..
 */
public interface DominoProviderInterface {

    /**
     * Method for getting the dominos.
     * @return ArrayList of Dominos.
     */
    ArrayList<Domino> getDominos();

}
