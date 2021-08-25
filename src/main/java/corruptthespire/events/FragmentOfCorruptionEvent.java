package corruptthespire.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.EventRoom;
import corruptthespire.CorruptTheSpire;
import corruptthespire.relics.FragmentOfCorruption;

import java.text.MessageFormat;

public class FragmentOfCorruptionEvent extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:FragmentOfCorruption";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private final AbstractRelic relic;

    public FragmentOfCorruptionEvent() {
        super(NAME, DESCRIPTIONS[0], getImage());

        this.relic = new FragmentOfCorruption();

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.relic.name));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relic);
        this.goToNormalEvent();
    }

    private void goToNormalEvent() {
        //We could also consider calling room.onPlayerEntry here instead of replicating the logic
        //We'd have to set a flag to bypass our own prefix patch, but it's doable
        //The upside is that we avoid duplicating logic and that any insert/instrument/raw patches to the method work
        //The downside is that any prefix patches might run twice, and that's enough of a downside to try this approach
        EventRoom room = ((EventRoom)AbstractDungeon.getCurrRoom());
        Random eventRngDuplicate = new Random(Settings.seed, AbstractDungeon.eventRng.counter);
        room.event = AbstractDungeon.generateEvent(eventRngDuplicate);
        room.event.onEnterRoom();
    }

    private static String getImage() {
        int NUM_IMAGES = 4;
        long n = (Settings.seed + AbstractDungeon.floorNum) % NUM_IMAGES;
        return CorruptTheSpire.eventImage(ID + n);
    }
}
