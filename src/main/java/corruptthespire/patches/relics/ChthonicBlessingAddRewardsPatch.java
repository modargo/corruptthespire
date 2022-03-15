package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import corruptthespire.relics.chaotic.ChthonicBlessing;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractRoom.class, method = "update")
public class ChthonicBlessingAddRewardsPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void addRewards(AbstractRoom __instance) {
        ChthonicBlessing r = (ChthonicBlessing)AbstractDungeon.player.getRelic(ChthonicBlessing.ID);
        if (r != null) {
            r.addRewards();
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
