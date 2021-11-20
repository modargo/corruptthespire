package corruptthespire.events.chaotic;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import corruptthespire.CorruptTheSpire;
import corruptthespire.patches.event.PathsInTheSmokePatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class PathsInTheSmoke extends AbstractImageEvent {
    private static final Logger logger = LogManager.getLogger(PathsInTheSmoke.class.getName());
    public static final String ID = "CorruptTheSpire:PathsInTheSmoke";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private final EventInfo eventInfo0;
    private final EventInfo eventInfo1;
    private final EventInfo eventInfo2;

    private int screenNum = 0;

    public PathsInTheSmoke() {
        super(NAME, DESCRIPTIONS[0], IMG);

        Random eventRngDuplicate = new Random(Settings.seed, AbstractDungeon.eventRng.counter);
        Map<String, EventStrings> events = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "events");
        PathsInTheSmokePatch.isActive = true;
        this.eventInfo0 = this.getEvent(eventRngDuplicate, events);
        this.eventInfo1 = this.getEvent(eventRngDuplicate, events);
        this.eventInfo2 = this.getEvent(eventRngDuplicate, events);
        PathsInTheSmokePatch.isActive = false;

        new RoomEventDialog();
        new GenericEventDialog();

        imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screenNum = 1;
                logger.info("Event IDs: " + this.eventInfo0.eventId + ", " + this.eventInfo1.eventId + ", " + this.eventInfo2.eventId);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption("[" + this.eventInfo0.eventName + "]");
                imageEventText.setDialogOption("[" + this.eventInfo1.eventName + "]");
                imageEventText.setDialogOption("[" + this.eventInfo2.eventName + "]");
                break;
            default:
                switch (buttonPressed) {
                    case 0:
                        this.goToEvent(this.eventInfo0.eventId);
                        break;
                    case 1:
                        this.goToEvent(this.eventInfo1.eventId);
                        break;
                    case 2:
                        this.goToEvent(this.eventInfo2.eventId);
                        break;
                }
                break;
        }
    }

    private EventInfo getEvent(Random rng, Map<String, EventStrings> events) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.eventId = null;
        eventInfo.eventName = null;
        while (true) {
            eventInfo.eventId = ((PathsInTheSmokePatch.DummyEvent)AbstractDungeon.generateEvent(rng)).eventId;
            String key = eventInfo.eventId;
            if (events.containsKey(key)) {
                eventInfo.eventName = events.get(key).NAME;
                break;
            }
            key = key.replace("_", " ");
            if (events.containsKey(key)) {
                eventInfo.eventName = events.get(key).NAME;
                break;
            }
            logger.info("Couldn't find name for event ID: " + eventInfo.eventId);
        }

        return eventInfo;
    }

    private void goToEvent(String eventId) {
        EventRoom room = ((EventRoom)AbstractDungeon.getCurrRoom());
        room.event = EventHelper.getEvent(eventId);
        room.event.onEnterRoom();
    }

    private static class EventInfo {
        public String eventId;
        public String eventName;
    }
}