package corruptthespire.events;

import corruptthespire.events.chaotic.*;
import corruptthespire.events.corrupted.IncantationOfCorruption;
import corruptthespire.events.corrupted.NightmareBloom;
import corruptthespire.events.corrupted.TheChoice;

import java.util.HashMap;
import java.util.Map;

public class CorruptedEventUtil {
    public static Map<String, CorruptedEventInfo> getAllCorruptedEvents() {
        Map<String, CorruptedEventInfo> corruptedEvents = new HashMap<>();
        corruptedEvents.put(AStepToTheSide.ID, new CorruptedEventInfo(AStepToTheSide.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(Divergence.ID, new CorruptedEventInfo(Divergence.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(MindsEye.ID, new CorruptedEventInfo(MindsEye.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(PathsInTheSmoke.ID, new CorruptedEventInfo(PathsInTheSmoke.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(Wheel.ID, new CorruptedEventInfo(Wheel.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(IncantationOfCorruption.ID, new CorruptedEventInfo(IncantationOfCorruption.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(NightmareBloom.ID, new CorruptedEventInfo(NightmareBloom.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(TheChoice.ID, new CorruptedEventInfo(TheChoice.class, CorruptedEventType.Corrupted));
        return corruptedEvents;
    }
}
