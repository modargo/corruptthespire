package corruptthespire.events;

import com.megacrit.cardcrawl.cards.green.Nightmare;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import corruptthespire.Cor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class EventFilter {
    private static final Logger logger = LogManager.getLogger(EventFilter.class.getName());

    //Not supposed to be instantiated
    private EventFilter() {
        throw new AssertionError();
    }

    public static ArrayList<String> FilterEvents(ArrayList<String> events) {
        ArrayList<String> eventsToRemove = new ArrayList<>();
        for (String event : events) {
            if (event.equals(MindBloom.ID) && !Cor.corruptedEventList.contains(Nightmare.ID)) {
                eventsToRemove.add(event);
            }
        }
        return eventsToRemove;
    }
}