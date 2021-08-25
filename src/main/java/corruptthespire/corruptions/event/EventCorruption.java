package corruptthespire.corruptions.event;

import com.megacrit.cardcrawl.daily.mods.Diverse;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.rooms.EventRoom;
import corruptthespire.Cor;
import corruptthespire.events.CorruptedEventUtil;
import corruptthespire.events.FragmentOfCorruptionEvent;
import corruptthespire.events.chaotic.MindsEye;
import corruptthespire.events.corrupted.NightmareBloom;
import corruptthespire.events.corrupted.TheChoice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventCorruption {
    public static final Logger logger = LogManager.getLogger(EventCorruption.class.getName());

    public static AbstractEvent handleFragment() {
        return new FragmentOfCorruptionEvent();
    }

    public static AbstractEvent handleCorruptedEvent() {
        return handleEvent(Cor.corruptedEventList, CorruptedEventUtil.getAllCorruptedEvents());
    }

    public static AbstractEvent handleChaoticEvent() {
        return handleEvent(Cor.chaoticEventList, CorruptedEventUtil.getAllChaoticEvents());
    }

    private static AbstractEvent handleEvent(ArrayList<String> eventList, Map<String, Class<? extends AbstractEvent>> allEvents) {
        ArrayList<String> possibleEvents = filterEvents(eventList);

        if (possibleEvents.isEmpty()) {
            return null;
        }

        String eventId = possibleEvents.remove(Cor.rng.random(possibleEvents.size() - 1));
        Class<? extends AbstractEvent> eventClass = allEvents.get(eventId);
        try {
            return eventClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.info("Failed to instantiate event: " + eventClass.getName());
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<String> filterEvents(ArrayList<String> corruptedEventList) {
        return corruptedEventList.stream().filter(EventCorruption::keepEvent).collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean keepEvent(String e) {
        if (e.equals(TheChoice.ID)) {
            return Cor.corruption >= TheChoice.CORRUPTION_REDUCTION;
        }

        if (e.equals(NightmareBloom.ID)) {
            return AbstractDungeon.actNum == 3 && AbstractDungeon.eventList.contains(MindBloom.ID);
        }

        if (e.equals(MindsEye.ID)) {
            return !ModHelper.isModEnabled(Diverse.ID) && !AbstractDungeon.player.hasRelic(PrismaticShard.ID);
        }

        return true;
    }
}
