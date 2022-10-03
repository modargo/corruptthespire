package corruptthespire.savables;

import basemod.abstracts.CustomSavable;
import corruptthespire.Cor;
import corruptthespire.CorruptionFlags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SavableCorruptionFlags implements CustomSavable<CorruptionFlags> {
    public final static String SaveKey = "CorruptionFlags";
    private static final Logger logger = LogManager.getLogger(SavableCorruptionFlags.class.getName());

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
            logger.info("flags.seenVault: " + flags.seenVault);
            logger.info("flags.seenHarbinger: " + flags.seenHarbinger);
            logger.info("flags.brokeDevice: " + flags.brokeDevice);
            logger.info("flags.seenServiceShop: " + flags.seenServiceShop);
            logger.info("flags.foughtRottingShambler: " + flags.foughtRottingShambler);
            logger.info("flags.foughtWisps: " + flags.foughtWisps);
            logger.info("flags.foughtHundredSouled: " + flags.foughtHundredSouled);
            logger.info("flags.warAndFear: " + flags.warAndFear);
            logger.info("flags.foughtArchfiend: " + flags.foughtArchfiend);
            logger.info("flags.foughtMaster: " + flags.foughtMaster);
            logger.info("flags.normalMonsterCount: " + flags.normalMonsterCount);
            logger.info("flags.hadFirstCorruptedNormalMonsterFight: " + flags.hadFirstCorruptedNormalMonsterFight);
        }
        Cor.flags = flags != null ? flags : new CorruptionFlags();
    }
}
