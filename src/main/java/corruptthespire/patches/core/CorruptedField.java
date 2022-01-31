package corruptthespire.patches.core;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.map.MapRoomNode;

@SpirePatch(
        clz = MapRoomNode.class,
        method = SpirePatch.CLASS
)
public class CorruptedField {
    public static final SpireField<Boolean> corrupted = new SpireField<>(() -> false);
}
