package domino;

import java.io.*;

/**
 * Separting logic for log file writing for my server.
 * Created by atesztoth on 2017. 04. 14..
 */
public class DominoFileWriter {

    private String fileName;
    private PrintWriter printWriter;

    public DominoFileWriter(String fileName) {
        this.fileName = fileName;
    }

    public void writeToFile(String line, boolean append, boolean autoFlush) {
        try {
            createPrintWriter(append, autoFlush);
            printWriter.write(line + System.getProperty("line.separator"));
            closePrintWriter();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createPrintWriter(boolean append, boolean autoFlush) throws FileNotFoundException {
        this.printWriter = new PrintWriter(new FileOutputStream(new File(fileName), append), autoFlush);
    }

    private void closePrintWriter() {
        this.printWriter.close();
    }
}
