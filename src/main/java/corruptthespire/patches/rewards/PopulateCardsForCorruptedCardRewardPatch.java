package corruptthespire.patches.rewards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import corruptthespire.rewards.CorruptedCardReward;
import javassist.CtBehavior;

@SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
public class PopulateCardsForCorruptedCardRewardPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void populateCardsForCorruptedCardReward(CombatRewardScreen __instance) {
        // The loading save checks wouldn't be needed for a normal card reward, since there's handling for card RNG values
        // to make them work both normally and when loading a post-combat save. However, Corrupt the Spire uses a separate
        // RNG for corrupted card rewards, so we have to do our own handling of this to avoid save/reload instability.
        // Specifically, if these conditions are true we know that we're able to make a save file, and we defer populating
        // the cards until right after that (see PopulateCardsForCorruptedCardRewardAfterSavePatch).
        // Anyone looking at this as an example of getting custom card rewards to be stable on save/reload can copy the
        // general structure but leave out this check.
        if (!CardCrawlGame.loadingSave && !AbstractDungeon.loading_post_combat) {
            return;
        }
        for (RewardItem rewardItem : __instance.rewards) {
            if (rewardItem instanceof CorruptedCardReward) {
                CorruptedCardReward ccr = (CorruptedCardReward)rewardItem;
                ccr.populateCards();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ProceedButton.class, "show");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
