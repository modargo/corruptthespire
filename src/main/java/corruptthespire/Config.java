package corruptthespire;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static String ACTIVE = "active";
    public static SpireConfig config = null;

    public static boolean active() {
        return config == null || config.getBool(ACTIVE);
    }

    public static void setActive(boolean active) {
        if (config != null) {
            config.setBool(ACTIVE, active);
            try {
                config.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initialize() {
        try {
            Properties defaults = new Properties();
            defaults.put(ACTIVE, Boolean.toString(true));
            config = new SpireConfig("CorruptTheSpire", "Config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
