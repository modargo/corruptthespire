package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import corruptthespire.Cor;
import javassist.CtBehavior;

public class CorruptionDamageIncreasePatch {
    @SpirePatch(clz = DamageInfo.class, method = "applyPowers")
    public static class DamageInfoPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void CorruptionDamageIncrease(DamageInfo __instance, AbstractCreature owner, AbstractCreature target, @ByRef float[] tmp) {
            tmp[0] = tmp[0] * (1.0F + (Cor.getCorruptionDamageMultiplierPercent() / 100.0F));
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "atDamageReceive");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
    public static class AbstractMonsterPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void CorruptionDamageIncrease(AbstractMonster __instance, int dmg, @ByRef float[] tmp) {
            tmp[0] = tmp[0] * (1.0F + (Cor.getCorruptionDamageMultiplierPercent() / 100.0F));
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "atDamageReceive");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
