package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;

import java.util.ArrayList;

public class SavableCorruptedEventList implements CustomSavable<ArrayList<String>> {
    public final static String SaveKey = "CorruptedEventList";

    @Override
    public ArrayList<String> onSave() {
        return Cor.corruptedEventList;
    }

    @Override
    public void onLoad(ArrayList<String> corruptedEventList) {
        Cor.corruptedEventList = corruptedEventList != null ? corruptedEventList : new ArrayList<>() ;
    }
}
