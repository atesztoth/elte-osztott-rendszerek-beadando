package domino;

import domino.Interface.ServerInterface;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by atesztoth on 2017. 04. 14..
 */
public class AbstractServer implements ServerInterface {

    protected int port;
    protected ServerSocket serverSocket;

    public AbstractServer(int port) {
        this.port = port;
    }

    @Override
    public void createServer() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.printf("Server started.");
    }

    @Override
    public void stopServer() throws IOException {
        serverSocket.close();
        System.out.printf("Server closed.");
    }
}
