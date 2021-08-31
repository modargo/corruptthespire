package corruptthespire.events;

import com.megacrit.cardcrawl.events.beyond.MindBloom;
import corruptthespire.Cor;
import corruptthespire.events.corrupted.NightmareBloom;

import java.util.ArrayList;

public class EventFilter {
    //Not supposed to be instantiated
    private EventFilter() {
        throw new AssertionError();
    }

    public static ArrayList<String> FilterEvents(ArrayList<String> events) {
        ArrayList<String> eventsToRemove = new ArrayList<>();
        for (String event : events) {
            if (event.equals(MindBloom.ID) && !Cor.corruptedEventList.contains(NightmareBloom.ID)) {
                eventsToRemove.add(event);
            }
        }
        return eventsToRemove;
    }
}