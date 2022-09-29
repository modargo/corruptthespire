package corruptthespire.patches.fight.room;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import corruptthespire.corruptions.fight.FightCorruption;
import corruptthespire.corruptions.fight.room.FightRoomCorruption;
import corruptthespire.corruptions.fight.room.FightRoomCorruptionType;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractRoom.class, method = "update")
public class FightRoomCorruptionAddRewardsPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void addRewards(AbstractRoom __instance) {
        if (__instance instanceof MonsterRoom) {
            FightRoomCorruption.addRewards(FightRoomCorruptionTypeField.roomCorruptionType.get(__instance));
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "addPotionToRewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
