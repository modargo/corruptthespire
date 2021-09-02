package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;

import java.util.ArrayList;

public class SavableCorruptionPerFloor implements CustomSavable<ArrayList<Integer>> {
    public final static String SaveKey = "CorruptionPerFloor";

    @Override
    public ArrayList<Integer> onSave() {
        return Cor.corruptionPerFloor;
    }

    @Override
    public void onLoad(ArrayList<Integer> corruptionPerFloor) {
        Cor.corruptionPerFloor = corruptionPerFloor != null ? corruptionPerFloor : new ArrayList<>();
    }
}