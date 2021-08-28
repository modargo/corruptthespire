package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;
import corruptthespire.CorruptionFlags;

import java.util.ArrayList;

public class SavableCorruptionFlags implements CustomSavable<CorruptionFlags> {
    public final static String SaveKey = "CorruptionFlags";

    @Override
    public CorruptionFlags onSave() {
        return Cor.flags;
    }

    @Override
    public void onLoad(CorruptionFlags flags) {
        Cor.flags = flags != null ? flags : new CorruptionFlags();
    }
}
