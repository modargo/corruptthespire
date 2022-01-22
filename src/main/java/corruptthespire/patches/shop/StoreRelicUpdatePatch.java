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
        // This is deliberately the exact same as Spicy Shops, to maintain consistency and avoid issues
        int relicRow = (___slot + 1) / 3 - (((___slot + 1) % 3 > 0) ? 0 : 1);
        __instance.relic.currentX = 1000.0F * Settings.scale + 150.0F * (___slot - 3 * relicRow) * Settings.scale;
        __instance.relic.currentY = rugY + (418.0F - 128.0F * relicRow) * Settings.scale;

    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "move");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
