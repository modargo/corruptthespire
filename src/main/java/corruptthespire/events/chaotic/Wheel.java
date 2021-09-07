package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.WheelOfFortune;

import java.text.MessageFormat;
import java.util.Collections;

public class Wheel extends AbstractImageEvent {
    public static final String ID = "CorruptTheSpire:Wheel";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG = CorruptTheSpire.eventImage(ID);

    private static final int MAX_HEALTH = 2;
    private static final int A15_MAX_HEALTH = 1;

    private final int maxHealth;
    private final AbstractCard card;

    private int screenNum = 0;

    public Wheel() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.maxHealth = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH : MAX_HEALTH;
        this.card = new WheelOfFortune();

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.card.name, this.maxHealth), this.card);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Touch
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.player.increaseMaxHp(this.maxHealth, true);
                        logMetric(ID, "Touch", Collections.singletonList(this.card.cardID), null, null, null, null, null, null, 0, 0, 0, this.maxHealth, 0, 0);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Leave
                        logMetricIgnored(ID);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
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