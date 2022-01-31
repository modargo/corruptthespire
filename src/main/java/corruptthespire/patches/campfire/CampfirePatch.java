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
import corruptthespire.patches.core.CorruptedField;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class CampfirePatch {
    @SpirePatch(clz = CampfireUI.class, method = SpirePatch.CONSTRUCTOR)
    public static class InitializeCorruptedCampfireButtonsExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(CampfireUI.class.getName()) && methodCall.getMethodName().equals("initializeButtons")) {
                methodCall.replace(String.format("{ if (!%1$s.handleInitializeButtons(this)) { $proceed($$); } }", CampfireCorruption.class.getName()));
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor initializeCorruptedCampfireButtons() {
            return new InitializeCorruptedCampfireButtonsExprEditor();
        }
    }

    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class InitializeButtonsPatch {
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
