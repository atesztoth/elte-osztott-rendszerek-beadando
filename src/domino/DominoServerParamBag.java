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
    private boolean gameOn = true; // indicates if the game has ended

    public DominoServerParamBag() {
    }

    public synchronized ArrayList<Domino> getTalon() {
        return talon;
    }

    public synchronized void setTalon(ArrayList<Domino> talon) {
        this.talon = talon;
    }

    public synchronized DominoClientMessageEntity getDominoClientMessageEntity() {
        return dominoClientMessageEntity;
    }

    public synchronized void setDominoClientMessageEntity(DominoClientMessageEntity dominoClientMessageEntity) {
        this.dominoClientMessageEntity = dominoClientMessageEntity;
    }

    public synchronized boolean isGameOn() {
        return gameOn;
    }

    public synchronized void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }
}
