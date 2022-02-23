package corruptthespire.patches.relics;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.relics.elite.HarbingersClaw;
import javassist.CtBehavior;

import java.util.ArrayList;

public class HarbingersClawPatch {
    private static boolean hasClaw() {
        return AbstractDungeon.player.hasRelic(HarbingersClaw.ID);
    }

    private static float increaseDamage(AbstractCard card, float damage) {
        if (hasClaw() && card.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
            damage = HarbingersClaw.increaseDamage(damage);
            if (card.baseDamage != damage) {
                card.isDamageModified = true;
            }
        }
        return damage;
    }

    private static float increaseDamage(DamageInfo info, float damage) {
        if (info.owner == AbstractDungeon.player) {
            if (hasClaw() && info.type == DamageInfo.DamageType.NORMAL) {
                damage = HarbingersClaw.increaseDamage(damage);
                if (info.base != damage) {
                    info.isModified = true;
                }
            }
        }
        return damage;
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowersSingle {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void Insert(AbstractCard __instance, @ByRef float[] tmp) {
            tmp[0] = increaseDamage(__instance, tmp[0]);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                ArrayList<Matcher> matchers = new ArrayList<>();
                matchers.add(finalMatcher);
                return LineFinder.findInOrder(ctMethodToPatch, matchers, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowersMulti {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp", "i"})
        public static void Insert(AbstractCard __instance, float[] tmp, int i) {
            tmp[i] = increaseDamage(__instance, tmp[i]);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                ArrayList<Matcher> matchers = new ArrayList<>();
                matchers.add(finalMatcher);
                matchers.add(finalMatcher);
                return LineFinder.findInOrder(ctMethodToPatch, matchers, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class CalculateCardSingle {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void Insert(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = increaseDamage(__instance, tmp[0]);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                ArrayList<Matcher> matchers = new ArrayList<>();
                matchers.add(finalMatcher);
                return LineFinder.findInOrder(ctMethodToPatch, matchers, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class CalculateCardMulti {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp", "i"})
        public static void Insert(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i) {
            tmp[i] = increaseDamage(__instance, tmp[i]);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                ArrayList<Matcher> matchers = new ArrayList<>();
                matchers.add(finalMatcher);
                matchers.add(finalMatcher);
                matchers.add(finalMatcher);
                return LineFinder.findInOrder(ctMethodToPatch, matchers, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=DamageInfo.class,
            method="applyPowers"
    )
    public static class DamageInfoApplyPowers
    {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void Insert(DamageInfo __instance, AbstractCreature owner, AbstractCreature target, @ByRef float[] tmp) {
            tmp[0] = increaseDamage(__instance, tmp[0]);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(cls = "eatyourbeets.cards.base.EYBCard", method = "UpdateDamage", optional = true)
    public static class AnimatorHarbingersClawPatch {
        @SpirePrefixPatch
        public static void ApplyDamageIncrease(AbstractCard __instance, @ByRef float[] amount) {
            amount[0] = increaseDamage(__instance, amount[0]);
            // Might as well fix Hubris's Pure Nail (reforged Old Nail) while we're at it
            AbstractRelic hubrisOldNail = AbstractDungeon.player.getRelic("hubris:OldNail");
            if (hubrisOldNail != null && hubrisOldNail.counter == -42 && __instance.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
                amount[0] *= 2;
            }
        }
    }
}
