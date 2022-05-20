package corruptthespire.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.Turnip;
import corruptthespire.relics.corrupted.BeanSprouts;
import javassist.CtBehavior;

public class BeanSproutsPatch {
    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class StackPowerPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void handleStackPower(ApplyPowerAction __instance) {
            checkAndTrigger(__instance);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "stackPower");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class OnInitialApplicationPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void handleStackPower(ApplyPowerAction __instance) {
            checkAndTrigger(__instance);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "onInitialApplication");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static void checkAndTrigger(ApplyPowerAction apa) {
        AbstractCreature target = apa.target;
        AbstractCreature source = apa.source;
        AbstractPower power = ReflectionHacks.getPrivate(apa, ApplyPowerAction.class, "powerToApply");
        BeanSprouts beanSprouts = (BeanSprouts)AbstractDungeon.player.getRelic(BeanSprouts.ID);
        if (beanSprouts != null && target == AbstractDungeon.player
                && (power.ID.equals(FrailPower.POWER_ID) || power.ID.equals(VulnerablePower.POWER_ID) || (power.ID.equals(DexterityPower.POWER_ID) && power.amount < 0 && !source.isPlayer))) {
            beanSprouts.trigger();
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class TurnipPatch {
        @SpirePrefixPatch
        public static void handleTurnip(ApplyPowerAction __instance) {
            if (__instance.target == null || __instance.target.isDeadOrEscaped() || __instance.isDone || __instance.target != AbstractDungeon.player) {
                return;
            }
            float startingDuration = ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "startingDuration");
            float duration = ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
            if (duration != startingDuration) {
                return;
            }

            AbstractPower power = ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            if (power.ID.equals(FrailPower.POWER_ID) && AbstractDungeon.player.hasRelic(Turnip.ID)) {
                BeanSprouts relic = (BeanSprouts)AbstractDungeon.player.getRelic(BeanSprouts.ID);
                if (relic != null) {
                    relic.trigger();
                }
            }
        }
    }
}