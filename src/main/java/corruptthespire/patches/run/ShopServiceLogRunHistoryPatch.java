package corruptthespire.patches.run;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryPath;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.RunData;
import corruptthespire.savables.logs.ShopServiceLog;
import javassist.*;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShopServiceLogRunHistoryPatch {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:ShopServiceRunHistory").TEXT;
    private static final String[] TOOLTIP_TEXT = CardCrawlGame.languagePack.getUIString("RunHistoryPathNodes").TEXT;
    private static final String TEXT_OBTAIN_HEADER = TOOLTIP_TEXT[18];
    private static final String TEXT_OBTAIN_TYPE_CARD = TOOLTIP_TEXT[22];

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class ShopServiceLogRunDataField {
        @SpireRawPatch
        public static void addShopServiceLog(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass runData = ctBehavior.getDeclaringClass().getClassPool().get(RunData.class.getName());

            String fieldSource = "public java.util.List shop_service_log;";

            CtField field = CtField.make(fieldSource, runData);

            runData.addField(field);
        }
    }

    @SpirePatch(clz = Metrics.class, method = "gatherAllData")
    public static class GatherAllDataPatch {
        @SpirePostfixPatch
        public static void gatherAllDataPatch(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters) {
            ReflectionHacks.privateMethod(Metrics.class, "addData", Object.class, Object.class)
                    .invoke(__instance, "shop_service_log", ShopServiceLog.shopServiceLog);
        }
    }

    @SpirePatch(clz = RunHistoryScreen.class, method = "refreshData")
    public static class ShopServiceLogRefreshDataPatch {
        @SuppressWarnings("unchecked")
        @SpireInsertPatch(locator = Locator.class, localvars = { "data" })
        public static void shopServiceLogRefreshData(RunHistoryScreen __instance, RunData data) throws NoSuchFieldException, IllegalAccessException {
            Field field = data.getClass().getField("shop_service_log");
            List<LinkedTreeMap<String, Object>> shop_service_log = (List<LinkedTreeMap<String, Object>>)field.get(data);
            if (shop_service_log == null) {
                return;
            }
            List<ShopServiceLog> l = new ArrayList<>();
            for (LinkedTreeMap<String, Object> ltm : shop_service_log) {
                ShopServiceLog log = new ShopServiceLog();
                log.floor = ((Double)ltm.get("floor")).intValue();
                log.cardsCorrupted = (List<String>)ltm.get("cardsCorrupted");
                log.cardsDuplicated = (List<String>)ltm.get("cardsDuplicated");
                log.cardsUpgraded = (List<String>)ltm.get("cardsUpgraded");
                log.cardsTransformed = (List<String>)ltm.get("cardsTransformed");
                log.cardsGained = (List<String>)ltm.get("cardsGained");
                l.add(log);
            }
            field.set(data, l);
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.MethodCallMatcher(Gson.class, "fromJson");
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(RunData.class, "timestamp");
                return LineFinder.findInOrder(ctMethodToPatch, Collections.singletonList(matcher), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = SpirePatch.CLASS)
    public static class ShopServiceLogField {
        public static final SpireField<ShopServiceLog> shopServiceLog = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = RunHistoryPath.class, method = "setRunData")
    public static class AddShopServiceLogDataPatch {
        @SuppressWarnings({"rawtypes", "unchecked"})
        @SpireInsertPatch(locator = Locator.class, localvars = { "element", "floor" })
        public static void addRewardsSkippedData(RunHistoryPath __instance, RunData newData, RunPathElement element, int floor) throws NoSuchFieldException, IllegalAccessException {
            Field field = newData.getClass().getField("shop_service_log");
            List shop_service_log = (List)field.get(newData);
            if (shop_service_log != null) {
                List<ShopServiceLog> shopServiceLogThisFloor = (List<ShopServiceLog>)shop_service_log.stream()
                        .filter(ssl -> ssl instanceof ShopServiceLog)
                        .filter(ssl -> ((ShopServiceLog)ssl).floor == floor)
                        .collect(Collectors.toList());
                if (!shopServiceLogThisFloor.isEmpty()) {
                    ShopServiceLogField.shopServiceLog.set(element, shopServiceLogThisFloor.get(0));
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
    public static class DisplayShopServiceLogDataPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "sb" })
        public static void hook(RunPathElement __instance, StringBuilder sb) {
            ShopServiceLog shopServiceLog = ShopServiceLogField.shopServiceLog.get(__instance);
            if (shopServiceLog != null && (!shopServiceLog.cardsCorrupted.isEmpty() || !shopServiceLog.cardsDuplicated.isEmpty() || !shopServiceLog.cardsUpgraded.isEmpty() || !shopServiceLog.cardsTransformed.isEmpty())) {
                for (String s : shopServiceLog.cardsCorrupted) {
                    sb.append(" NL ").append(MessageFormat.format(TEXT[0], CardLibrary.getCardNameFromMetricID(s)));
                }
                for (String s : shopServiceLog.cardsDuplicated) {
                    sb.append(" NL ").append(MessageFormat.format(TEXT[1], CardLibrary.getCardNameFromMetricID(s)));
                }
                for (String s : shopServiceLog.cardsUpgraded) {
                    sb.append(" NL ").append(MessageFormat.format(TEXT[2], CardLibrary.getCardNameFromMetricID(s)));
                }
                for (String s : shopServiceLog.cardsTransformed) {
                    sb.append(" NL ").append(MessageFormat.format(TEXT[3], CardLibrary.getCardNameFromMetricID(s)));
                }

                if (!shopServiceLog.cardsGained.isEmpty()) {
                    sb.append(" NL ").append(TEXT_OBTAIN_HEADER);
                    for (String s : shopServiceLog.cardsGained) {
                        sb.append(" NL ").append(" TAB ").append(TEXT_OBTAIN_TYPE_CARD).append(CardLibrary.getCardNameFromMetricID(s));
                    }
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher matcher = new Matcher.FieldAccessMatcher(RunPathElement.class, "shopPurchases");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
