package corruptthespire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.compendium.RelicViewScreen;
import corruptthespire.Cor;
import corruptthespire.relics.corrupted.AbstractCorruptedRelic;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Collections;

public class RelicViewScreenPatch {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:RelicViewScreen").TEXT;
    private static final ArrayList<AbstractCorruptedRelic> allCorruptedRelics = Cor.getAllCorruptedRelics();
    static {
        for (AbstractCorruptedRelic r : allCorruptedRelics) {
            r.isSeen = true;
        }
        Collections.sort(allCorruptedRelics);
    }

    @SpirePatch(clz = RelicLibrary.class, method = "addToTierList")
    public static class SkipCorruptedRelicsPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> skipCorruptedRelics(AbstractRelic relic) {
            if (relic instanceof AbstractCorruptedRelic) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "render")
    public static class RelicViewScreenRenderPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "sb" })
        public static void renderCorruptedRelicsList(RelicViewScreen __instance, SpriteBatch sb) {
            ReflectionHacks.RMethod m = ReflectionHacks.privateMethod(RelicViewScreen.class, "renderList", SpriteBatch.class, String.class, String.class, ArrayList.class);
            m.invoke(__instance, sb, TEXT[0], TEXT[1], allCorruptedRelics);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(RelicLibrary.class, "shopList");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = RelicViewScreen.class, method = "update")
    public static class RelicViewScreenUpdatePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void updateCorruptedRelicsList(RelicViewScreen __instance) {
            ReflectionHacks.RMethod m = ReflectionHacks.privateMethod(RelicViewScreen.class, "updateList", ArrayList.class);
            m.invoke(__instance, allCorruptedRelics);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(RelicLibrary.class, "shopList");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
