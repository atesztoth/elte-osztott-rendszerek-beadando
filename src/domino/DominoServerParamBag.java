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
    private boolean hasAnyoneSentDominoIn = false;
    private int siaTheGreatestThreadId = 0;

    public DominoServerParamBag() {
    }

    public synchronized ArrayList<Domino> getTalon() {
        return talon;
    }

    /**
     *
     * @return Returns first domino from the talon, null if nothing is in.
     */
    public synchronized Domino getOneFromTalon() {
        Domino d = null;

        if (talon.size() > 0) {
            d = talon.get(0);
            talon.remove(d);
        }

        return d;
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

    public synchronized boolean getHasAnyoneSentDominoIn() {
        return hasAnyoneSentDominoIn;
    }

    public synchronized void setHasAnyoneSentDominoIn(boolean hasAnyoneSentDominoIn) {
        this.hasAnyoneSentDominoIn = hasAnyoneSentDominoIn;
    }

    /**
     * Counts how many element there are in the talon.
     *
     * @return number of elements in the talon.
     */
    public synchronized int countTalon() {
        return talon.size();
    }

    public synchronized int getSiaTheGreatestThreadId() {
        return siaTheGreatestThreadId;
    }

    public synchronized void setSiaTheGreatestThreadId(int siaTheGreatestThreadId) {
        this.siaTheGreatestThreadId = siaTheGreatestThreadId;
    }
}
