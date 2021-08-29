package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;
import corruptthespire.CorruptionFlags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SavableCorruptionFlags implements CustomSavable<CorruptionFlags> {
    public final static String SaveKey = "CorruptionFlags";
    public static final Logger logger = LogManager.getLogger(SavableCorruptionFlags.class.getName());

    @Override
    public CorruptionFlags onSave() {
        return Cor.flags;
    }

    @Override
    public void onLoad(CorruptionFlags flags) {
        if (flags != null) {
            logger.info("flags.seenTreasureWardens: " + flags.seenTreasureWardens);
            logger.info("flags.seenSealedChest: " + flags.seenSealedChest);
            logger.info("flags.openedSealedChest: " + flags.openedSealedChest);
        }
        Cor.flags = flags != null ? flags : new CorruptionFlags();
    }
}
