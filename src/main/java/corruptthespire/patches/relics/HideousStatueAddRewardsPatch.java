package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.corruptions.fight.FightCorruption;
import corruptthespire.relics.corrupted.HideousStatue;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractRoom.class, method = "update")
public class HideousStatueAddRewardsPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void addRewards(AbstractRoom __instance) {
        HideousStatue.handleRewards();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "addPotionToRewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
