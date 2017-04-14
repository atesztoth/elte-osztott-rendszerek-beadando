package domino;

import domino.ConfigProvider.AbstractConfigProvider;

/**
 * Implemented using singleton principle. A very basic one, but it is fairly enough.
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

        addNewSetting("server_port", 60504);
    }
}
