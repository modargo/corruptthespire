package corruptthespire.patches.cards;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import corruptthespire.powers.AbysstouchedPower;
import corruptthespire.powers.PowerUtil;
import corruptthespire.powers.UnnaturalOrderPower;
import corruptthespire.relics.corrupted.AbyssalOrb;
import javassist.*;

import java.text.MessageFormat;

public class UnnaturalOrderAndAbyssalOrbPatch {
    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
    )
    public static class ApplyPowerActionConstructorPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void increasePoisonAbysstouchedAmount(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, @ByRef AbstractPower[] powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
            if (AbstractDungeon.player.hasPower(UnnaturalOrderPower.POWER_ID) && source != null && source.isPlayer && target != source && isPoisonAbysstouched(powerToApply[0].ID)) {
                AbstractPower power = AbstractDungeon.player.getPower(UnnaturalOrderPower.POWER_ID);
                power.flash();
                powerToApply[0].amount += power.amount;
                __instance.amount = powerToApply[0].amount;
            }

            AbstractRelic abyssalOrb = AbstractDungeon.player.getRelic(AbyssalOrb.ID);
            if (abyssalOrb != null && source != null && source.isPlayer && target != source && isAbysstouched(powerToApply[0].ID)) {
                abyssalOrb.flash();
                powerToApply[0].amount += AbyssalOrb.ABYSSTOUCHED_INCREASE;
                __instance.amount = powerToApply[0].amount;
            }

            if (AbstractDungeon.player.hasPower(UnnaturalOrderPower.POWER_ID) && isAbysstouched(powerToApply[0].ID)) {
                PoisonPower p = new PoisonPower(target, source, powerToApply[0].amount);
                ReflectionHacks.setPrivate(__instance, ApplyPowerAction.class, "powerToApply", p);
                powerToApply[0] = p;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class PoisonPowerAtDamageReceivePatch {
        @SpireRawPatch
        public static void addAtDamageReceive(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {

            CtClass poisonPower = ctBehavior.getDeclaringClass().getClassPool().get(PoisonPower.class.getName());

            String methodSource = String.format(
                    "    public float atDamageReceive(float damage, %1$s.DamageType type) {\n" +
                    "        if (%2$s.player.hasPower(%3$s.POWER_ID) && !this.owner.isPlayer) {\n" +
                    "            if (type == %1$s.DamageType.NORMAL) {\n" +
                    "                return damage * (1 + this.amount / 100.0F);\n" +
                    "            }\n" +
                    "        }\n" +
                    "        return damage;\n" +
                    "    }",
                    DamageInfo.class.getName(),
                    AbstractDungeon.class.getName(),
                    UnnaturalOrderPower.class.getName());

            CtMethod atDamageReceive = CtNewMethod.make(methodSource, poisonPower);

            poisonPower.addMethod(atDamageReceive);
        }
    }

    @SpirePatch(clz = PoisonPower.class, method = "updateDescription")
    public static class PoisonPowerUpdateDescriptionPatch {
        @SpirePostfixPatch
        public static void changeDescriptionForUnnaturalOrder(PoisonPower __instance) {
            if (__instance.owner != null && !__instance.owner.isPlayer && AbstractDungeon.player != null && AbstractDungeon.player.hasPower(UnnaturalOrderPower.POWER_ID)) {
                //There's at least one translation that missed this entry, so we just skip doing this if it isn't there
                if (UnnaturalOrderPower.DESCRIPTIONS.length > 1) {
                    __instance.description = MessageFormat.format(AbysstouchedPower.DESCRIPTIONS[1] + " " + UnnaturalOrderPower.DESCRIPTIONS[1], __instance.amount);
                }
            }
        }
    }

    @SpirePatch(clz = PoisonLoseHpAction.class, method = "update")
    public static class PoisonLoseHpActionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"p"})
        public static void skipReduction(PoisonLoseHpAction __instance, @ByRef AbstractPower[] p) {
            if (AbstractDungeon.player.hasPower(UnnaturalOrderPower.POWER_ID)) {
                p[0] = null;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "getPower");
                int[] lines = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                // We need to insert on the line after the call, so add 1
                for (int i = 0; i < lines.length; i++) {
                    lines[i] = lines[i] + 1;
                }
                return lines;
            }
        }
    }


    private static boolean isPoisonAbysstouched(String s) {
        return s.equals(PoisonPower.POWER_ID) || isAbysstouched(s);
    }

    private static boolean isAbysstouched(String s) {
        return s.equals(AbysstouchedPower.POWER_ID) || s.equals(PowerUtil.AbysstouchedPowerId);
    }
}
