package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import corruptthespire.Cor;

public class SavableRng implements CustomSavable<Integer> {
    public final static String SaveKey = "CorruptionRng";

    @Override
    public Integer onSave() {
        return Cor.rng.counter;
    }

    @Override
    public void onLoad(Integer counter) {
        Cor.rng = new Random(Settings.seed, counter != null ? counter : 0);
    }
}
