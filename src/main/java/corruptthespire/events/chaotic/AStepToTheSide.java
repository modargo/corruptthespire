package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.Fated;
import corruptthespire.relics.chaotic.DeckOfManyFates;

import java.text.MessageFormat;

public class AStepToTheSide extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:AStepToTheSide";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int GOLD = 150;
    private static final int A15_GOLD = 125;

    private final AbstractRelic relic;
    private final AbstractCard curse;
    private final int gold;

    private int screenNum = 0;

    public AStepToTheSide() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.relic = new DeckOfManyFates();
        this.curse = new Fated();
        this.gold = AbstractDungeon.ascensionLevel >= 15 ? A15_GOLD : GOLD;

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.relic.name), this.curse, this.relic);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.gold));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Deck
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.relic);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.curse, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        logMetricObtainCardAndRelic(ID, "Deck", this.curse, this.relic);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Drawing
                        AbstractDungeon.player.gainGold(this.gold);
                        logMetricGainGold(ID, "Drawing", this.gold);

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