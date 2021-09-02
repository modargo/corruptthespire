package corruptthespire.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import corruptthespire.Cor;

public class CorruptionPerFloorPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "incrementFloorBasedMetrics")
    public static class IncrementFloorBasedMetricsPatch {
        @SpirePostfixPatch
        public static void incrementFloorBasedMetricsPatch(AbstractDungeon __instance) {
            if (AbstractDungeon.floorNum != 0) {
                Cor.corruptionPerFloor.add(Cor.corruption);
            }
        }
    }

    @SpirePatch(clz = Metrics.class, method = "gatherAllData")
    public static class GatherAllDataPatch {
        @SpirePostfixPatch
        public static void gatherAllDataPatch(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters) {
            if (death) {
                Cor.corruptionPerFloor.add(Cor.corruption);
            }
            ReflectionHacks.privateMethod(Metrics.class, "addData", Object.class, Object.class)
                    .invoke(__instance, "corruption_per_floor", Cor.corruptionPerFloor);
        }
    }
}
