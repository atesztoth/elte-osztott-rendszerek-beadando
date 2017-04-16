package domino;

import domino.Entity.DominoClientMessageEntity;

import java.util.ArrayList;

/**
 * This package gets injected to every ThreadHelper instance which makes it great for communication between threads.
 * Created by atesztoth on 2017. 04. 16..
 */
public class DominoServerParamBag {

    private ArrayList<Domino> talon = new ArrayList<>();
    private DominoClientMessageEntity dominoClientMessageEntity = null;

    public DominoServerParamBag() {
    }

    public ArrayList<Domino> getTalon() {
        return talon;
    }

    public void setTalon(ArrayList<Domino> talon) {
        this.talon = talon;
    }

    public DominoClientMessageEntity getDominoClientMessageEntity() {
        return dominoClientMessageEntity;
    }

    public void setDominoClientMessageEntity(DominoClientMessageEntity dominoClientMessageEntity) {
        this.dominoClientMessageEntity = dominoClientMessageEntity;
    }
}
