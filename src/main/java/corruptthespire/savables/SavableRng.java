package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import corruptthespire.Cor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class SavableRng implements CustomSavable<Integer> {
    private static final Logger logger = LogManager.getLogger(SavableRng.class.getName());
    public final static String SaveKey = "CorruptionRng";

    @Override
    public Integer onSave() {
        return Cor.rng.counter;
    }

    @Override
    public void onLoad(Integer counter) {
        logger.info("Loading RNG. Cor.rng.counter: " + counter);
        Cor.resetRng(Settings.seed, counter);
    }
}
