package corruptthespire.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import corruptthespire.cards.ShimmeringShield;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

@SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
public class FineTuneDescriptionDisplayPatch {
    public static final float WIDTH_ADJUSTMENT = 20.0f;
    public static boolean addWidthBack = false;

    private static boolean matches(AbstractCard card, String word) {
        return card.cardID.equals(ShimmeringShield.ID) && word.equals("corruptthespire:different\u00a0color");
    }

    @SpireInsertPatch(locator = Locator.class, localvars = {"word", "currentWidth"})
    public static void fineTuneLineWidth(AbstractCard __instance, String word, @ByRef float[] currentWidth) {
        if (matches(__instance, word)) {
            currentWidth[0] = currentWidth[0] - WIDTH_ADJUSTMENT;
            FineTuneDescriptionDisplayPatch.addWidthBack = true;
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(String.class, "toLowerCase");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    public static class FineTuneDescriptionDisplayExprEditor extends ExprEditor {
        @Override
        public void edit(NewExpr newExpr) throws CannotCompileException {
            if (newExpr.getClassName().equals(DescriptionLine.class.getName())) {
                newExpr.replace(String.format("{ if(%1$s.addWidthBack) { $2 += %1$s.WIDTH_ADJUSTMENT; %1$s.addWidthBack = false; }; $_ = $proceed($$); }", FineTuneDescriptionDisplayPatch.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor fineTuneDescriptionDisplay() {
        return new FineTuneDescriptionDisplayExprEditor();
    }
}
