package corruptthespire.patches.run;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.CampfireChoice;
import corruptthespire.corruptions.campfire.FireRitualOption;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.text.MessageFormat;

public class FireRitualRunHistoryPatch {
    private static final String FIRE_RITUAL_TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:FireRitualRunHistory").TEXT[0];

    @SpirePatch(clz = RunPathElement.class, method = "getTipDescriptionText")
    public static class DisplayFireRitualCampfireChoiceDataPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "sb" })
        public static void displayCorruptionData(RunPathElement __instance, StringBuilder sb) {
            CampfireChoice campfireChoice = ReflectionHacks.getPrivate(__instance, RunPathElement.class, "campfireChoice");
            if (campfireChoice != null && campfireChoice.key.equals(FireRitualOption.METRIC_KEY)) {
                if (sb.length() > 0) {
                    sb.append(" NL ");
                }
                sb.append(MessageFormat.format(FIRE_RITUAL_TEXT, campfireChoice.data));
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(RunPathElement.class, "campfireChoice");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = "getTipDescriptionText")
    public static class SkipExitingCampfireChoiceLogicPatch extends ExprEditor {
        @Override
        public void edit(FieldAccess fieldAccess) throws CannotCompileException {
            if (fieldAccess.getClassName().equals(RunPathElement.class.getName()) && fieldAccess.getFieldName().equals("campfireChoice")) {
                fieldAccess.replace(String.format("{ $_ = this.campfireChoice != null && this.campfireChoice.key.equals(\"%1$s\") ? null : this.campfireChoice; }", FireRitualOption.METRIC_KEY));
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor skipExitingCampfireChoiceLogicPatch() {
            return new SkipExitingCampfireChoiceLogicPatch();
        }
    }
}
