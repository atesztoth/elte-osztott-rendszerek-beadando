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
    private int skipThisId = -1; // This id gets skipped.
    private boolean debug = false; // Debug mode switcher

    public DominoServerThreadHelper(int whoGoes, int howMany) {
        this.whoGoes = whoGoes;
        this.howMany = howMany;
    }

    public synchronized void triggerWinningAction(int winnerThreadId) {
        // Except this id, everyone gets called one last time to finish the game.
        whoGoes = 0;
        skipThisId = winnerThreadId;
        notifyAll();
    }

    public synchronized void blockThreads() {
        whoGoes = -1; // no thread should have an ID of -1...
        if (debug) {
            System.out.println("ThreadHelper: Threads blocked.");
        }
        notifyAll();
    }

    public synchronized void letItRoll() {
        whoGoes = 0; // Set this to the first possible id.
        if (debug) {
            System.out.println("ThreadHelper: LET IT ROLL!");
        }
        notifyAll();
    }

    public synchronized int getWhoGoes() {
        return this.whoGoes;
    }

    public synchronized void switchTurns() {
        int newWhoGoes = (whoGoes + 1) % howMany;
        whoGoes = newWhoGoes == skipThisId ? (newWhoGoes + 1) % howMany : newWhoGoes; // calculating id...
        if (debug) {
            System.out.println("Switching turns: " + whoGoes);
        }
        notifyAll(); // ctrl + click, couldn't describe it any better.
    }

    public synchronized void waitForMyTurn(int id) throws InterruptedException {
        while (whoGoes != id) {
            wait(); // works hands-in-hands with notifyAll()
        }
    }
}
