package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;

import java.util.ArrayList;

public class SavableChaoticEventList implements CustomSavable<ArrayList<String>> {
    public final static String SaveKey = "ChaoticEventList";

    @Override
    public ArrayList<String> onSave() {
        return Cor.chaoticEventList;
    }

    @Override
    public void onLoad(ArrayList<String> chaoticEventList) {
        Cor.chaoticEventList = chaoticEventList != null ? chaoticEventList : new ArrayList<>() ;
    }
}
