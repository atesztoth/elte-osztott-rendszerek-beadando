package domino.DominoProvider;

import domino.Domino;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * This class is responsible for providing dominos for the server, reading them from a file.
 * Created by atesztoth on 2017. 04. 14..
 */
public class DominoProvider implements DominoProviderInterface {

    private String fileName;
    private ArrayList<Domino> dominos = new ArrayList<>();

    public DominoProvider(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public ArrayList<Domino> getDominos() {
        readDominosFromFile();

        return dominos;
    }

    private void readDominosFromFile() {
        try {
            InputStream inputStream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // We can read now:
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.printf(line + " \n");
            }
        } catch (FileNotFoundException e) {
            System.out.printf("A keresett file nem található. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
