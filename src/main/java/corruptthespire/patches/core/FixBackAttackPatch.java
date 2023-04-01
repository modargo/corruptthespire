package corruptthespire.patches.core;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import corruptthespire.Cor;
import javassist.CtBehavior;

// The base game has a subtle inconsistency with Back Attack (used by Spire Spear and Spire Shield, and also by the
// Relentless War and Eternal Fear bosses added by Corrupt the Spire), which this patch fixes.
// In Slay the Spire, the value shown as the intent damage and the actual damage done are calculated by two different
// pieces of code. Intent damage is calculated by AbstractMonster.calculateDamage, while the actual damage done is
// calculated by AbstractMonster.applyPowers, based on the DamageInfos set up for the monster.
// AbstractMonster.calculateDamage applies the various increases, then after AbstractStance.atDamageReceive but before
// AbstractPower.atDamageFinalGive/atDamageFinalReceive, it applies back attack and takes the floor (by casting to int).
// Then it applies the rest of the increases and takes the floor.
// AbstractMonster.applyPowers (by calling DamageInfo.applyPowers) applies all the increases, including atDamageFinalX
// methods, then takes the floor, then applies back attack and takes the floor.
// The difference in when back attack is applied means that if the various increases produce a fractional part, the two
// calculations may differ by 1 (if the fractional part is large enough that the 1.5x multiplier from Back Attack would
// result in an extra damage).
// This never gets noticed in the base game because the damage multipliers available are only Weak and Vulnerable, which
// have the
// We fix this by making the actual damage calculation (AbstractMonster.applyPowers/DamageInfo.applyPowers) use the same
// calculation order as the intent damage calculation, by disabling the multiplication for Back Attack in AbstractMonster
// and doing it in DamageInfo instead (with the same ordering relative to other increases as AbstractMonster.calculateDamage).
// This is something that the Slay the Spire devs opted not to fix and that the modding community decided not to include
// in BaseMod (a patch that increases enemy damage would be negative for enough people that it isn't worth it). But it's
// fine to have it here for Corrupt the Spire.
public class FixBackAttackPatch {
    @SpirePatch(
            clz = DamageInfo.class,
            method = SpirePatch.CLASS
    )
    public static class ApplyBackAttackField {
        public static final SpireField<Boolean> applyBackAttack = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractMonster.class, method = "applyPowers", paramtypez = {})
    public static class AbstractMonsterPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"applyBackAttack"})
        public static void adjustBackAttackHandling(AbstractMonster __instance, @ByRef boolean[] applyBackAttack) {
            if (Cor.active && applyBackAttack[0]) {
                applyBackAttack[0] = false;
                for (DamageInfo di : __instance.damage) {
                    ApplyBackAttackField.applyBackAttack.set(di, true);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(DamageInfo.class, "applyPowers");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = DamageInfo.class, method = "applyPowers", paramtypez = { AbstractCreature.class, AbstractCreature.class })
    public static class DamageInfoPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void adjustBackAttackHandling(DamageInfo __instance, AbstractCreature owner, AbstractCreature target, @ByRef float[] tmp) {
            if (ApplyBackAttackField.applyBackAttack.get(__instance)) {
                ApplyBackAttackField.applyBackAttack.set(__instance, false);
                tmp[0] = (int)(tmp[0] * 1.5F);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "atDamageFinalGive");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
