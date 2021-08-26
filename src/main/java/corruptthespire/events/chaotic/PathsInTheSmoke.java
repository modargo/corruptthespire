package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import corruptthespire.CorruptTheSpire;
import corruptthespire.patches.event.PathsInTheSmokePatch;

public class PathsInTheSmoke extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:PathsInTheSmoke";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private final String eventId0;
    private final String eventId1;
    private final String eventId2;

    private int screenNum = 0;

    public PathsInTheSmoke() {
        super(NAME, DESCRIPTIONS[0], IMG);

        Random eventRngDuplicate = new Random(Settings.seed, AbstractDungeon.eventRng.counter);
        PathsInTheSmokePatch.isActive = true;
        this.eventId0 = ((PathsInTheSmokePatch.DummyEvent)AbstractDungeon.generateEvent(eventRngDuplicate)).eventId;
        this.eventId1 = ((PathsInTheSmokePatch.DummyEvent)AbstractDungeon.generateEvent(eventRngDuplicate)).eventId;
        this.eventId2 = ((PathsInTheSmokePatch.DummyEvent)AbstractDungeon.generateEvent(eventRngDuplicate)).eventId;
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
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption("[" + CardCrawlGame.languagePack.getEventString(this.eventId0).NAME + "]");
                imageEventText.setDialogOption("[" + CardCrawlGame.languagePack.getEventString(this.eventId1).NAME + "]");
                imageEventText.setDialogOption("[" + CardCrawlGame.languagePack.getEventString(this.eventId2).NAME + "]");
                break;
            default:
                switch (buttonPressed) {
                    case 0:
                        this.goToEvent(this.eventId0);
                        break;
                    case 1:
                        this.goToEvent(this.eventId1);
                        break;
                    case 2:
                        this.goToEvent(this.eventId2);
                        break;
                }
                break;
        }
    }

    private void goToEvent(String eventId) {
        EventRoom room = ((EventRoom)AbstractDungeon.getCurrRoom());
        room.event = EventHelper.getEvent(eventId);
        room.event.onEnterRoom();
    }
}