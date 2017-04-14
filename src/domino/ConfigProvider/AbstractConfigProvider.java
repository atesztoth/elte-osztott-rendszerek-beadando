package domino.ConfigProvider;

import java.util.HashMap;

/**
 * THIS IS THE CLASS THAT SHOULD BE EXTENDED WHENEVER YOU CREATE A NEW CONFIG PROVIDER.
 * Though, if you want to do some very specific thing, you still have the ConfigProviderInterface to play with.
 * Created by atesztoth on 2017. 04. 14..
 *
 * This class uses HashMap to implement ConfigProviderInterface
 */
public class AbstractConfigProvider implements ConfigProviderInterface {

    // Giving the skeleton for a well crafted config provider object:
    private HashMap<String, Object> configContainer = new HashMap<>();

    @Override
    public HashMap<String, Object> getCompleteConfig() {
        return configContainer;
    }

    @Override
    public Object getValueOf(String key) {
        // Let's just use those confortable methods given us by maps :3.
        return configContainer.get(key);
    }

    @Override
    public void addNewSetting(String key, Object val) {
        configContainer.put(key, val);
    }
}
