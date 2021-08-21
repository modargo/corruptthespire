package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransitionStart")
public class NextRoomTransitionStartPatch {
    @SpirePrefixPatch
    public static void AddCorruptionForDoubleBoss() {
        //Pretty sure this isn't needed anymore due to checking and setting the field in NextRoomTransitionPatch
        //TODO remove dead code
        /*
        if (AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss
            && AbstractDungeon.nextRoom != null && AbstractDungeon.nextRoom.room instanceof MonsterRoomBoss) {
            CorruptedField.corrupted.set(AbstractDungeon.nextRoom, true);
        }
         */
    }
}
