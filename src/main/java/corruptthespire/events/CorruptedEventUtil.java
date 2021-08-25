package corruptthespire.events;

import corruptthespire.events.chaotic.MindsEye;
import corruptthespire.events.corrupted.NightmareBloom;
import corruptthespire.events.corrupted.TheChoice;

import java.util.HashMap;
import java.util.Map;

public class CorruptedEventUtil {
    public static Map<String, CorruptedEventInfo> getAllCorruptedEvents() {
        Map<String, CorruptedEventInfo> corruptedEvents = new HashMap<>();
        corruptedEvents.put(TheChoice.ID, new CorruptedEventInfo(TheChoice.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(NightmareBloom.ID, new CorruptedEventInfo(NightmareBloom.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(MindsEye.ID, new CorruptedEventInfo(MindsEye.class, CorruptedEventType.Chaotic));
        return corruptedEvents;
    }
}
