package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;

import java.util.ArrayList;

public class SavableCorruptedRelicPool implements CustomSavable<ArrayList<String>> {
    public final static String SaveKey = "CorruptedRelicPool";

    @Override
    public ArrayList<String> onSave() {
        return Cor.corruptedRelicPool;
    }

    @Override
    public void onLoad(ArrayList<String> corruptedRelicPool) {
        Cor.corruptedRelicPool = corruptedRelicPool != null ? corruptedRelicPool : new ArrayList<>() ;
    }
}
