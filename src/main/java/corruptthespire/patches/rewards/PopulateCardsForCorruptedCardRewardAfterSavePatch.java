package corruptthespire.patches.rewards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import corruptthespire.rewards.CorruptedCardReward;
import javassist.CtBehavior;

// See the comment in PopulateCardsForCorruptedCardRewardAfterSavePatch for why this is needed
@SpirePatch(clz = AbstractRoom.class, method = "update")
public class PopulateCardsForCorruptedCardRewardAfterSavePatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void populateCardsForCorruptedCardReward(AbstractRoom __instance) {
        if (AbstractDungeon.combatRewardScreen == null) {
            return;
        }
        for (RewardItem rewardItem : AbstractDungeon.combatRewardScreen.rewards) {
            if (rewardItem instanceof CorruptedCardReward) {
                CorruptedCardReward ccr = (CorruptedCardReward)rewardItem;
                ccr.populateCards();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(SaveAndContinue.class, "save");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
