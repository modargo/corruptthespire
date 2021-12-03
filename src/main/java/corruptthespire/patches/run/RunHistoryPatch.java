package corruptthespire.patches.run;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryPath;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.RunData;
import javassist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class RunHistoryPatch {
    private static final Logger logger = LogManager.getLogger(RunHistoryPatch.class.getName());
    public static final String ID = "CorruptTheSpire:Corruption";
    private static final String CORRUPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT[0];

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class CorruptionPerFloorField {
        @SpireRawPatch
        public static void addCorruptionPerFloor(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass runData = ctBehavior.getDeclaringClass().getClassPool().get(RunData.class.getName());

            String fieldSource = "public java.util.List corruption_per_floor;";

            CtField field = CtField.make(fieldSource, runData);

            runData.addField(field);
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = SpirePatch.CLASS)
    public static class CorruptionField {
        public static final SpireField<Integer> corruption = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = RunHistoryPath.class, method = "setRunData")
    public static class AddCorruptionDataPatch {
        @SuppressWarnings("rawtypes")
        @SpireInsertPatch(locator = Locator.class, localvars = { "element", "i" })
        public static void addCorruptionData(RunHistoryPath __instance, RunData newData, RunPathElement element, int i) throws NoSuchFieldException, IllegalAccessException {
            Field field = newData.getClass().getField("corruption_per_floor");
            List corruption_per_floor = (List)field.get(newData);
            if (corruption_per_floor != null && i < corruption_per_floor.size()) {
                Object corruption = corruption_per_floor.get(i);
                Integer c = null;
                if (corruption instanceof Double) {
                    c = ((Double) corruption).intValue();
                }
                else if (corruption instanceof Integer) {
                    c = (Integer) corruption;
                }
                else {
                    logger.error("Unrecognized corruption_per_floor data: " + corruption);
                }
                CorruptionField.corruption.set(element, c);
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.NewExprMatcher(RunPathElement.class);
                Matcher finalMatcher = new Matcher.MethodCallMatcher(List.class, "add");
                return LineFinder.findInOrder(ctMethodToPatch, Collections.singletonList(matcher), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = "getTipDescriptionText")
    public static class DisplayCorruptionDataPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "sb" })
        public static void displayCorruptionData(RunPathElement __instance, StringBuilder sb) {
            Integer corruption = CorruptionField.corruption.get(__instance);
            if (corruption != null) {
                if (sb.length() > 0) {
                    sb.append(" NL ");
                }
                sb.append("#p").append(corruption).append(" #p").append(CORRUPTION_TEXT);
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(RunPathElement.class, "eventStats");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
