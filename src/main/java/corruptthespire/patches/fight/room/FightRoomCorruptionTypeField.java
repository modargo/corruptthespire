package corruptthespire.patches.fight.room;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import corruptthespire.corruptions.fight.room.FightRoomCorruptionType;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;

@SpirePatch(
        clz = MonsterRoom.class,
        method = SpirePatch.CLASS
)
public class FightRoomCorruptionTypeField {
    public static final SpireField<FightRoomCorruptionType> roomCorruptionType = new SpireField<>(() -> null);
}
