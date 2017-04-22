package domino.DominoProvider;

import domino.Domino;
import domino.Exception.BadDominoException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static java.lang.System.exit;

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
                String[] sides = line.split(" ");

                try {
                    dominos.add(new Domino(Integer.parseInt(sides[0]), Integer.parseInt(sides[1])));
                } catch (Exception e) {
                    try {
                        throw new BadDominoException("Hibás volt valamelyik oldal.");
                    } catch (BadDominoException e1) {
                        // Yes, yes, I know it seems like a joke to instantly catch your error. But I want to show an error
                        // to the user, while making the program continue running. This is why I am doing this this way.
                        System.out.println("A hibás dominó: " + line + System.getProperty("line.separator"));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.printf("A keresett file nem található." + System.getProperty("line.separator"));
            e.printStackTrace();
            exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
