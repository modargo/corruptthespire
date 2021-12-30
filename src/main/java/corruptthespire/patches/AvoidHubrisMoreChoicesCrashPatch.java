package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch(clz = AbstractDungeon.class, method = "returnRandomRelicKey")
public class AvoidHubrisMoreChoicesCrashPatch {
    private static final Logger logger = LogManager.getLogger(AvoidHubrisMoreChoicesCrashPatch.class.getName());

    @SpirePrefixPatch
    public static SpireReturn<String> checkForSpecialRelicTier(AbstractRelic.RelicTier tier) {
        // The More Choices mod has code (in MoreChoices.setupPotionsAndRelicsRewardList) that adds alternate relics
        // for anything on the combat reward screen. That code calls returnRandomRelicKey with the tier of the relic.
        // However, that method isn't meant to handle Special tier relics, and returns null. From there the behavior is
        // effectively undefined.
        // Without other mods running, code downstream catches it and returns Circlet for that case. But Hubris's
        // ZylophonePatch.AvoidIfNoAppropriateCards.Postfix patches that method, and assumes the return value isn't
        // null. Violating that assumption is the direct cause of the crash.
        // We could also patch returnRandomRelicKeyEnd but avoid that on the grounds of keeping this limited.
        if (Loader.isModLoaded("MoreChoices") && tier == AbstractRelic.RelicTier.SPECIAL) {
            logger.info("Returning null for returnRandomRelicKey call with RelicTier.Special");
            return SpireReturn.Return(Circlet.ID);
        }
        return SpireReturn.Continue();
    }
}
