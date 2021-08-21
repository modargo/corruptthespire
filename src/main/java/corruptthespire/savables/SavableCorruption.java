package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;

public class SavableCorruption implements CustomSavable<Integer> {
    public final static String SaveKey = "Corruption";

    @Override
    public Integer onSave() {
        return Cor.corruption;
    }

    @Override
    public void onLoad(Integer counter) {
        Cor.corruption = counter != null ? counter : 0;
    }
}
