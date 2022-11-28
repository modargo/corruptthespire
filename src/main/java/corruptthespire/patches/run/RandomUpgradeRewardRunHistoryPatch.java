package corruptthespire.patches.run;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryPath;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.RunData;
import corruptthespire.savables.logs.RandomUpgradeRewardPerFloorLog;
import javassist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomUpgradeRewardRunHistoryPatch {
    private static final Logger logger = LogManager.getLogger(RandomUpgradeRewardRunHistoryPatch.class.getName());
    private static final String[] TOOLTIP_TEXT = CardCrawlGame.languagePack.getUIString("RunHistoryPathNodes").TEXT;
    private static final String TEXT_UPGRADED = TOOLTIP_TEXT[43];

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class RandomUpgradeRewardPerFloorField {
        @SpireRawPatch
        public static void addRandomUpgradeRewardPerFloor(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass runData = ctBehavior.getDeclaringClass().getClassPool().get(RunData.class.getName());

            String fieldSource = "public java.util.List random_upgrade_reward_per_floor;";

            CtField field = CtField.make(fieldSource, runData);

            runData.addField(field);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
    public static class NextRoomTransitionAddRandomUpgradeRewardEntryPatch {
        @SpirePrefixPatch
        public static void nextRoomTransitionAddRandomUpgradeRewardEntryPatch(AbstractDungeon __instance, SaveFile saveFile) {
            boolean isLoadingSave = CardCrawlGame.loadingSave && saveFile != null;
            if (!isLoadingSave) {
                if (RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog != null) {
                    RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog.add(new ArrayList<>());
                }
            }
        }
    }

    @SpirePatch(clz = Metrics.class, method = "gatherAllData")
    public static class GatherAllDataPatch {
        @SpirePostfixPatch
        public static void gatherAllDataPatch(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters) {
            ReflectionHacks.privateMethod(Metrics.class, "addData", Object.class, Object.class)
                    .invoke(__instance, "random_upgrade_reward_per_floor", RandomUpgradeRewardPerFloorLog.randomUpgradeRewardPerFloorLog);
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = SpirePatch.CLASS)
    public static class RandomUpgradeRewardsField {
        public static final SpireField<List<String>> randomUpgradeRewards = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = RunHistoryPath.class, method = "setRunData")
    public static class AddRandomUpgradeRewardDataPatch {
        @SuppressWarnings({"rawtypes", "unchecked"})
        @SpireInsertPatch(locator = Locator.class, localvars = { "element", "i" })
        public static void addRandomUpgradeRewardData(RunHistoryPath __instance, RunData newData, RunPathElement element, int i) throws NoSuchFieldException, IllegalAccessException {
            Field field = newData.getClass().getField("random_upgrade_reward_per_floor");
            List random_upgrade_reward_per_floor = (List)field.get(newData);
            if (random_upgrade_reward_per_floor != null && i < random_upgrade_reward_per_floor.size()) {
                Object randomUpgradeRewards = random_upgrade_reward_per_floor.get(i);
                if (randomUpgradeRewards instanceof List) {
                    RandomUpgradeRewardsField.randomUpgradeRewards.set(element, (List<String>)randomUpgradeRewards);
                }
                else {
                    logger.warn("Unrecognized random_upgrade_reward_per_floor data");
                }
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
    public static class DisplayRandomUpgradeRewardPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "sb" })
        public static void displayRandomUpgradeRewardPatch(RunPathElement __instance, StringBuilder sb) {
            List<String> randomUpgradeRewards = RandomUpgradeRewardsField.randomUpgradeRewards.get(__instance);
            if (randomUpgradeRewards != null) {
                for (String cardID : randomUpgradeRewards) {
                    sb.append(" NL ").append(String.format(TEXT_UPGRADED, CardLibrary.getCardNameFromMetricID(cardID)));
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(RunPathElement.class, "shopPurges");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
