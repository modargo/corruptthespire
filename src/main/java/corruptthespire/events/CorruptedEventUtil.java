package corruptthespire.events;

import corruptthespire.events.chaotic.*;
import corruptthespire.events.corrupted.*;
import corruptthespire.relics.chaotic.HandOfMidas;

import java.util.HashMap;
import java.util.Map;

public class CorruptedEventUtil {
    public static Map<String, CorruptedEventInfo> getAllCorruptedEvents() {
        Map<String, CorruptedEventInfo> corruptedEvents = new HashMap<>();
        corruptedEvents.put(AStepToTheSide.ID, new CorruptedEventInfo(AStepToTheSide.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(BlessingsOfChaos.ID, new CorruptedEventInfo(BlessingsOfChaos.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(Divergence.ID, new CorruptedEventInfo(Divergence.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(GoldenVision.ID, new CorruptedEventInfo(GoldenVision.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(MindsEye.ID, new CorruptedEventInfo(MindsEye.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(PathsInTheSmoke.ID, new CorruptedEventInfo(PathsInTheSmoke.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(TreeOfSwords.ID, new CorruptedEventInfo(TreeOfSwords.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(UnderAStrangeSky.ID, new CorruptedEventInfo(UnderAStrangeSky.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(Wheel.ID, new CorruptedEventInfo(Wheel.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(WithoutBeginningOrEnd.ID, new CorruptedEventInfo(WithoutBeginningOrEnd.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(WorldsUponWorlds.ID, new CorruptedEventInfo(WorldsUponWorlds.class, CorruptedEventType.Chaotic));
        corruptedEvents.put(AncientLaboratory.ID, new CorruptedEventInfo(AncientLaboratory.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(CorruptedShrine.ID, new CorruptedEventInfo(CorruptedShrine.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(ForbiddenLibrary.ID, new CorruptedEventInfo(ForbiddenLibrary.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(FutureSight.ID, new CorruptedEventInfo(FutureSight.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(IncantationOfCorruption.ID, new CorruptedEventInfo(IncantationOfCorruption.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(Messenger.ID, new CorruptedEventInfo(Messenger.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(NightmareBloom.ID, new CorruptedEventInfo(NightmareBloom.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(SinisterTemple.ID, new CorruptedEventInfo(SinisterTemple.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(StrangeMarketplace.ID, new CorruptedEventInfo(StrangeMarketplace.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(TheChoice.ID, new CorruptedEventInfo(TheChoice.class, CorruptedEventType.Corrupted));
        corruptedEvents.put(TheDevice.ID, new CorruptedEventInfo(TheDevice.class, CorruptedEventType.Corrupted));
        return corruptedEvents;
    }
}
