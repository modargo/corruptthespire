package corruptthespire.events;

import corruptthespire.events.chaotic.AStepToTheSide;
import corruptthespire.events.chaotic.MindsEye;
import corruptthespire.events.chaotic.Wheel;
import corruptthespire.events.corrupted.NightmareBloom;
import corruptthespire.events.corrupted.TheChoice;

import java.util.HashMap;
import java.util.Map;

public class CorruptedEventUtil {
    public static Map<String, CorruptedEventInfo> getAllCorruptedEvents() {
        Map<String, CorruptedEventInfo> corruptedEvents = new HashMap<>();
        corruptedEvents.put(AStepToTheSide.ID, new CorruptedEventInfo(AStepToTheSide.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(MindsEye.ID, new CorruptedEventInfo(MindsEye.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(Wheel.ID, new CorruptedEventInfo(Wheel.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(NightmareBloom.ID, new CorruptedEventInfo(NightmareBloom.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(TheChoice.ID, new CorruptedEventInfo(TheChoice.class, CorruptedEventType.Corrupted));
        return corruptedEvents;
    }
}
