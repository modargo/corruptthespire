package corruptthespire.events.corrupted;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import corruptthespire.Cor;
import corruptthespire.CorruptTheSpire;

public class TheChoice extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:TheChoice";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    public static int CORRUPTION_GAIN = 5;
    public static int CORRUPTION_REDUCTION = 5;

    private int screenNum = 0;

    public TheChoice() {
        super(NAME, DESCRIPTIONS[0], IMG);

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    //TODO Implement rewards
                    case 0: // Embrace
                        Cor.addCorruption(CORRUPTION_GAIN);
                        //logMetric(ID, )

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Reject
                        Cor.addCorruption(-CORRUPTION_REDUCTION);
                        //logMetric(ID, )

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }
}
