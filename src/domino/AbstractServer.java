package domino;

import domino.Interface.ServerInterface;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by atesztoth on 2017. 04. 14..
 */
public class AbstractServer implements ServerInterface {

    protected int port;

    // Following var can be private, since from outside this class you need not to touch the original sercerSocket.
    // You only need to start a server, and close the connection when you're done with your work, so this var can be
    // easily encapsualted in this class allowing no one to touch it from outside.
    private ServerSocket serverSocket;

    // If somehow it would ever be needed to get the serverSocket object, There will be a method implemented that gives
    // you the reference for that.

    public AbstractServer(int port) {
        this.port = port;
    }

    @Override
    public void createServer() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void stopServer() throws IOException {
        serverSocket.close();
    }
}
