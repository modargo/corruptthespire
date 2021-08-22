package corruptthespire.patches.shop;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import corruptthespire.corruptions.shop.ShopCorruption;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ShopPatch {
    @SpirePatch(clz = Merchant.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {float.class, float.class, int.class})
    public static class ChangeCardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void changeCards(Merchant __instance, float x, float y, int newShopScreen) {
            ArrayList<AbstractCard> coloredCards = ReflectionHacks.getPrivate(__instance, Merchant.class, "cards1");
            ArrayList<AbstractCard> colorlessCards = ReflectionHacks.getPrivate(__instance, Merchant.class, "cards2");
            ShopCorruption.handleCards(coloredCards, colorlessCards);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ShopScreen.class, "init");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "initRelics")
    public static class ChangeRelicsPatch {
        @SpirePrefixPatch
        public static SpireReturn changeRelics(ShopScreen __instance) {
            if (ShopCorruption.handleRelics(__instance)) {
                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}
