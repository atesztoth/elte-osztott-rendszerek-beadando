package domino;

import domino.ConfigProvider.AbstractConfigProvider;

/**
 * Implemented using singleton principle.
 * Created by atesztoth on 2017. 04. 14..
 */
public class DominoConfigProvider extends AbstractConfigProvider {
    // SINGLETON PATTERN! Why to make more instances?!

    private static DominoConfigProvider instance = null;
    private boolean hasRan = false;

    public static DominoConfigProvider getInstance() {
        if (null == instance) {
            instance = new DominoConfigProvider();
        }

        return instance;
    }

    private DominoConfigProvider() {
        // I know this is already overcomplicated, it would get nicer by introducing one of the dependency injection patterns to this
        // project, but that would be waaaay overcomplicated, so lets just do it this bare way:

        // Just to make it look more cool & to defeat instantiation, this has a private constructor.
        this.selfSetup();
    }

    private void selfSetup() {
        if (hasRan) {
            return;
        }

        // The port to start the server at:
        addNewSetting("server_port", 60504);
        // Path to the file that contains dominos:
        addNewSetting("domino_file", "dominos.txt");
        // Used for separating dominos when sending initial domino pack for clients.
        // This is more like an "internal setting", but can be changed as a feature.
        addNewSetting("domino_string_domino_separator", "--");
    }
}
