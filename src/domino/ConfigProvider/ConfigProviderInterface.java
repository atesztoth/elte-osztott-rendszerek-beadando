package domino.ConfigProvider;

import java.util.Map;

/**
 * Created by atesztoth on 2017. 04. 14..
 * Yes, yes... Overkill, not neccessary, I know...
 * I am just implementing this because... Well, just experimenting, learning some Java.
 */
public interface ConfigProviderInterface {

    // Every config provider should be able to give you a list of all the config vars,
    // and give you one specific config setting. No matter where they are getting their
    // information from, what matters is only that they should be able to do things mentioned above, so:

    // Getting the whole config
    // Giving Object as second param of the map, so this can be String, Integer, whatever. I'll know what to cast it when I am gonna use it.
    Map<String, Object> getCompleteConfig(); // I don't even define which kinda map you should use to store your keys. Your choice.

    // Returns one specified option from the config.
    Object getValueOf(String key);

    // Ability to add properties to your config
    void addNewSetting(String key, Object val);
}
