package corruptthespire.patches.fight;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.corruptions.fight.FightCorruption;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractRoom.class, method = "update")
public class FightCorruptionAddRewardsPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void addRewards(AbstractRoom __instance) {
        if (FightCorruption.shouldApplyCorruptions()) {
            FightCorruption.addRewards();
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "addPotionToRewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
