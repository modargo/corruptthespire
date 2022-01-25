package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;

public class SavableActive implements CustomSavable<Boolean> {
    public final static String SaveKey = "Active";

    @Override
    public Boolean onSave() {
        return Cor.active;
    }

    @Override
    public void onLoad(Boolean active) {
        Cor.active = active == null ? true : active;
    }
}
