package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import corruptthespire.map.CorruptMap;
import javassist.CtBehavior;

@SpirePatch(clz = DungeonMap.class, method = "update")
public class DungeonMapUpdateBossRoomCorruptedPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"node"})
    public static void MarkBossNodeAsCorrupted(DungeonMap __instance, MapRoomNode node) {
        CorruptedField.corrupted.set(node, CorruptMap.isBossCorrupted());
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.NewExprMatcher(MonsterRoomBoss.class);
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
