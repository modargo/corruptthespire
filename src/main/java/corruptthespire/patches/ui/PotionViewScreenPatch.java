package corruptthespire.patches.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.screens.compendium.PotionViewScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import corruptthespire.potions.PotionUtil;
import javassist.CtBehavior;

import java.util.ArrayList;

public class PotionViewScreenPatch {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:PotionViewScreen").TEXT;
    private static final ArrayList<AbstractPotion> allCorruptedPotions = PotionUtil.getAllCorruptedPotions();

    @SpirePatch(clz = PotionViewScreen.class, method = "render")
    public static class PotionViewScreenRenderPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "sb" })
        public static void renderCorruptedPotionsList(PotionViewScreen __instance, SpriteBatch sb) {
            ReflectionHacks.RMethod m = ReflectionHacks.privateMethod(PotionViewScreen.class, "renderList", SpriteBatch.class, String.class, String.class, ArrayList.class);
            m.invoke(__instance, sb, TEXT[0], TEXT[1], allCorruptedPotions);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MenuCancelButton.class, "render");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = PotionViewScreen.class, method = "update")
    public static class PotionViewScreenUpdatePatch {
        @SpirePostfixPatch
        public static void updateCorruptedPotionsList(PotionViewScreen __instance) {
            ReflectionHacks.RMethod m = ReflectionHacks.privateMethod(PotionViewScreen.class, "updateList", ArrayList.class);
            m.invoke(__instance, allCorruptedPotions);
        }
    }
}
