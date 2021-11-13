package corruptthespire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

@SpirePatch(clz = basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderCustomDynamicVariable.Inner.class, method = "myRenderDynamicVariable", paramtypez = {Object.class, String.class, char.class, float.class, float.class, int.class, BitmapFont.class, SpriteBatch.class, Character.class})
@SpirePatch(clz = basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.RenderCustomDynamicVariable.Inner.class, method = "myRenderDynamicVariable", paramtypez = {Object.class, String.class, char.class, float.class, float.class, int.class, BitmapFont.class, SpriteBatch.class, Character.class})
public class RenderPercentSignInCardTextPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"end", "stringBuilder"})
    public static void includePercentSignInDynamicText(Object __instance, String key, char ckey, float start_x, float draw_y, int i, BitmapFont font, SpriteBatch sb, Character cend, @ByRef String[] end, StringBuilder stringBuilder) {
        AbstractCard card = __instance instanceof AbstractCard ? (AbstractCard)__instance : ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
        if (card.cardID.startsWith("CorruptTheSpire:") && end[0].equals("%")) {
            stringBuilder.append(end[0]);
            end[0] = "";
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
