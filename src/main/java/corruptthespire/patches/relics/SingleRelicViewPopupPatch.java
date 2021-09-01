package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import corruptthespire.relics.corrupted.AbstractCorruptedRelic;

@SpirePatch(clz = SingleRelicViewPopup.class, method = "generateRarityLabel")
public class SingleRelicViewPopupPatch {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:CorruptedRelic").TEXT;

    @SpirePostfixPatch
    public static void setCorruptedRarity(SingleRelicViewPopup __instance, AbstractRelic ___relic, @ByRef String[] ___rarityLabel) {
        if (___relic instanceof AbstractCorruptedRelic) {
            ___rarityLabel[0] = TEXT[0];
        }
    }
}
