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
import corruptthespire.powers.AbysstouchedPower;
import corruptthespire.powers.PowerUtil;
import corruptthespire.powers.UnnaturalOrderPower;
import javassist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnnaturalOrderPatch {
    private static final Logger logger = LogManager.getLogger(UnnaturalOrderPatch.class.getName());

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
    )
    public static class ApplyPowerActionConstructorPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void increasePoisonAbysstouchedAmount(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
            if (AbstractDungeon.player.hasPower(UnnaturalOrderPower.POWER_ID) && source != null && source.isPlayer && target != source && isPoisonAbysstouched(powerToApply.ID)) {
                AbstractPower power = AbstractDungeon.player.getPower(UnnaturalOrderPower.POWER_ID);
                power.flash();
                powerToApply.amount += power.amount;
                __instance.amount = powerToApply.amount;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        @SpirePostfixPatch
        public static void convertAbysstouchedToPoison(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect, @ByRef AbstractPower[] ___powerToApply) {
            logger.info("Checking whether to convert abysstouched to poison");
            if (AbstractDungeon.player.hasPower(UnnaturalOrderPower.POWER_ID) && isAbysstouched(___powerToApply[0].ID)) {
                logger.info("Converting abysstouched to poison");
                AbstractPower p = ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
                ReflectionHacks.setPrivate(__instance, ApplyPowerAction.class, "powerToApply", new PoisonPower(target, source, p.amount));
                p = ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
                logger.info("p.ID: " + p.ID + ", p.amount:" + p.amount);
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
