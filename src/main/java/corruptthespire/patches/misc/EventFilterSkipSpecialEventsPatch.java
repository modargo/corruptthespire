package corruptthespire.patches.misc;

import basemod.eventUtil.AddEventParams;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import corruptthespire.events.special.DevourerEvent;
import corruptthespire.events.special.DoomEvent;
import corruptthespire.events.special.HarbingerEvent;

import java.util.Arrays;
import java.util.List;

@SpirePatch(cls = "eventfilter.patches.AddEventPatch", method = "Prefix", optional = true)
public class EventFilterSkipSpecialEventsPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> eventFilterSkipSpecialEvents(AddEventParams params) {
        // We hide these events from Event Filter because they're registered only for debugging purposes
        // The actual use of the event is in a specially constructed map node, meaning that these events don't use
        // either normal event generation or Corrupt the Spire corrupted event generation
        List<String> specialEvents = Arrays.asList(HarbingerEvent.ID, DevourerEvent.ID, DoomEvent.ID);
        if (specialEvents.contains(params.eventID)) {
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }
}
