package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.Settings;
import corruptthespire.Cor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SavableRewardRng implements CustomSavable<Integer> {
    private static final Logger logger = LogManager.getLogger(SavableRewardRng.class.getName());
    public final static String SaveKey = "CorruptionRewardRng";

    @Override
    public Integer onSave() {
        return Cor.rewardRng.counter;
    }

    @Override
    public void onLoad(Integer counter) {
        logger.info("Loading reward RNG. Cor.rewardRng.counter: " + counter);
        Cor.resetRewardRng(Settings.seed, counter);
    }
}
