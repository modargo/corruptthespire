package corruptthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.map.MapRoomNode;

@SpirePatch(
        clz = MapRoomNode.class,
        method = SpirePatch.CLASS
)
public class CorruptedField {
    public static SpireField<Boolean> corrupted = new SpireField<>(() -> false);
}
