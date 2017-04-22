package domino;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Separting logic for log file writing of my server.
 * Created by atesztoth on 2017. 04. 14..
 */
public class DominoFileWriter {

    private String fileName;
    private PrintWriter printWriter;

    public DominoFileWriter(String fileName) {
        this.fileName = fileName;
    }

    /**
     * This should be used to output your content to your file.
     * Synchronized, so this can be used when the object is shared.
     * @param line The line to print.
     * @param append Sets up appending.
     */
    public synchronized void writeToFile(String line, boolean append) {
        try {
            createPrintWriter(append, true);
            printWriter.write(line + System.getProperty("line.separator"));
            closePrintWriter();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helps instantiating the core of this class, a PrintWriter object.
     * @param append Appending.
     * @param autoFlush AutoFlush.
     * @throws FileNotFoundException
     */
    private void createPrintWriter(boolean append, boolean autoFlush) throws FileNotFoundException {
        this.printWriter = new PrintWriter(new FileOutputStream(new File(fileName), append), autoFlush);
    }

    /**
     * Talks for itself.
     */
    private void closePrintWriter() {
        this.printWriter.close();
    }
}
