package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.relics.corrupted.Gohei;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
public class GoheiPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"r", "damageAmount"})
    public static void callGoheiHook(AbstractPlayer __instance, DamageInfo damageInfo, AbstractRelic r, @ByRef int[] damageAmount) {
        if (r instanceof Gohei) {
            damageAmount[0] = ((Gohei)r).onAttackedAfterToriiBeforeTungstenRod(damageInfo, damageAmount[0]);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "onLoseHpLast");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
