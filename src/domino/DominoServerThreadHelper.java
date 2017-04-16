package domino;

/**
 * This class is synchronised class between threads of DominoServer helping to determine if a thread should
 * let code of it work, or wait for other to do it's job.
 * This class is going to be a shared resource between threads.
 * Created by atesztoth on 2017. 04. 14..
 */
public class DominoServerThreadHelper {

    private int whoGoes; // "id" of the thread that can do it's thing
    private int howMany; // Number of how many threads there are.

    public DominoServerThreadHelper(int whoGoes, int howMany) {
        this.whoGoes = whoGoes;
        this.howMany = howMany;
    }

    public synchronized int getWhoGoes() {
        return this.whoGoes;
    }

    public synchronized void switchTurns() {
        whoGoes = (whoGoes + 1) % howMany; // calculating id...
        notifyAll(); // ctrl + click, couldn't describe it any better.
    }

    public synchronized void waitForMyTurn(int id) throws InterruptedException {
        while (whoGoes != id) {
            wait(); // works hands-in-hands with notifyAll()
        }
    }
}
