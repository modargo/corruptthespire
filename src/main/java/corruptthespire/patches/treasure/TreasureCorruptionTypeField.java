package corruptthespire.patches.treasure;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import corruptthespire.corruptions.treasure.TreasureCorruptionType;

@SpirePatch(
        clz = TreasureRoom.class,
        method = SpirePatch.CLASS
)
public class TreasureCorruptionTypeField {
    public static final SpireField<TreasureCorruptionType> corruptionType = new SpireField<>(() -> null);
}
