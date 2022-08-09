package corruptthespire.events.chaotic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import corruptthespire.CorruptTheSpire;
import corruptthespire.cards.TheFool;
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
    private static final int MAX_HEALTH_LOSS = 2;
    private static final int A15_MAX_HEALTH_LOSS = 3;

    private final int maxHealth;
    private final int maxHealthLoss;
    private final AbstractCard card;
    private final AbstractCard card2;

    private int screenNum = 0;

    public Wheel() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.maxHealth = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH : MAX_HEALTH;
        this.maxHealthLoss = Math.min(AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH_LOSS : MAX_HEALTH_LOSS, AbstractDungeon.player.maxHealth - 1);
        this.card = new WheelOfFortune();
        this.card2 = new TheFool();

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], this.card.name, this.maxHealth), this.card);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.card2.name, this.maxHealthLoss), this.card2);
        imageEventText.setDialogOption(OPTIONS[2]);
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
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Reach
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.card2, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHealthLoss);
                        logMetric(ID, "Reach", Collections.singletonList(this.card2.cardID), null, null, null, null, null, null, 0, 0, this.maxHealthLoss, 0, 0, 0);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Leave
                        logMetricIgnored(ID);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
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