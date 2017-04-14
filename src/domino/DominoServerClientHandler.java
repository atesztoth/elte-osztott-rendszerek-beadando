package domino;

import java.net.Socket;

/**
 * ! Thread implements Runnable interface!
 * Created by atesztoth on 2017. 04. 14..
 */
public class DominoServerClientHandler extends Thread {

    private Socket client;
    private final int threadId;
    private final DominoServerThreadHelper threadHelper;

    public DominoServerClientHandler(Socket client, int threadId, DominoServerThreadHelper threadHelper) {
        this.client = client;
        this.threadId = threadId;
        this.threadHelper = threadHelper;
    }

    public void workOfThread() {
        for(int i = 0; i < 10; i++) {
            try {
                threadHelper.waitForMyTurn(threadId);
                System.out.println("This is A and B ");
                threadHelper.switchTurns();
            } catch (InterruptedException ex) {
                // Handle it...
            }
        }
    }

    @Override
    public void run() {
        super.run();

        workOfThread();
    }
}
