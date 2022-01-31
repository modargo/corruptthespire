package corruptthespire.patches.core;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import corruptthespire.Cor;
import javassist.CtBehavior;

@SpirePatch(clz = AwakenedOne.class, method = "changeState")
public class ApplyCorruptionHealthIncreaseAwakenedOneSecondPhasePatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void handleAwakenedOne(AwakenedOne __instance, String key) {
        __instance.maxHealth += (__instance.maxHealth * Cor.getCorruptionDamageMultiplierPercent()) / 100;
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
