package corruptthespire.patches.event;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.EventRoom;
import corruptthespire.corruptions.event.EventCorruptionType;

@SpirePatch(
        clz = EventRoom.class,
        method = SpirePatch.CLASS
)
public class EventCorruptionTypeField {
    public static SpireField<EventCorruptionType> corruptionType = new SpireField<>(() -> null);
}
