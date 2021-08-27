package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.Fated;
import corruptthespire.cards.Nudge;

import java.text.MessageFormat;
import java.util.Collections;

public class WithoutBeginningOrEnd extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:WithoutBeginningOrEnd";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int CARDS = 2;
    private static final int GOLD = 50;
    private static final int A15_GOLD = 25;

    private final AbstractCard card;
    private final AbstractCard curse;
    private final int gold;

    private int screenNum = 0;

    public WithoutBeginningOrEnd() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.card = new Nudge();
        this.curse = new Fated();
        this.gold = AbstractDungeon.ascensionLevel >= 15 ? A15_GOLD : GOLD;

        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screenNum = 1;
                this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                this.imageEventText.clearRemainingOptions();
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                this.screenNum = 2;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], CARDS, this.card.name), this.card);
                this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.gold), this.curse);
                break;
            case 2:
                switch (buttonPressed) {
                    case 0: // Throw
                        for (int i = 0; i < CARDS; i++) {
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.card.makeCopy(), (float)Settings.WIDTH * (float)(i + 1) / (float)(CARDS + 1), (float)Settings.HEIGHT / 2.0F));
                        }
                        logMetricObtainCards(ID, "Throw", Collections.nCopies(CARDS, this.card.cardID));

                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screenNum = 3;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Turn Away
                        AbstractDungeon.player.gainGold(this.gold);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.curse, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        logMetricGainGold(ID, "Turn Away", this.gold);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.screenNum = 3;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
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