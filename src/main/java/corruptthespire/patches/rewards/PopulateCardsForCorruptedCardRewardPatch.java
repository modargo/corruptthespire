package corruptthespire.patches.rewards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import corruptthespire.rewards.CorruptedCardReward;
import javassist.CtBehavior;

@SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
public class PopulateCardsForCorruptedCardRewardPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void populateCardsForCorruptedCardReward(CombatRewardScreen __instance) {
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
