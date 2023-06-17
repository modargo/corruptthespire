package corruptthespire;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import corruptthespire.cards.corrupted.powers.CorruptedForm;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String ACTIVE = "active";
    private static final String NERF_CORRUPTED_FORM = "nerfCorruptedForm";
    public static SpireConfig config = null;

    public static boolean active() {
        return config == null || config.getBool(ACTIVE);
    }

    public static boolean nerfCorruptedForm() {
        return config != null && config.getBool(NERF_CORRUPTED_FORM);
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

    public static void setNerfCorruptedForm(boolean nerfCorruptedForm) {
        if (config != null) {
            config.setBool(NERF_CORRUPTED_FORM, nerfCorruptedForm);
            CorruptedForm corruptedForm = (CorruptedForm)CardLibrary.getCard(CorruptedForm.ID);
            corruptedForm.resetMagicNumber();
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
            defaults.put(NERF_CORRUPTED_FORM, Boolean.toString(false));
            config = new SpireConfig("CorruptTheSpire", "Config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
