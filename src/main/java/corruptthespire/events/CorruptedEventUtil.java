package corruptthespire.events;

import com.megacrit.cardcrawl.events.AbstractEvent;
import corruptthespire.events.chaotic.MindsEye;
import corruptthespire.events.corrupted.NightmareBloom;
import corruptthespire.events.corrupted.TheChoice;

import java.util.HashMap;
import java.util.Map;

public class CorruptedEventUtil {
    public static Map<String, Class<? extends AbstractEvent>> getAllCorruptedEvents() {
        Map<String, Class<? extends AbstractEvent>> corruptedEvents = new HashMap<>();
        corruptedEvents.put(TheChoice.ID, TheChoice.class);
        corruptedEvents.put(NightmareBloom.ID, NightmareBloom.class);
        return corruptedEvents;
    }

    public static Map<String, Class<? extends AbstractEvent>> getAllChaoticEvents() {
        Map<String, Class<? extends AbstractEvent>> chaoticEvents = new HashMap<>();
        chaoticEvents.put(MindsEye.ID, MindsEye.class);
        return chaoticEvents;
    }
}
