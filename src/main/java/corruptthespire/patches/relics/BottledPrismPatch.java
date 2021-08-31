package corruptthespire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.events.shrines.Duplicator;
import com.megacrit.cardcrawl.relics.DollysMirror;
import corruptthespire.relics.corrupted.BottledPrism;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class BottledPrismPatch {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class InBottledPrismField {
        public static SpireField<Boolean> inBottlePrism = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class AbstractCard_makeStatEquivalentCopy {
        @SpirePostfixPatch
        public static AbstractCard patch(AbstractCard __return, AbstractCard __this) {
            InBottledPrismField.inBottlePrism.set(__return, InBottledPrismField.inBottlePrism.get(__this));
            return __return;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "bottledCardUpgradeCheck", paramtypez = {AbstractCard.class})
    public static class AbstractPlayer_bottledCardUpgradeCheck {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __this, AbstractCard c) {
            if (InBottledPrismField.inBottlePrism.get(c) && __this.hasRelic(BottledPrism.ID)) {
                ((BottledPrism) __this.getRelic(BottledPrism.ID)).setDescriptionAfterLoading();
            }
        }
    }

    public static boolean isInBottlePrism(AbstractCard c) {
        return InBottledPrismField.inBottlePrism.get(c);
    }

    @SpirePatch(clz = CardGroup.class, method = "initializeDeck", paramtypez = {CardGroup.class})
    public static class AbstractGroup_initializeDeck {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    // Transforms `!inBottleTornado` into `!(inBottleTornado || inBottlePrism)`
                    // Logically equivalent to `!inBottleTornado && !inBottlePrism`
                    if (fieldAccess.getClassName().equals(AbstractCard.class.getName()) && fieldAccess.getFieldName().equals("inBottleTornado")) {
                        fieldAccess.replace("{ $_ = ($proceed() || corruptthespire.patches.relics.BottledPrismPatch.isInBottlePrism(c)); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = Duplicator.class, method = "update")
    public static class Duplicator_update {
        @SpireInsertPatch(locator = AbstractCard_InBottleTornado_Locator.class, localvars = "c")
        public static void patch(Duplicator __this, AbstractCard c) {
            InBottledPrismField.inBottlePrism.set(c, false);
        }
    }

    @SpirePatch(clz = DollysMirror.class, method = "update")
    public static class DollysMirror_update {
        @SpireInsertPatch(locator = AbstractCard_InBottleTornado_Locator.class, localvars = "c")
        public static void patch(DollysMirror __this, AbstractCard c) {
            InBottledPrismField.inBottlePrism.set(c, false);
        }
    }

    public static class AbstractCard_InBottleTornado_Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "inBottleTornado");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}