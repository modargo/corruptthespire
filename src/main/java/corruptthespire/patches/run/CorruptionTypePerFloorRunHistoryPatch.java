package corruptthespire.patches.run;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryPath;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.RunData;
import corruptthespire.Cor;
import corruptthespire.corruptions.event.EventCorruptionType;
import corruptthespire.corruptions.fight.FightCorruptionInfo;
import corruptthespire.corruptions.fight.FightCorruptionType;
import corruptthespire.corruptions.shop.ShopCorruptionType;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;
import corruptthespire.events.special.SealedChestEvent;
import corruptthespire.events.special.TreasureWardensEventRoom;
import corruptthespire.patches.core.CorruptedField;
import corruptthespire.patches.event.EventCorruptionTypeField;
import corruptthespire.patches.fight.FightCorruptionInfosField;
import corruptthespire.patches.shop.ShopCorruptionTypeField;
import corruptthespire.patches.treasure.TreasureCorruptionTypeField;
import corruptthespire.savables.logs.CorruptionTypePerFloorLog;
import javassist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CorruptionTypePerFloorRunHistoryPatch {
    private static final Logger logger = LogManager.getLogger(CorruptionTypePerFloorRunHistoryPatch.class.getName());
    private static final Map<String, String> TEXT_DICT = CardCrawlGame.languagePack.getUIString("CorruptTheSpire:CorruptionType").TEXT_DICT;

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class CorruptionTypePerFloorField {
        @SpireRawPatch
        public static void addCorruptionTypePerFloor(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass runData = ctBehavior.getDeclaringClass().getClassPool().get(RunData.class.getName());

            String fieldSource = "public java.util.List corruption_type_per_floor;";

            CtField field = CtField.make(fieldSource, runData);

            runData.addField(field);
        }
    }

    @SpirePatch(clz = Metrics.class, method = "gatherAllData")
    public static class GatherAllDataPatch {
        @SpirePostfixPatch
        public static void gatherAllDataPatch(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters) {
            if (Cor.active) {
                if (death) {
                    CorruptionTypePerFloorLog.corruptionTypePerFloorLog.add(getCorruptionType());
                }
                ReflectionHacks.privateMethod(Metrics.class, "addData", Object.class, Object.class)
                        .invoke(__instance, "corruption_type_per_floor", CorruptionTypePerFloorLog.corruptionTypePerFloorLog);
            }
        }
    }

    @SpirePatch(clz = RunPathElement.class, method = SpirePatch.CLASS)
    public static class CorruptionTypeField {
        public static final SpireField<String> corruptionType = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = RunHistoryPath.class, method = "setRunData")
    public static class AddCorruptionDataPatch {
        @SuppressWarnings("rawtypes")
        @SpireInsertPatch(locator = Locator.class, localvars = { "element", "i" })
        public static void addCorruptionData(RunHistoryPath __instance, RunData newData, RunPathElement element, int i) throws NoSuchFieldException, IllegalAccessException {
            Field field = newData.getClass().getField("corruption_type_per_floor");
            List corruption_type_per_floor = (List)field.get(newData);
            if (corruption_type_per_floor != null && i < corruption_type_per_floor.size()) {
                Object corruptionType = corruption_type_per_floor.get(i);
                String s = null;
                if (corruptionType instanceof String) {
                    s = (String)corruptionType;
                }
                else {
                    logger.warn("Unrecognized corruption_type_per_floor data: " + corruptionType);
                }
                CorruptionTypeField.corruptionType.set(element, s);
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

    public static void displayCorruptionTypeData(RunPathElement element, StringBuilder sb) {
        String corruptionType = CorruptionTypeField.corruptionType.get(element);
        if (corruptionType != null) {
            sb.append(" NL TAB ").append(TEXT_DICT.get(corruptionType));
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "incrementFloorBasedMetrics")
    public static class CorruptionTypePerFloorAddLoggingPatch {
        @SpirePostfixPatch
        public static void incrementFloorBasedMetricsPatch(AbstractDungeon __instance) {
            if (AbstractDungeon.floorNum != 0 && Cor.active) {
                CorruptionTypePerFloorLog.corruptionTypePerFloorLog.add(getCorruptionType());
            }
        }
    }

    public static String getCorruptionType() {
        MapRoomNode node = AbstractDungeon.getCurrMapNode();
        if (node == null || node.room == null || !CorruptedField.corrupted.get(node)) {
            return null;
        }
        String corruptionType;
        AbstractRoom room = node.room;
        if (room instanceof TreasureWardensEventRoom) {
            corruptionType = TreasureCorruptionType.Wardens.name();
        }
        else if (room instanceof EventRoom && ((EventRoom)room).event instanceof SealedChestEvent) {
            corruptionType = TreasureCorruptionType.Sealed.name();
        }
        else if (room instanceof EventRoom) {
            EventCorruptionType eventCorruptionType = EventCorruptionTypeField.corruptionType.get(room);
            corruptionType = eventCorruptionType != null ? eventCorruptionType.name() : null;
        }
        else if (room instanceof MonsterRoom) {
            List<FightCorruptionInfo> fightCorruptionInfos = FightCorruptionInfosField.corruptionInfos.get(room);
            corruptionType = fightCorruptionInfos != null && fightCorruptionInfos.size() == 1 ? fightCorruptionInfos.get(0).corruptionType.name() : null;
            if (fightCorruptionInfos != null && fightCorruptionInfos.size() > 1) {
                logger.warn("Unexpected room with multiple fight corruption on floor " + AbstractDungeon.floorNum);
            }
        }
        else if (room instanceof ShopRoom) {
            ShopCorruptionType shopCorruptionType = ShopCorruptionTypeField.corruptionType.get(room);
            corruptionType = shopCorruptionType != null ? shopCorruptionType.name() : null;
        }
        else if (room instanceof TreasureRoom) {
            TreasureCorruptionType treasureCorruptionType = TreasureCorruptionTypeField.corruptionType.get(room);
            corruptionType = treasureCorruptionType != null ? treasureCorruptionType.name() : null;
        }
        else {
            logger.warn("Unexpected null corruptionType for corrupted room on floor " + AbstractDungeon.floorNum);
            corruptionType = null;
        }

        return corruptionType;
    }

    private enum RoomType {
        Event,
        Fight,
        Shop,
        Treasure
    }

    private static class CorruptionTypeInfo {
        public CorruptionTypeInfo(RoomType roomType, Object value, String s) {
            this.roomType = roomType;
            this.value = value;
            this.s = s;
        }
        public RoomType roomType;
        public Object value;
        public String s;
    }

    private static Map<String, CorruptionTypeInfo> corruptionTypeMap = null;

    public static void buildCorruptionTypeMapping() {
        if (corruptionTypeMap == null) {
            corruptionTypeMap = new HashMap<>();
            addCorruptionTypeInfos(EventCorruptionType.values(), RoomType.Event);
            addCorruptionTypeInfos(FightCorruptionType.values(), RoomType.Fight);
            addCorruptionTypeInfos(ShopCorruptionType.values(), RoomType.Shop);
            addCorruptionTypeInfos(TreasureCorruptionType.values(), RoomType.Treasure);
            logger.info("Built corruption type mapping, " + corruptionTypeMap.size() + " entries");
        }
    }

    @SuppressWarnings("rawtypes")
    private static void addCorruptionTypeInfos(Enum[] values, RoomType roomType) {
        List<CorruptionTypeInfo> infos = Arrays.stream(values).map(v -> new CorruptionTypeInfo(roomType, v, v.name())).collect(Collectors.toList());
        for (CorruptionTypeInfo info : infos) {
            if (corruptionTypeMap.containsKey(info.s)) {
                throw new RuntimeException("Duplicate corruption types detected. Key: " + info.s + ", room types: " + info.roomType.name() + ", " + corruptionTypeMap.get(info.s).roomType.name());
            }
            corruptionTypeMap.put(info.s, info);
        }
    }
}
