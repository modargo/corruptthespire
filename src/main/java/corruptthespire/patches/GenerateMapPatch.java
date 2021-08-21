package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import corruptthespire.map.CorruptMap;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "generateMap"
)
@SpirePatch(
        clz = TheEnding.class,
        method = "generateSpecialMap"
)
@SpirePatch(
        cls = "abyss.act.VoidAct",
        method = "makeMap",
        optional = true
)
public class GenerateMapPatch {
    @SpirePostfixPatch
    public static void MarkCorruptedNodes() {
        CorruptMap.markCorruptedNodes();
    }
}
