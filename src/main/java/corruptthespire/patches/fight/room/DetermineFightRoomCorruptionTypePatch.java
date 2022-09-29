package corruptthespire.patches.fight.room;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import corruptthespire.corruptions.fight.room.FightRoomCorruptionDistribution;
import corruptthespire.corruptions.fight.room.FightRoomCorruptionType;
import corruptthespire.patches.core.CorruptedField;

@SpirePatch(clz = MonsterRoom.class, method = "onPlayerEntry")
public class DetermineFightRoomCorruptionTypePatch {
    @SpirePrefixPatch
    public static void determineFightRoomCorruptionType(MonsterRoom __instance) {
        if (CorruptedField.corrupted.get(AbstractDungeon.getCurrMapNode())) {
            FightRoomCorruptionType roomCorruptionType = new FightRoomCorruptionDistribution().roll();
            FightRoomCorruptionTypeField.roomCorruptionType.set(__instance, roomCorruptionType);
        }
    }
}
