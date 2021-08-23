package corruptthespire.patches.shop;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CtBehavior;

@SpirePatch(clz = StoreRelic.class, method = "update")
public class StoreRelicUpdatePatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void FixPositionForSecondRow(StoreRelic __instance, float rugY, int ___slot) {
        if (___slot > 2) {
            __instance.relic.currentX -= 150.0F * 3 * Settings.xScale;
            __instance.relic.currentY -= 200.0F * Settings.yScale;
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "move");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
