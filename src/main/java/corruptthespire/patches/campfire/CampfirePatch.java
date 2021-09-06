package corruptthespire.patches.campfire;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import corruptthespire.corruptions.campfire.CampfireCorruption;
import corruptthespire.corruptions.campfire.CampfireInfo;
import corruptthespire.corruptions.campfire.FireRitualOption;
import corruptthespire.patches.CorruptedField;
import javassist.CtBehavior;

import java.util.ArrayList;

public class CampfirePatch {
    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class InitializeButtonsPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> InitializeCorruptedCampfireButtons(CampfireUI __instance) {
            if (!CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
                return SpireReturn.Continue();
            }
            CampfireInfo campfireInfo = CampfireInfoField.campfireInfo.get(__instance);
            if (campfireInfo.isDone) {
                return SpireReturn.Continue();
            }
            CampfireCorruption.initializeCampfireInfo(campfireInfo);
            ArrayList<AbstractCampfireOption> buttons = ReflectionHacks.getPrivate(__instance, CampfireUI.class, "buttons");
            buttons.addAll(campfireInfo.options);
            return SpireReturn.Return();
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void AddFireRitualOption(CampfireUI __instance) {
            if (CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
                ArrayList<AbstractCampfireOption> buttons = ReflectionHacks.getPrivate(__instance, CampfireUI.class, "buttons");
                buttons.add(new FireRitualOption());
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(SmithOption.class);
                int[] lines = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                // We need to insert on the line after the call, so add 1
                for (int i = 0; i < lines.length; i++) {
                    lines[i] = lines[i] + 1;
                }
                return lines;
            }
        }
    }

    @SpirePatch(clz = CampfireUI.class, method = "getCampMessage")
    public static class GetCampMessagePatch {
        @SpirePrefixPatch
        public static SpireReturn<String> InitializeCorruptedCampfireMessage(CampfireUI __instance) {
            if (!CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
                return SpireReturn.Continue();
            }
            CampfireInfo campfireInfo = CampfireInfoField.campfireInfo.get(__instance);
            if (campfireInfo.isDone) {
                return SpireReturn.Continue();
            }

            return SpireReturn.Return(CampfireCorruption.TEXT[0]);
        }
    }
}
