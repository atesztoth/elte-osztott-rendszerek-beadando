package domino;

import domino.ConfigProvider.AbstractConfigProvider;

/**
 * Created by atesztoth on 2017. 04. 14..
 */
public class DominoConfigProvider extends AbstractConfigProvider {

    public DominoConfigProvider() {
        // I know this is already overcomplicated, it would get nicer by introducing one of the dependency injection patterns to this
        // project, but that would be waaaay overcomplicated, so lets just do it this bare way:

        addNewSetting("server_port",60504);
    }
}
