package corruptthespire.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import corruptthespire.cards.Contagion;
import javassist.CtBehavior;

@SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { AbstractCard.class, AbstractCreature.class })
public class ContagionDontExhaustPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void contagionDontExhaust(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
        if (card.cardID.equals(Contagion.ID) && card.dontTriggerOnUseCard) {
            __instance.exhaustCard = false;
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(UseCardAction.class, "setValues");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
