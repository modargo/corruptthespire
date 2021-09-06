package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import corruptthespire.Cor;
import corruptthespire.corruptions.fight.FightCorruptionDistribution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        this.testRng();
        Cor.rng = new Random(Settings.seed, counter != null ? counter : 0);
    }

    private void testRng() {
        //long seed = -868755405467266338L;
        long seed = Settings.seed;
        Random r0 = new Random(seed, 27);
        Random r1 = new Random(seed);
        Random r2 = new Random(seed);
        Random r3 = new Random(seed);
        Random r4 = new Random(seed);
        Random r5 = new Random(seed);
        Random r6 = new Random(seed);
        int finalNumber = r0.random(1399);
        logger.info("final number: " + finalNumber);
        iterateRandom(r1, r -> r.random(999));
        iterateRandom(r2, r -> r.random(1400));
        iterateRandom(r3, r -> r.random(100));
        iterateRandom(r4, Random::random);
        iterateRandom(r5, r -> r.random(1.0F));
        iterateRandom(r6, r -> r.random(1000000000));

    }

    private void iterateRandom(Random r, Consumer<Random> f) {
        for (int i = 0; i < 27; i++) {
            f.accept(r);
        }
        int finalNumber = r.random(1399);
        logger.info("final number: " + finalNumber + " (counter: " + r.counter + ")");
    }
}
