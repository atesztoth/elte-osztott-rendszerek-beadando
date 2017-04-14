package domino.Interface;

import java.io.IOException;

public interface ServerInterface {

    void createServer() throws IOException;

    void stopServer() throws IOException;
}